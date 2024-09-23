package com.kevin.cpoj.codejudger.service;

import com.kevin.cpoj.codejudger.model.ExecCmdResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExecCmdService
{
  private final static Logger log = LoggerFactory.getLogger(ExecCmdService.class);

//  public ExecCmdResult execCommand(final String[] command, final String[] options)
//  {
//    try
//    {
//      final Runtime runtime = Runtime.getRuntime();
//      final Process cmdProc = runtime.exec(command);
//
//      final BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(cmdProc.getInputStream()));
//      final StringBuilder stdout = new StringBuilder();
//      String line;
//      while ((line = stdoutReader.readLine()) != null)
//      {
//        stdout.append(line);
//        stdout.append()
//      }
//      stdoutReader.close();
//      final BufferedReader stderrReader = new BufferedReader(new InputStreamReader(cmdProc.getErrorStream()));
//      while ((line = stderrReader.readLine()) != null)
//      {
//        // process procs standard error here
//      }
//      stderrReader.close();
//      final int exitValue = cmdProc.waitFor();
//    }
//    catch (final IOException e)
//    {
//      e.printStackTrace();
//    }
//  }

  public ExecCmdResult execCommand(final List<String> command, final String workDirectory)
  {
    log.info("Run command: " + command);

//    List<String> command = new ArrayList<String>();
//    command.add(System.getenv("windir") +"\\system32\\"+"tree.com");
//    command.add("/A");

    final List<String> test = new ArrayList<>();
    test.add("bash");
    test.add("callDocker.sh");
    test.add("ttt");
    final ProcessBuilder builder = new ProcessBuilder(command);
    //http://stackoverflow.com/questions/5711084/java-runtime-getruntime-getting-output-from-executing-a-command-line-program

//    final Map<String, String> env = builder.environment();
//    // If you want clean environment, call env.clear() first
//    // env.clear()
//    env.put("VAR1", "myValue");
//    env.remove("OTHERVAR");
//    env.put("VAR2", env.get("VAR1") + "suffix");

    // merges standard error and standard output
    // builder.redirectErrorStream();

    builder.directory(new File(workDirectory));

    //System.out.println("Directory : " + System.getenv("temp"));

    Process process = null;
    try
    {
      process = builder.start();

      final BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
      final StringBuilder stdout = new StringBuilder();
      String line = null;
      try
      {
        while ((line = stdoutReader.readLine()) != null)
        {
          stdout.append(line).append("\n");
        }
      }
      catch (final IOException e)
      {
        // This is acceptable
        log.error("IOException when getting stdout.", e);
      }
      finally
      {
        try
        {
          stdoutReader.close();
        }
        catch (final IOException e)
        {
          // This is acceptable
          log.error("IOException when closing stdout reader.", e);
        }
      }

      final BufferedReader stderrReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
      final StringBuilder stderr = new StringBuilder();
      try
      {
        while ((line = stderrReader.readLine()) != null)
        {
          stderr.append(line).append("\n");
        }
      }
      catch (final IOException e)
      {
        // This is acceptable
        log.error("IOException when getting stderr.", e);
      }
      finally
      {
        try
        {
          stderrReader.close();
        }
        catch (final IOException e)
        {
          // This is acceptable
          log.error("IOException when closing stderr reader.", e);
        }
      }

      int exitValue = -1;
      try
      {
        exitValue = process.waitFor();
      }
      catch (final InterruptedException e)
      {
        // This is acceptable
        log.error("InterruptedException when waiting command to finish - " + command, e);
      }
      return new ExecCmdResult(exitValue, stdout.toString(), stderr.toString());
    }

    catch (final IOException e)
    {
      // This is acceptable
      log.error("IOException when running command - " + command, e);
    }
    finally
    {
      if (process != null)
      {
        process.destroy();
      }
    }
    return null;
  }
}
