package com.kevin.cpoj.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.kevin.cpoj.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SubmissionTestCaseResultDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubmissionTestCaseResultDTO.class);
        SubmissionTestCaseResultDTO submissionTestCaseResultDTO1 = new SubmissionTestCaseResultDTO();
        submissionTestCaseResultDTO1.setId(1L);
        SubmissionTestCaseResultDTO submissionTestCaseResultDTO2 = new SubmissionTestCaseResultDTO();
        assertThat(submissionTestCaseResultDTO1).isNotEqualTo(submissionTestCaseResultDTO2);
        submissionTestCaseResultDTO2.setId(submissionTestCaseResultDTO1.getId());
        assertThat(submissionTestCaseResultDTO1).isEqualTo(submissionTestCaseResultDTO2);
        submissionTestCaseResultDTO2.setId(2L);
        assertThat(submissionTestCaseResultDTO1).isNotEqualTo(submissionTestCaseResultDTO2);
        submissionTestCaseResultDTO1.setId(null);
        assertThat(submissionTestCaseResultDTO1).isNotEqualTo(submissionTestCaseResultDTO2);
    }
}
