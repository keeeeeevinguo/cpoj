package com.kevin.cpoj.codejudger.model;

public class TestCaseContext
{
  private String inputFileName;
  private String inputData;
  private String outputFileName;
  private String outputData;
  private int timeLimitInMS;
  private int weightPercentage;

  private ERuntimeResult runtimeResult;
  private String runtimeResultMsg;

  public TestCaseContext(final String inputFileName, final String inputData, final String outputFileName, final String outputData, final int timeLimitInMS, final int weightPercentage)
  {
    this.inputFileName = inputFileName;
    this.inputData = inputData;
    this.outputFileName = outputFileName;
    this.outputData = outputData;
    this.timeLimitInMS = timeLimitInMS;
    this.weightPercentage = weightPercentage;
  }

  public String getRuntimeResultMsg()
  {
    return runtimeResultMsg;
  }

  public void setRuntimeResultMsg(final String runtimeResultMsg)
  {
    this.runtimeResultMsg = runtimeResultMsg;
  }

  public ERuntimeResult getRuntimeResult()
  {
    return runtimeResult;
  }

  public void setRuntimeResult(final ERuntimeResult runtimeResult)
  {
    this.runtimeResult = runtimeResult;
  }

  public int getTimeLimitInMS()
  {
    return timeLimitInMS;
  }

  public void setTimeLimitInMS(final int timeLimitInMS)
  {
    this.timeLimitInMS = timeLimitInMS;
  }

  public int getWeightPercentage()
  {
    return weightPercentage;
  }

  public void setWeightPercentage(final int weightPercentage)
  {
    this.weightPercentage = weightPercentage;
  }

  public String getOutputData()
  {
    return outputData;
  }

  public void setOutputData(final String outputData)
  {
    this.outputData = outputData;
  }

  public String getInputFileName()
  {
    return inputFileName;
  }

  public void setInputFileName(final String inputFileName)
  {
    this.inputFileName = inputFileName;
  }

  public String getInputData()
  {
    return inputData;
  }

  public void setInputData(final String inputData)
  {
    this.inputData = inputData;
  }

  public String getOutputFileName()
  {
    return outputFileName;
  }

  public void setOutputFileName(final String outputFileName)
  {
    this.outputFileName = outputFileName;
  }
}
