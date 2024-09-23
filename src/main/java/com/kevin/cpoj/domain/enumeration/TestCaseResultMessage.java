package com.kevin.cpoj.domain.enumeration;

import com.kevin.cpoj.codejudger.model.ERuntimeResult;

/**
 * The TestCaseResultMessage enumeration.
 */
public enum TestCaseResultMessage
{
    NA,
    PASS,
    WRONG_ANSWER,
    TLE,
    RUNTIME_ERROR;

    public static TestCaseResultMessage getTestCaseResultMessage(ERuntimeResult runtimeResult)
    {
        switch (runtimeResult)
        {
            case PASS:
                return TestCaseResultMessage.PASS;
            case NA:
                return TestCaseResultMessage.NA;
            case WRONG_ANSWER:
                return TestCaseResultMessage.WRONG_ANSWER;
            case TIME_LIMIT_EXCEEDED:
                return TestCaseResultMessage.TLE;
            case RUNTIME_ERROR:
                return TestCaseResultMessage.RUNTIME_ERROR;
            default:
                return NA;
        }
    }
}
