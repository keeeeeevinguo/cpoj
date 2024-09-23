package com.kevin.cpoj.codejudger.scheduledtask;

import com.kevin.cpoj.codejudger.config.JudgeConfig;
import com.kevin.cpoj.codejudger.exception.ExceedMaxTriesException;
import com.kevin.cpoj.codejudger.exception.FatalErrorException;
import com.kevin.cpoj.codejudger.exception.InvalidDataException;
import com.kevin.cpoj.codejudger.exception.MinorErrorException;
import com.kevin.cpoj.codejudger.model.ERuntimeResult;
import com.kevin.cpoj.codejudger.model.ExecCmdResult;
import com.kevin.cpoj.codejudger.model.JudgeContext;
import com.kevin.cpoj.codejudger.model.TestCaseContext;
import com.kevin.cpoj.codejudger.service.ExecCmdService;
import com.kevin.cpoj.codejudger.service.JudgeContextService;
import com.kevin.cpoj.codejudger.service.JudgeDBService;
import com.kevin.cpoj.codejudger.utils.JudgeFileUtils;
import com.kevin.cpoj.domain.Submission;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//https://gist.github.com/harrymoore/8604212
@Service
public class JudgeWorker
{
    private static final Logger log = LoggerFactory.getLogger(JudgeWorker.class);

    private final JudgeConfig judgeConfig;
    private final JudgeDBService judgeDBService;
    private final ExecCmdService execCmdService;
    private final JudgeContextService judgeContextService;

    private final String DATA = "data";
    private final String JUDGE_SH_FILE = "callDocker.sh";

    @Autowired
    public JudgeWorker(final JudgeDBService judgeDBService, final ExecCmdService execCmdService, final JudgeContextService judgeContextService, final JudgeConfig judgeConfig)
    {
        this.judgeDBService = judgeDBService;
        this.execCmdService = execCmdService;
        this.judgeContextService = judgeContextService;
        this.judgeConfig = judgeConfig;
    }

    @Scheduled(fixedDelayString = "${application.codeJudger.scheduledDelay}")
    public void runCodeJudgerTask()
    {
        try
        {
            runJudge();
        }
        catch (final Exception e)
        {
            log(Level.ERROR, "Exception in running judge.", e);
            //ignore
        }
    }

    private long getWorkerId()
    {
        return Thread.currentThread().getId();
    }

    private String dos2unix(final String text)
    {
        return text.replaceAll("\r\n", "\n");
    }

    private void log(final Level logLevel, final String message)
    {
        log(logLevel, message, null);
    }

    private void log(final Level logLevel, final String message, final Throwable throwable)
    {
        long workerId = getWorkerId();
        final String msg = "[Worker-" + workerId + "] " + message;
        if (Level.DEBUG.equals(logLevel))
        {
            if (log.isDebugEnabled())
                log.debug(msg, throwable);
        }
        else if (Level.INFO.equals(logLevel))
        {
            log.info(msg, throwable);
        }
        else if (Level.WARN.equals(logLevel))
        {
            log.warn(msg, throwable);
        }
        else if (Level.ERROR.equals(logLevel))
        {
            log.error(msg, throwable);
        }
        else
            log.info(msg, throwable);
    }


    private void runJudge()
    {
        final JudgeContext judgeContext = new JudgeContext();
        try
        {
            final String threadName = Thread.currentThread().getName();

            // Step 1: Get and lock next submission job
            final Submission submission = judgeDBService.getAndLockNextSubmissionJob();

            // Step 2: No job, I am done.
            if (submission == null)
            {
                log(Level.DEBUG, "No submission job found. Do nothing.");
                return;
            }

            // Step 3: Sanity check submission
            judgeContextService.populateAndValidateJudgeContext(judgeContext, submission);

            // Step 4: Prepare judge data to run compile
            // If preparation failed, I can't run compile at all, call it done.
            prepareJudgeAndSubmissionData(judgeContext);

            // Step 5: Run compile
            final ExecCmdResult compileResult = runCompile(judgeContext);

            // Step 6: Process compile result
            processCompileResult(compileResult, judgeContext);

            if (judgeContext.isCompilationError())
            {
                processOverallJudgeResult(judgeContext);
                return;
            }

            // Step 7: Prepare execution input data
            // If preparation failed, I can't run exec at all, call it done.
            prepareJudgeInputData(judgeContext);

            // Run execute
            for (final TestCaseContext testCaseContext : judgeContext.getTestCaseContextList())
            {
                // Step 8: Run exec
                final ExecCmdResult executeResult = runExecute(testCaseContext, judgeContext);
                // Step 9: Process exec result
                processExecuteResult(executeResult, testCaseContext, judgeContext);
            }

            // Step 10: Process overall result
            processOverallJudgeResult(judgeContext);

            return;
        }
        //  * If invalid submission job data, mark submission Invalid
        //  * If hard failure, mark submission as Internal Error
        //  * If recoverable failure, requeue the job
        catch (final InvalidDataException e)
        {
            // Invalid submission, there is nothing I can do. Mark it invalid. Call it done.
            log(Level.ERROR, "InvalidDataException, mark submission invalid.", e);
            handleInvalidDataException(judgeContext, e);
            return;
        }
        catch (final ExceedMaxTriesException e)
        {
            log(Level.ERROR, "ExceedMaxTriesException, mark submission internal error.", e);
            handleExceedMaxTriesException(judgeContext);
            return;
        }
        catch (final MinorErrorException e)
        {
            log(Level.ERROR, "MinorErrorException, re-queue the submission job.", e);
            handleMinorException(judgeContext.getSubmissionId(), e.getMessage());
            return;
        }
        catch (final FatalErrorException e)
        {
            log(Level.ERROR, "FatalErrorException, mark submission internal error.", e);
            handleFatalException(judgeContext.getSubmissionId(), e.getMessage(), "MongoException in markSubmissionInvalid");
        }
        catch (final DataAccessException e)
        {
            log(Level.ERROR, "DataAccessException, re-queue the submission job.", e);
            handleMinorException(judgeContext.getSubmissionId(), e.getMessage());
            return;
        }
        catch (final Exception e)
        {
            log(Level.ERROR, "Generic Exception, mark submission internal error.", e);
            handleFatalException(judgeContext.getSubmissionId(), e.getMessage(), "MongoException in requeueSubmission");
            return;
        }
        finally
        {
            // Step 11: Cleanup
            cleanUpJudge(judgeContext);
        }
    }

    private void handleExceedMaxTriesException(final JudgeContext judgeContext)
    {
        try
        {
            judgeDBService.markSubmissionExceedMaxTries(judgeContext.getSubmissionId(), judgeContext.getTries());
        }
        catch (final DataAccessException exception)
        {
            log(Level.ERROR, "DataAccessException in handleExceedMaxTriesException", exception);
        }
    }

    private void handleInvalidDataException(final JudgeContext judgeContext, final InvalidDataException e)
    {
        try
        {
            judgeDBService.markSubmissionInvalid(judgeContext.getSubmissionId(), e.getMessage());
        }
        catch (final DataAccessException exception)
        {
            log(Level.ERROR, "DataAccessException in markSubmissionInvalid", exception);
        }
    }

    private void handleMinorException(final Long submissionId, final String message)
    {
        try
        {
            judgeDBService.requeueSubmission(submissionId, message);
        }
        catch (final DataAccessException exception)
        {
            log(Level.ERROR, "DataAccessException in requeueSubmission", exception);
        }
    }

    private void handleFatalException(final Long submissionId, final String message, final String message2)
    {
        try
        {
            judgeDBService.markSubmissionInternalError(submissionId, message);
        }
        catch (final DataAccessException exception)
        {
            log(Level.ERROR, message2, exception);
        }
        return;
    }

    /**
     * TODO
     * If hard failure, mark submission as Internal Error, return false
     * If recoverable failure, requeue the job, return false
     * Otherwise, return true
     *
     * @param compileResult
     * @param judgeContext
     */
    private void processCompileResult(final ExecCmdResult compileResult, final JudgeContext judgeContext)
    {
//    In COMPILE/EXECUTION mode:
//    #             -exit_code.txt  exit code
//    #             -log.txt        log messages about the judge
//    #             -compiled file, e.g., HelloWorld.class under /judge/data if in COMPILE mode
//    #             -stdout.txt     the stdout of the CODE_FILE ran againt INPUT_FILE
//    #             -stderr.txt     compile or runtime errors
//    #             -timeout.txt    if timeout, this will contain timeout information. Typically the code ran too long.
//    # If a file does not exit or is empty, it means the corresponding out/error did not happen.

        log(Level.INFO, "Process compile result for program: " + judgeContext.getProgramCode());
        if (compileResult != null)
        {
            log(Level.INFO, "Compile Result: " + compileResult);
        }
        final File logFile = new File(judgeContext.getJudgeDataFolderPath() + "/output/log.txt");
        if (JudgeFileUtils.checkFileExistsAndNonEmptyQuietly(logFile))
        {
            log(Level.INFO, "Log of compile step");
            try
            {
                final String logMsg = FileUtils.readFileToString(logFile, Charset.defaultCharset());
                log(Level.INFO, logMsg);
            }
            catch (final IOException e)
            {
                final String errMsg = "IOException when reading log.txt.";
                log(Level.ERROR, errMsg, e);
                throw new MinorErrorException(errMsg);
            }
        }
        else
        {
            final String errMsg = "No log.txt found, looks like the judge docker did not run properly";
            log(Level.ERROR, errMsg);
            throw new MinorErrorException(errMsg);
        }

        final File timeoutFile = new File(judgeContext.getJudgeDataFolderPath() + "/output/timeout.txt");
        if (JudgeFileUtils.checkFileExistsAndNonEmptyQuietly(timeoutFile))
        {
            final String errMsg = "Compile timeout." + judgeContext.getRawSubmission();
            log(Level.ERROR, errMsg);
            throw new MinorErrorException(errMsg);
        }

        final File stderrFile = new File(judgeContext.getJudgeDataFolderPath() + "/output/stderr.txt");
        if (JudgeFileUtils.checkFileExistsAndNonEmptyQuietly(stderrFile))
        {
            log(Level.INFO, "Compilation error found");
            try
            {
                final String stderr = FileUtils.readFileToString(stderrFile, Charset.defaultCharset());
                judgeContext.setCompilationError(true);
                judgeContext.setCompilationErrorMsg(stderr);
            }
            catch (final IOException e)
            {
                final String errMsg = "IOException when reading stderr.txt.";
                log(Level.ERROR, errMsg, e);
                throw new MinorErrorException(errMsg);
            }
        }
        else
        {
            judgeContext.setCompilationError(false);
            judgeContext.setCompilationErrorMsg(null);
        }
        // check the compiled file TODO
    }

    /**
     * TODO
     * If hard failure, mark submission as Internal Error, return false
     * If recoverable failure, requeue the job, return false
     * Otherwise, return true
     *
     * @param executeResult
     * @param testCaseContext
     * @param judgeContext
     */
    private void processExecuteResult(final ExecCmdResult executeResult, final TestCaseContext testCaseContext, final JudgeContext judgeContext)
    {
//    In COMPILE/EXECUTION mode:
//    #             -exit_code.txt  exit code
//    #             -log.txt        log messages about the judge
//    #             -comiled file, e.g., HelloWorld.class under /judge/data if in COMPILE mode
//    #             -stdout.txt     the stdout of the CODE_FILE ran againt INPUT_FILE
//    #             -stderr.txt     compile or runtime errors
//    #             -timeout.txt    if timeout, this will contain timeout information. Typically the code ran too long.
//    # If a file does not exit or is empty, it means the correponding out/error did not happen.
        log(Level.INFO, "Process execute result for test case: " + testCaseContext.getInputData());
        testCaseContext.setRuntimeResult(ERuntimeResult.NA);

        if (executeResult != null)
        {
            log(Level.INFO, "Execute Result: " + executeResult);
        }

        final File logFile = new File(judgeContext.getJudgeDataFolderPath() + "/output/log.txt");
        final boolean hasLogFile = JudgeFileUtils.checkFileExistsAndNonEmptyQuietly(logFile);

        final File stdoutFile = new File(judgeContext.getJudgeDataFolderPath() + "/output/stdout.txt");
        final boolean hasStdoutFile = JudgeFileUtils.checkFileExistsAndNonEmptyQuietly(stdoutFile);

        final File timeoutFile = new File(judgeContext.getJudgeDataFolderPath() + "/output/timeout.txt");
        final boolean hasTimeoutFile = JudgeFileUtils.checkFileExistsAndNonEmptyQuietly(timeoutFile);

        final File stderrFile = new File(judgeContext.getJudgeDataFolderPath() + "/output/stderr.txt");
        final boolean hasStderrFile = JudgeFileUtils.checkFileExistsAndNonEmptyQuietly(stderrFile);

        if (hasLogFile)
        {
            log(Level.INFO, "Log of execute step");
            try
            {
                final String logMsg = FileUtils.readFileToString(logFile, Charset.defaultCharset());
                log(Level.INFO, logMsg);
            }
            catch (final IOException e)
            {
                final String errMsg = "IOException when reading log.txt.";
                log(Level.ERROR, errMsg, e);
                throw new MinorErrorException(errMsg);
            }
        }
        else
        {
            final String errMsg = "No log.txt found, looks like the judge docker did not run properly";
            log(Level.ERROR, errMsg);
            throw new MinorErrorException(errMsg);
        }

        if (hasStdoutFile)
        {
            try
            {
                final String stdout = FileUtils.readFileToString(stdoutFile, Charset.defaultCharset());
                if (StringUtils.isNotBlank(stdout) && dos2unix(testCaseContext.getOutputData()).trim().equals(dos2unix(stdout).trim()))
                {
                    testCaseContext.setRuntimeResult(ERuntimeResult.PASS);

                    // As long as we pass, this is the best result
                    return;
                }
                else
                {
                    // We still try to check if time limit exceeded, or runtime error if found
                    testCaseContext.setRuntimeResult(ERuntimeResult.WRONG_ANSWER);
                }
            }
            catch (final IOException e)
            {
                final String errMsg = "IOException when reading stdout.txt.";
                log(Level.ERROR, errMsg, e);
                throw new MinorErrorException(errMsg);
            }
        }

        if (hasTimeoutFile)
        {
            final String errMsg = "Runtime Limit Exceed.";
            log(Level.INFO, errMsg);
            testCaseContext.setRuntimeResult(ERuntimeResult.TIME_LIMIT_EXCEEDED);
            return;
        }

        if (hasStderrFile)
        {
            log(Level.INFO, "Runtime error found");
            try
            {
                final String stderr = FileUtils.readFileToString(stderrFile, Charset.defaultCharset());
                testCaseContext.setRuntimeResult(ERuntimeResult.RUNTIME_ERROR);
                testCaseContext.setRuntimeResultMsg(stderr);
                return;
            }
            catch (final IOException e)
            {
                final String errMsg = "IOException when reading stderr.txt.";
                log(Level.ERROR, errMsg, e);
                throw new MinorErrorException(errMsg);
            }
        }

        // if no stdout and no stderr, we consider it as wrong answer
        if (!hasStdoutFile && !hasStderrFile)
        {
            testCaseContext.setRuntimeResult(ERuntimeResult.WRONG_ANSWER);
            return;
        }
    }

    /**
     * Run compile command
     *
     * @throws FatalErrorException
     */
    // Assume judge folder has callJudge.sh, data/Program file, now compile it
    private ExecCmdResult runCompile(final JudgeContext judgeContext)
    {
        try
        {
            final List<String> command = buildCompileCommand(judgeContext);

            final ExecCmdResult result = execCmdService.execCommand(command, judgeContext.getJudgeFolderPath());

            return result;
        }
        catch (final Exception e)
        {
            final String errMsg = "Failed to run compile step for submission: " + judgeContext.getRawSubmission();
            log(Level.ERROR, errMsg, e);
            throw new FatalErrorException(errMsg);
        }
    }

    /**
     * Run compile command
     *
     * @throws FatalErrorException
     */
    // Assume judge folder has callJudge.sh, data/compiled file, input file, now execute it
    private ExecCmdResult runExecute(final TestCaseContext testCaseContext, final JudgeContext judgeContext)
    {
        try
        {
            final List<String> command = buildExecuteCommand(judgeContext, testCaseContext);

            final ExecCmdResult result = execCmdService.execCommand(command, judgeContext.getJudgeFolderPath());

            return result;
        }
        catch (final Exception e)
        {
            final String errMsg = "Failed to run execute step for submission: " + judgeContext.getRawSubmission();
            log(Level.ERROR, errMsg, e);
            throw new FatalErrorException(errMsg);
        }
    }

    private List<String> buildCompileCommand(final JudgeContext judgeContext)
    {
        //"bash callDocker.sh data dockerid judge-compile java HelloJavaGood.java input1.txt 10"
        final List<String> commands = new ArrayList<>();
        commands.add("bash");
        commands.add(JUDGE_SH_FILE);
        commands.add(DATA);
        commands.add(judgeContext.getDockerContainerName());
        commands.add("judge-compile");
        commands.add(judgeContext.getLanguage().getLanguageCmd());
        commands.add(judgeContext.getProgramFileName());
        commands.add("noinput.txt");
        commands.add("10");
        return commands;
    }

    private List<String> buildExecuteCommand(final JudgeContext judgeContext, final TestCaseContext testCaseContext)
    {
        //"bash callDocker.sh data dockerid judge-execute java HelloJavaGood.class input1.txt 10"
        final List<String> commands = new ArrayList<>();
        commands.add("bash");
        commands.add(JUDGE_SH_FILE);
        commands.add(DATA);
        commands.add(judgeContext.getDockerContainerName());
        commands.add("judge-execute");
        commands.add(judgeContext.getLanguage().getLanguageCmd());
        commands.add(judgeContext.getCompiledFileName());
        commands.add(testCaseContext.getInputFileName());
        commands.add("" + (int) Math.ceil((double) testCaseContext.getTimeLimitInMS() / 1000));
        return commands;
    }

    /**
     * Prepare judge data: judge folder, judge sh, program code file
     */
    private void prepareJudgeAndSubmissionData(final JudgeContext judgeContext)
    {
        // Ensure judge folder exists, empty
        long workerId = getWorkerId();
        final String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss.SSS").format(new Date());
        final String dockerContainerId = judgeContext.getSubmissionId() + "-Worker-" + workerId + "-" + timeStamp;
        final String judgeFolderPath = judgeConfig.getJudgeWorkdir() + File.separator + dockerContainerId;
        final String judgeDataFolderPath = judgeFolderPath + File.separator + DATA;

        judgeContext.setDockerContainerName(dockerContainerId);
        judgeContext.setJudgeFolderPath(judgeFolderPath);
        judgeContext.setJudgeDataFolderPath(judgeDataFolderPath);

        final File judgeFolder = new File(judgeFolderPath);
        if (judgeFolder.exists())
        {
            log.warn("Folder: " + judgeFolderPath + " already exists. Clean it.");

            try
            {
                FileUtils.cleanDirectory(judgeFolder);
            }
            catch (final IOException e)
            {
                final String errMsg = "Failed to clean existing judge folder: " + judgeFolderPath;
                log(Level.ERROR, errMsg, e);
                throw new MinorErrorException(errMsg);
            }
        }

        try
        {
            FileUtils.forceMkdir(new File(judgeDataFolderPath));
        }
        catch (final IOException e)
        {
            final String errMsg = "Failed to create judge data folder: " + judgeDataFolderPath;
            log(Level.ERROR, errMsg, e);
            throw new MinorErrorException(errMsg);
        }

        // Copy runJudge.sh from resource to judge data folder
        try (final InputStream inputStream = getClass().getResourceAsStream(File.separator + JUDGE_SH_FILE))
        {
            if (inputStream == null)
            {
                final String errMsg = "Failed to load judge sh file from resource";
                log(Level.ERROR, errMsg);
                throw new FatalErrorException(errMsg);
            }
            final File targetFile = new File(judgeFolderPath + File.separator + JUDGE_SH_FILE);
            try
            {
                FileUtils.copyInputStreamToFile(inputStream, targetFile);
            }
            catch (final IOException e)
            {
                final String errMsg = "Failed to copy judge sh file to folder: " + judgeFolderPath;
                log(Level.ERROR, errMsg, e);
                throw new FatalErrorException(errMsg);
            }
        }
        catch (final IOException e)
        {
            final String errMsg = "Failed to load judge sh file from resource";
            log(Level.ERROR, errMsg);
            throw new FatalErrorException(errMsg);
        }

        // Copy submission program to judge data folder
        final String program = judgeContext.getProgramCode();
        final File programFile = new File(judgeDataFolderPath + File.separator + judgeContext.getProgramFileName());
        try
        {
            FileUtils.writeStringToFile(programFile, program, Charset.defaultCharset());
        }
        catch (final IOException e)
        {
            final String errMsg = "Failed to write submission program to file: " + programFile.getAbsolutePath();
            log(Level.ERROR, errMsg, e);
            throw new FatalErrorException(errMsg);
        }
    }

    /**
     * Prepare judge test case input data
     */
    private void prepareJudgeInputData(final JudgeContext judgeContext)
    {
        final String judgeDataFolderPath = judgeContext.getJudgeDataFolderPath();

        for (final TestCaseContext testCaseContext : judgeContext.getTestCaseContextList())
        {
            final File inputFile = new File(judgeDataFolderPath + File.separator + testCaseContext.getInputFileName());
            try
            {
                FileUtils.writeStringToFile(inputFile, testCaseContext.getInputData(), Charset.defaultCharset());
            }
            catch (final IOException e)
            {
                final String errMsg = "Failed to write test case input data to file: " + inputFile.getAbsolutePath();
                log(Level.ERROR, errMsg, e);
                throw new FatalErrorException(errMsg);
            }
        }
    }

    private void cleanUpJudge(final JudgeContext judgeContext)
    {
        log(Level.DEBUG, "Clean up judge data...");
        try
        {
            if (StringUtils.isNotBlank(judgeContext.getJudgeFolderPath()))
            {
                FileUtils.deleteQuietly(new File(judgeContext.getJudgeFolderPath()));
            }
        }
        catch (final Exception e)
        {
            log(Level.ERROR, "Exception in cleaning up judge data.", e);
            //ignore
        }
    }

    private Submission processOverallJudgeResult(final JudgeContext judgeContext)
    {
        log(Level.INFO, "Process Overall Judge Result...");

        if (judgeContext.isCompilationError())
        {
            return judgeDBService.updateSubmissionWithCompilationError(judgeContext.getSubmissionId(), judgeContext.getCompilationErrorMsg());
        }
        else
        {
            return judgeDBService.updateSubmissionWithRuntimeResult(judgeContext.getSubmissionId(), judgeContext);
        }
    }
}
