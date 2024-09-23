package com.kevin.cpoj.codejudger.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class JudgeFileUtils
{
  private final static Logger log = LoggerFactory.getLogger(JudgeFileUtils.class);

  public static String createJudgeFolder()
  {
    final File rootDir = File.listRoots()[0];
    final File dir = new File(new File(new File(new File(rootDir, "Users"), "taylor"), "codejudger"), "judgedata");
    if (!dir.exists())
    {
      dir.mkdirs();
    }
    return dir.getAbsolutePath();
  }

  public static boolean checkFileExistsAndNonEmptyQuietly(final File file)
  {
    if (file == null || !file.exists() || !file.isFile())
    {
      return false;
    }

    FileReader fr = null;
    try
    {
      fr = new FileReader(file);
      if (fr.read() == -1)
      {
        //Empty file
        return false;
      }
      return true;
    }
    catch (final IOException e)
    {
      return false;
    }
    finally
    {
      if (fr != null)
      {
        try
        {
          fr.close();
        }
        catch (final IOException e)
        {
          return false;
        }
      }
    }
  }

  public static boolean isFileExistsAndNonEmpty(final File file)
  {
    if (file == null)
    {
      return false;
    }
    if (!file.exists())
    {
      log.debug("File does not exist: " + file.getAbsolutePath());
      return false;
    }
    if (!file.isFile())
    {
      log.debug("The given file directory is not a file: " + file.getAbsolutePath());
      return false;
    }

    FileReader fr = null;
    try
    {
      fr = new FileReader(file);
      if (fr.read() == -1)
      {
        //Empty file
        return false;
      }
      return true;
    }
    catch (final IOException e)
    {
      log.warn("Could not read file: " + file.getAbsolutePath(), e);
      return false;
    }
    finally
    {
      if (fr != null)
      {
        try
        {
          fr.close();
        }
        catch (final IOException e)
        {
          log.warn("Could not close file: " + file.getAbsolutePath(), e);
          return false;
        }
      }
    }
  }
}
