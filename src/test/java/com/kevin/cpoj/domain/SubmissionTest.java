package com.kevin.cpoj.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.kevin.cpoj.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SubmissionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Submission.class);
        Submission submission1 = new Submission();
        submission1.setId(1L);
        Submission submission2 = new Submission();
        submission2.setId(submission1.getId());
        assertThat(submission1).isEqualTo(submission2);
        submission2.setId(2L);
        assertThat(submission1).isNotEqualTo(submission2);
        submission1.setId(null);
        assertThat(submission1).isNotEqualTo(submission2);
    }
}
