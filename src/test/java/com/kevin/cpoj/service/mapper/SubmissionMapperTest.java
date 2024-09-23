package com.kevin.cpoj.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SubmissionMapperTest {

    private SubmissionMapper submissionMapper;

    @BeforeEach
    public void setUp() {
        submissionMapper = new SubmissionMapperImpl();
    }
}
