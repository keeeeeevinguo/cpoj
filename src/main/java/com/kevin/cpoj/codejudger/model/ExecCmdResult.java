package com.kevin.cpoj.codejudger.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class ExecCmdResult
{
  private int exitCode;
  private String stdout;
  private String stderr;

  public ExecCmdResult(final int exitCode, final String stdout, final String stderr)
  {
    this.exitCode = exitCode;
    this.stdout = stdout;
    this.stderr = stderr;
  }

  public ExecCmdResult()
  {
    // Empty
  }

  public int getExitCode()
  {
    return this.exitCode;
  }

  public void setExitCode(final int exitCode)
  {
    this.exitCode = exitCode;
  }

  public String getStdout()
  {
    return this.stdout;
  }

  public void setStdout(final String stdout)
  {
    this.stdout = stdout;
  }

  public String getStderr()
  {
    return this.stderr;
  }

  public void setStderr(final String stderr)
  {
    this.stderr = stderr;
  }

  @Override
  public boolean equals(final Object obj)
  {
    if (obj == null)
    {
      return false;
    }
    if (obj == this)
    {
      return true;
    }
    if (obj.getClass() != getClass())
    {
      return false;
    }
    final ExecCmdResult rhs = (ExecCmdResult) obj;
    return new EqualsBuilder()
        .append(this.exitCode, rhs.exitCode)
        .append(this.stdout, rhs.stdout)
        .append(this.stderr, rhs.stderr)
        .isEquals();
  }

  @Override
  public int hashCode()
  {
    return new HashCodeBuilder()
        .append(this.exitCode)
        .append(this.stdout)
        .append(this.stderr)
        .toHashCode();
  }

  @Override
  public String toString()
  {
    return new ToStringBuilder(this)
        .append("exitCode", this.exitCode)
        .append("stdout", this.stdout)
        .append("stderr", this.stderr)
        .toString();
  }
}
