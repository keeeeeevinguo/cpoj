package com.kevin.cpoj.codejudger.model;

import com.kevin.cpoj.domain.Problem;
import com.kevin.cpoj.domain.Submission;

import java.util.ArrayList;
import java.util.List;

public class JudgeContext
{
  private Submission rawSubmission;
  private Problem rawQuestion;
  private boolean compilationError;
  private String compilationErrorMsg;
  private Long submissionId;
  private Long questionId;
  private String judgeFolderPath;
  private String judgeDataFolderPath;
  private String dockerContainerName;
  private ELanguage language;
  private String programCode;
  private String programFileName;
  private String compiledFileName;
  private List<TestCaseContext> testCaseContextList = new ArrayList<>();
  private int tries;

  public String getDockerContainerName()
  {
    return dockerContainerName;
  }

  public void setDockerContainerName(final String dockerContainerName)
  {
    this.dockerContainerName = dockerContainerName;
  }

  public String getCompiledFileName()
  {
    return compiledFileName;
  }

  public void setCompiledFileName(final String compiledFileName)
  {
    this.compiledFileName = compiledFileName;
  }

  public List<TestCaseContext> getTestCaseContextList()
  {
    return testCaseContextList;
  }

  public void setTestCaseContextList(final List<TestCaseContext> testCaseContextList)
  {
    this.testCaseContextList = testCaseContextList;
  }

  public String getProgramCode()
  {
    return programCode;
  }

  public void setProgramCode(final String programCode)
  {
    this.programCode = programCode;
  }

  public Long getSubmissionId()
  {
    return submissionId;
  }

  public void setSubmissionId(final Long submissionId)
  {
    this.submissionId = submissionId;
  }

  public Long getQuestionId()
  {
    return questionId;
  }

  public void setQuestionId(final Long questionId)
  {
    this.questionId = questionId;
  }

  public String getJudgeFolderPath()
  {
    return judgeFolderPath;
  }

  public void setJudgeFolderPath(final String judgeFolderPath)
  {
    this.judgeFolderPath = judgeFolderPath;
  }

  public String getJudgeDataFolderPath()
  {
    return judgeDataFolderPath;
  }

  public void setJudgeDataFolderPath(final String judgeDataFolderPath)
  {
    this.judgeDataFolderPath = judgeDataFolderPath;
  }

  public ELanguage getLanguage()
  {
    return language;
  }

  public void setLanguage(final ELanguage language)
  {
    this.language = language;
  }

  public String getProgramFileName()
  {
    return programFileName;
  }

  public void setProgramFileName(final String programFileName)
  {
    this.programFileName = programFileName;
  }

  public Submission getRawSubmission()
  {
    return rawSubmission;
  }

  public void setRawSubmission(final Submission rawSubmission)
  {
    this.rawSubmission = rawSubmission;
  }

  public Problem getRawQuestion()
  {
    return rawQuestion;
  }

  public void setRawQuestion(final Problem rawQuestion)
  {
    this.rawQuestion = rawQuestion;
  }

  public boolean isCompilationError()
  {
    return compilationError;
  }

  public void setCompilationError(final boolean compilationError)
  {
    this.compilationError = compilationError;
  }

  public String getCompilationErrorMsg()
  {
    return compilationErrorMsg;
  }

  public void setCompilationErrorMsg(final String compilationErrorMsg)
  {
    this.compilationErrorMsg = compilationErrorMsg;
  }

  public int getTries()
  {
    return tries;

  }

  public void setTries(final int tries)
  {
    this.tries = tries;
  }
}
