package com.kevin.cpoj.codejudger.exception;

public class InvalidDataException extends CodeJudgerException
{
  public InvalidDataException()
  {
    super();
  }

  public InvalidDataException(final String message)
  {
    super(message);
  }

  public InvalidDataException(final String message, final Throwable cause)
  {
    super(message, cause);
  }

}
