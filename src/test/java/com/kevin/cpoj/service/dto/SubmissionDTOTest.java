package com.kevin.cpoj.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.kevin.cpoj.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SubmissionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubmissionDTO.class);
        SubmissionDTO submissionDTO1 = new SubmissionDTO();
        submissionDTO1.setId(1L);
        SubmissionDTO submissionDTO2 = new SubmissionDTO();
        assertThat(submissionDTO1).isNotEqualTo(submissionDTO2);
        submissionDTO2.setId(submissionDTO1.getId());
        assertThat(submissionDTO1).isEqualTo(submissionDTO2);
        submissionDTO2.setId(2L);
        assertThat(submissionDTO1).isNotEqualTo(submissionDTO2);
        submissionDTO1.setId(null);
        assertThat(submissionDTO1).isNotEqualTo(submissionDTO2);
    }
}
