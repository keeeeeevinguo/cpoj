package com.kevin.cpoj.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestCaseMapperTest {

    private TestCaseMapper testCaseMapper;

    @BeforeEach
    public void setUp() {
        testCaseMapper = new TestCaseMapperImpl();
    }
}
