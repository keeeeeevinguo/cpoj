package com.kevin.cpoj.codejudger.exception;

public class MinorErrorException extends CodeJudgerException
{
  public MinorErrorException()
  {
    super();
  }

  public MinorErrorException(final String message)
  {
    super(message);
  }

  public MinorErrorException(final String message, final Throwable cause)
  {
    super(message, cause);
  }
}
