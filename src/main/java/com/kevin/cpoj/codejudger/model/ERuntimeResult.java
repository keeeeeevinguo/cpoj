package com.kevin.cpoj.codejudger.model;

public enum ERuntimeResult
{
  //  'N/A',
//      'Pass',
//      'Wrong Answer',
//      'Time Limit Exceeded',
//      'Memory Limit Exceeded',
//      'Runtime Error',
//      'Compile Error',
//      'Other'
  NA("N/A"),
  PASS("Pass"),
  WRONG_ANSWER("Wrong Answer"),
  TIME_LIMIT_EXCEEDED("Time Limit Exceed"),
  MEMORY_LIMIT_EXCEEDED("Memory Limit Exceeded"), //Not supported yet
  RUNTIME_ERROR("Runtime Error");

  private final String value;

  ERuntimeResult(final String value)
  {
    this.value = value;
  }

  public String getValue()
  {
    return value;
  }
}
