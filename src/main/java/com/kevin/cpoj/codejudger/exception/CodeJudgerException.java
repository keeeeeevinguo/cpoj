package com.kevin.cpoj.codejudger.exception;

public class CodeJudgerException extends RuntimeException
{
  public CodeJudgerException()
  {
    super();
  }

  public CodeJudgerException(final String message)
  {
    super(message);
  }

  public CodeJudgerException(final String message, final Throwable cause)
  {
    super(message, cause);
  }
}
