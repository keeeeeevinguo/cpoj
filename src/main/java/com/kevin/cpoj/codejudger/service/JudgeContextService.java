package com.kevin.cpoj.codejudger.service;

import com.kevin.cpoj.codejudger.config.JudgeConfig;
import com.kevin.cpoj.codejudger.exception.ExceedMaxTriesException;
import com.kevin.cpoj.codejudger.exception.InvalidDataException;
import com.kevin.cpoj.codejudger.model.ELanguage;
import com.kevin.cpoj.codejudger.model.JudgeContext;
import com.kevin.cpoj.codejudger.model.TestCaseContext;
import com.kevin.cpoj.domain.Problem;
import com.kevin.cpoj.domain.Submission;
import com.kevin.cpoj.domain.TestCase;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class JudgeContextService
{
    private final static Logger log = LoggerFactory.getLogger(JudgeContextService.class);
    private final Pattern javaClassPattern;

    private final JudgeDBService judgeDBService;
    private final JudgeConfig judgeConfig;

    @Autowired
    public JudgeContextService(final JudgeDBService judgeDBService, final JudgeConfig judgeConfig)
    {
        this.judgeDBService = judgeDBService;
        this.judgeConfig = judgeConfig;
        //http://stackoverflow.com/questions/14885315/regex-pattern-to-find-java-class-in-java-file
        javaClassPattern = Pattern.compile(".*\\s*public\\s+class\\s+(\\w+)(\\s+(extends\\s+\\w+)|(implements\\s+\\w+( ,\\w+)*))?\\s*\\{?.*");
    }


    /**
     * Populate judgeContext data and validate them
     */
    @Transactional(readOnly = true)
    public void populateAndValidateJudgeContext(final JudgeContext judgeContext, final Submission submission)
    {
        try
        {
            //Submission
            judgeContext.setRawSubmission(submission);
            validateSubmission(submission, judgeContext);

            //Question
            final Problem question = judgeDBService.findQuestionById(judgeContext.getQuestionId());
            if (question == null)
            {
                log.error("The submission does not have a question associated to it. " + submission);
                throw new InvalidDataException("The submission does not have a question associated to it.");
            }
            judgeContext.setRawQuestion(question);
            validateQuestion(question, judgeContext);
        }
        catch (final InvalidDataException | ExceedMaxTriesException e)
        {
            throw e;
        }
        catch (final Exception e)
        {
            log.error("Failed to validate submission: " + submission, e);
            throw new InvalidDataException("Failed to validate submission. " + e.getMessage());
        }
    }

    private void validateSubmission(final Submission submission, final JudgeContext judgeContext)
    {
        try
        {
            judgeContext.setSubmissionId(submission.getId());

            judgeContext.setLanguage(ELanguage.getLanguage(submission.getProgrammingLanguage()));
            if (judgeContext.getLanguage() == null)
            {
                log.error("The submission solution language: " + submission.getProgrammingLanguage() + " is invalid: " + submission);
                throw new InvalidDataException("The submission solution language: " + submission.getProgrammingLanguage() + " is invalid");
            }

            judgeContext.setProgramCode(submission.getCode());

            final String programFileName = getProgramFileName(judgeContext.getLanguage(), judgeContext.getProgramCode());
            judgeContext.setProgramFileName(programFileName);

            final String compiledFileName = getCompiledFileName(judgeContext.getLanguage(), programFileName);
            judgeContext.setCompiledFileName(compiledFileName);

            if (submission.getProblem() == null)
            {
                log.error("The submission does not have a question id. " + submission);
                throw new InvalidDataException("The submission does not have a question id.");
            }
            judgeContext.setQuestionId(submission.getProblem().getId());

            judgeContext.setTries(submission.getOverallResultTries());
            if (submission.getOverallResultTries() >= judgeConfig.getJudgeMaxTries())
            {
                log.error("This submission exceeds the max tries, marked it internal error");
                throw new ExceedMaxTriesException("This submission exceeds the max tries");
            }
        }
        catch (final InvalidDataException | ExceedMaxTriesException e)
        {
            throw e;
        }
        catch (final Exception e)
        {
            log.error("Failed to validate submission: " + submission, e);
            throw new InvalidDataException("Failed to validate submission. " + e.getMessage());
        }
    }

    private String getProgramFileName(final ELanguage language, final String programCode)
    {
        String programFileName = "Program";

        if (ELanguage.JAVA.equals(language))
        {
            final Matcher matcher = javaClassPattern.matcher(programCode);
            if (matcher.find())
            {
                programFileName = matcher.group(1);
            }
            else
            {
                log.warn("Could not find class name for java code: " + programCode + "\n Use Program.java instead");
            }
        }

        final String extension = language.getFileExtension();

        return programFileName + extension;
    }

    private String getCompiledFileName(final ELanguage language, final String programFileName)
    {
        switch (language)
        {
            case JAVA:
                return programFileName.replaceAll("\\.java", "\\.class");
            case C:
            case CPP:
                return programFileName.substring(0, programFileName.lastIndexOf("."));
            case PYTHON2:
            case PYTHON3:
            default:
                return programFileName;
        }
    }

    private void validateQuestion(final Problem question, final JudgeContext judgeContext)
    {
        try
        {
            List<TestCase> testCases = new ArrayList<>(question.getTestCases());
            if (CollectionUtils.isEmpty(testCases))
            {
                log.error("The question does not have test cases: " + question);
                throw new InvalidDataException("The question has 0 test case.");
            }

            testCases.sort(new Comparator<TestCase>()
            {
                @Override
                public int compare(TestCase o1, TestCase o2)
                {
                    return Long.compare(o1.getId(), o2.getId());
                }
            });

            for (int i = 0; i < testCases.size(); i++)
            {
                TestCase testCase = testCases.get(i);
                String inputFileName = "input" + (i + 1) + ".txt";
                String inputData = testCase.getInputData();
                if (StringUtils.isBlank(inputData))
                {
                    log.error("The test case input data is blank: " + testCase);
                    throw new InvalidDataException("The test case input data is blank.");
                }
                final String outputFileName = "output" + (i + 1) + ".txt";
                final String outputData = testCase.getOutputData();
                if (StringUtils.isBlank(outputData))
                {
                    log.error("The test case output data is blank: " + testCase);
                    throw new InvalidDataException("The test case output data is blank.");
                }
                final Integer timeLimitInMS = testCase.getTimeLimitInMS();
                if (timeLimitInMS == null)
                {
                    log.error("The test case time limit is blank: " + testCase);
                    throw new InvalidDataException("The test case time limit is blank.");
                }
                final Integer weight = testCase.getWeightPercentage();
                if (weight == null)
                {
                    log.error("The test case score weight is blank: " + testCase);
                    throw new InvalidDataException("The test case score weight is blank.");
                }
                judgeContext.getTestCaseContextList().add(new TestCaseContext(inputFileName, inputData, outputFileName, outputData, timeLimitInMS, weight));
            }
        }
        catch (final InvalidDataException | ExceedMaxTriesException e)
        {
            throw e;
        }
        catch (final Exception e)
        {
            log.error("Failed to validate question: " + question, e);
            throw new InvalidDataException("Failed to validate question. " + e.getMessage());
        }
    }
}
