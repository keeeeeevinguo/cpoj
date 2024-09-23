package com.kevin.cpoj.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SubmissionTestCaseResultMapperTest {

    private SubmissionTestCaseResultMapper submissionTestCaseResultMapper;

    @BeforeEach
    public void setUp() {
        submissionTestCaseResultMapper = new SubmissionTestCaseResultMapperImpl();
    }
}
