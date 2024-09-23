package com.kevin.cpoj.codejudger.service;

import com.kevin.cpoj.codejudger.config.JudgeConfig;
import com.kevin.cpoj.codejudger.model.ERuntimeResult;
import com.kevin.cpoj.codejudger.model.JudgeContext;
import com.kevin.cpoj.codejudger.model.TestCaseContext;
import com.kevin.cpoj.domain.Problem;
import com.kevin.cpoj.domain.Submission;
import com.kevin.cpoj.domain.SubmissionTestCaseResult;
import com.kevin.cpoj.domain.enumeration.SubmissionResultMessage;
import com.kevin.cpoj.domain.enumeration.SubmissionResultStatus;
import com.kevin.cpoj.domain.enumeration.TestCaseResultMessage;
import com.kevin.cpoj.repository.ProblemRepository;
import com.kevin.cpoj.repository.SubmissionRepository;
import com.kevin.cpoj.repository.SubmissionTestCaseResultRepository;
import com.kevin.cpoj.service.SubmissionQueryService;
import com.kevin.cpoj.service.criteria.SubmissionCriteria;
import com.kevin.cpoj.service.dto.SubmissionDTO;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
@Transactional
public class JudgeDBService {

    private static final Logger log = LoggerFactory.getLogger(JudgeDBService.class);

    private final JudgeConfig judgeConfig;
    private SubmissionRepository submissionRepository;
    private SubmissionQueryService submissionQueryService;
    private ProblemRepository problemRepository;
    private SubmissionTestCaseResultRepository testCaseResultRepository;

    @Autowired
    public JudgeDBService(
        JudgeConfig judgeConfig,
        SubmissionRepository submissionRepository,
        SubmissionQueryService submissionQueryService,
        ProblemRepository problemRepository,
        SubmissionTestCaseResultRepository testCaseResultRepository
    ) {
        this.judgeConfig = judgeConfig;
        this.submissionRepository = submissionRepository;
        this.submissionQueryService = submissionQueryService;
        this.problemRepository = problemRepository;
        this.testCaseResultRepository = testCaseResultRepository;
    }

    private static Date getNSecondsAgo(final int seconds) {
        return DateUtils.addSeconds(new Date(), -seconds);
    }

    public Submission markSubmissionInvalid(final Long submissionId, final String message) {
        if (submissionId == null) return null;

        Submission submission = submissionRepository.getById(submissionId);
        submission.setOverallResultStatus(SubmissionResultStatus.JUDGED);
        submission.setOverallResultMessage(SubmissionResultMessage.INVALID_SUBMISSION);
        submission.setOverallResultScorePercentage(0);
        submission.setOverallResultMessageDetail(message);
        submission = submissionRepository.saveAndFlush(submission);
        return submission;
    }

    public Submission markSubmissionInternalError(final Long submissionId, final String message) {
        if (submissionId == null) return null;
        Submission submission = submissionRepository.getById(submissionId);
        submission.setOverallResultStatus(SubmissionResultStatus.JUDGED);
        submission.setOverallResultMessage(SubmissionResultMessage.INTERNAL_ERROR);
        submission.setOverallResultScorePercentage(0);
        submission.setOverallResultMessageDetail(message);
        submission = submissionRepository.saveAndFlush(submission);
        return submission;
    }

    public Submission requeueSubmission(final Long submissionId, final String message) {
        if (submissionId == null) return null;

        Submission submission = submissionRepository.getById(submissionId);
        submission.setOverallResultStatus(SubmissionResultStatus.WAIT);
        submission.setOverallResultMessage(SubmissionResultMessage.NA);
        submission.setOverallResultScorePercentage(0);
        submission.setOverallResultMessageDetail(message);
        submission = submissionRepository.saveAndFlush(submission);
        return submission;
    }

    @Transactional
    public Submission getAndLockNextSubmissionJob() {
        //    final MongoCollection<Document> collection = this.database.getCollection("submissions");
        //
        //    final Bson filter =
        //        or(
        //            and(
        //                lt("overallResultTries", judgeConfig.getJudgeMaxTries() + 1),
        //                eq("overallResultStatus", "Waiting to be judged")
        //                //eq("overallResultStatus", "Judged")
        //            ),
        //            and(
        //                lt("overallResultTries", judgeConfig.getJudgeMaxTries() + 1),
        //                eq("overallResultStatus", "Judging"),
        //                lt("lastModified", getNSecondsAgo(judgeConfig.getJudgeExpiredSeconds()))
        //            )
        //        );
        SubmissionDTO submissionDto = getFirstWaitingSubmission();
        if (submissionDto == null) {
            return null;
        }
        Submission submission = submissionRepository.getById(submissionDto.getId());
        submission.setOverallResultStatus(SubmissionResultStatus.JUDGING);
        submission.setOverallResultTries(submission.getOverallResultTries() + 1);

        submission = submissionRepository.saveAndFlush(submission);

        return submission;
    }

    private SubmissionDTO getFirstWaitingSubmission() {
        SubmissionCriteria.SubmissionResultStatusFilter submissionResultStatusFilter = new SubmissionCriteria.SubmissionResultStatusFilter();
        submissionResultStatusFilter.setEquals(SubmissionResultStatus.WAIT);
        SubmissionCriteria submissionCriteria = new SubmissionCriteria();
        submissionCriteria.setOverallResultStatus(submissionResultStatusFilter);
        Pageable top1 = PageRequest.of(0, 1);
        Page<SubmissionDTO> submission = submissionQueryService.findByCriteria(submissionCriteria, top1);
        if (CollectionUtils.isEmpty(submission.getContent())) {
            return null;
        }
        return submission.getContent().get(0);
    }

    public Submission updateSubmissionWithCompilationError(final Long submissionId, final String compilationErrorMsg) {
        if (submissionId == null) return null;

        Submission submission = submissionRepository.getById(submissionId);
        submission.setOverallResultStatus(SubmissionResultStatus.JUDGED);
        submission.setOverallResultMessage(SubmissionResultMessage.COMPILE_TIME_ERROR);
        submission.setOverallResultScorePercentage(0);
        submission.setOverallResultMessageDetail(compilationErrorMsg);
        submission = submissionRepository.saveAndFlush(submission);
        return submission;
    }

    public Submission updateSubmissionWithRuntimeResult(final Long submissionId, final JudgeContext judgeContext) {
        if (submissionId == null || judgeContext == null) return null;
        Submission submission = submissionRepository.getById(submissionId);

        final int scorePercentage = calculateSubmissionScore(judgeContext);
        final SubmissionResultMessage result = (scorePercentage == 100 ? SubmissionResultMessage.PASS : SubmissionResultMessage.NOT_PASS);

        final List<SubmissionTestCaseResult> testCaseResults = new ArrayList<>();

        List<TestCaseContext> testCaseContextList = judgeContext.getTestCaseContextList();
        for (int i = 0; i < testCaseContextList.size(); i++) {
            TestCaseContext testCaseContext = testCaseContextList.get(i);
            SubmissionTestCaseResult testCaseResult = new SubmissionTestCaseResult();
            testCaseResult.setSubmission(submission);
            testCaseResult.setName(submission.getName() + "-TCR-" + (i + 1));
            String name = testCaseResult.getName();
            if (name.length() > 20) {
                name = name.substring(name.length() - 20);
            }
            testCaseResult.setName(name);
            testCaseResult.setResultMessage(TestCaseResultMessage.getTestCaseResultMessage(testCaseContext.getRuntimeResult()));
            String resultMsg = testCaseContext.getRuntimeResultMsg();
            if (resultMsg != null) {
                resultMsg = resultMsg.substring(0, Math.min(20, resultMsg.length()));
            }
            testCaseResult.setResultMessageDetail(resultMsg);
            testCaseResult.setElapsedTimeInMS(testCaseContext.getTimeLimitInMS());

            testCaseResults.add(testCaseResult);
        }

        testCaseResultRepository.saveAll(testCaseResults);

        submission.setOverallResultStatus(SubmissionResultStatus.JUDGED);
        submission.setOverallResultMessage(result);
        submission.setOverallResultScorePercentage(scorePercentage);
        submission.setOverallResultMessageDetail(result.toString());
        submission = submissionRepository.saveAndFlush(submission);

        return submission;
    }

    private int calculateSubmissionScore(final JudgeContext judgeContext) {
        int score = 0;
        int totalWeight = 0;
        for (final TestCaseContext testCaseContext : judgeContext.getTestCaseContextList()) {
            totalWeight += testCaseContext.getWeightPercentage();
            if (ERuntimeResult.PASS.equals(testCaseContext.getRuntimeResult())) {
                score += testCaseContext.getWeightPercentage();
            }
        }
        if (totalWeight == 100) return score; else return Math.min(100, (int) Math.ceil((double) score * 100 / totalWeight));
    }

    @Transactional(readOnly = true)
    public Problem findQuestionById(final long id) {
        return problemRepository.getById(id);
    }

    public Submission markSubmissionExceedMaxTries(final Long submissionId, final int tries) {
        if (submissionId == null) return null;

        Submission submission = submissionRepository.getById(submissionId);
        submission.setOverallResultStatus(SubmissionResultStatus.JUDGED);
        submission.setOverallResultMessage(SubmissionResultMessage.INTERNAL_ERROR);
        submission.setOverallResultScorePercentage(0);
        submission.setOverallResultMessageDetail(
            "Tried to judge this submission for " + tries + " times, but never succeeded. Please submit your solution again."
        );
        submission = submissionRepository.saveAndFlush(submission);
        return submission;
    }
}
