package com.kevin.cpoj.codejudger.exception;

public class FatalErrorException extends CodeJudgerException
{
  public FatalErrorException()
  {
    super();
  }

  public FatalErrorException(final String message)
  {
    super(message);
  }

  public FatalErrorException(final String message, final Throwable cause)
  {
    super(message, cause);
  }

}
