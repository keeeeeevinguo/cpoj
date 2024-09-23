package com.kevin.cpoj.codejudger.exception;

public class ExceedMaxTriesException extends CodeJudgerException
{
  public ExceedMaxTriesException()
  {
    super();
  }

  public ExceedMaxTriesException(final String message)
  {
    super(message);
  }

  public ExceedMaxTriesException(final String message, final Throwable cause)
  {
    super(message, cause);
  }
}
