package com.kevin.cpoj.codejudger.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Service

public class JudgeConfig
{
    private final static Logger log = LoggerFactory.getLogger(JudgeConfig.class);

    @Value("${application.codeJudger.workdir}")
    private String workdir;

    @Value("${application.codeJudger.maxTries}")
    private int maxTries;

    @Value("${application.codeJudger.expiredSeconds}")
    private int expiredSeconds;

    public String getJudgeWorkdir()
    {
        if (workdir != null) return workdir;

        final File jarPath = new File(JudgeConfig.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        return jarPath.getParent();
    }

    public int getJudgeMaxTries()
    {
        return maxTries;
    }

    public int getJudgeExpiredSeconds()
    {
        return expiredSeconds;
    }
}
