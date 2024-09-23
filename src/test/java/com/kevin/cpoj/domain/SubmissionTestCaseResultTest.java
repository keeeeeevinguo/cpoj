package com.kevin.cpoj.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.kevin.cpoj.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SubmissionTestCaseResultTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubmissionTestCaseResult.class);
        SubmissionTestCaseResult submissionTestCaseResult1 = new SubmissionTestCaseResult();
        submissionTestCaseResult1.setId(1L);
        SubmissionTestCaseResult submissionTestCaseResult2 = new SubmissionTestCaseResult();
        submissionTestCaseResult2.setId(submissionTestCaseResult1.getId());
        assertThat(submissionTestCaseResult1).isEqualTo(submissionTestCaseResult2);
        submissionTestCaseResult2.setId(2L);
        assertThat(submissionTestCaseResult1).isNotEqualTo(submissionTestCaseResult2);
        submissionTestCaseResult1.setId(null);
        assertThat(submissionTestCaseResult1).isNotEqualTo(submissionTestCaseResult2);
    }
}
