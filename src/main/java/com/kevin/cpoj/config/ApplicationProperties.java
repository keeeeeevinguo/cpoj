package com.kevin.cpoj.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Cpoj.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link tech.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties
{
    private CodeJudgerConfig codeJudger;

    public CodeJudgerConfig getCodeJudger()
    {
        return codeJudger;
    }

    public void setCodeJudger(CodeJudgerConfig codeJudger)
    {
        this.codeJudger = codeJudger;
    }

    public static class CodeJudgerConfig
    {
        private String workdir;

        private int maxTries;

        private int expiredSeconds;

        private int scheduledDelay;

        public String getWorkdir()
        {
            return workdir;
        }

        public void setWorkdir(String workdir)
        {
            this.workdir = workdir;
        }

        public int getMaxTries()
        {
            return maxTries;
        }

        public void setMaxTries(int maxTries)
        {
            this.maxTries = maxTries;
        }

        public int getExpiredSeconds()
        {
            return expiredSeconds;
        }

        public void setExpiredSeconds(int expiredSeconds)
        {
            this.expiredSeconds = expiredSeconds;
        }

        public int getScheduledDelay()
        {
            return scheduledDelay;
        }

        public void setScheduledDelay(int scheduledDelay)
        {
            this.scheduledDelay = scheduledDelay;
        }
    }
}
