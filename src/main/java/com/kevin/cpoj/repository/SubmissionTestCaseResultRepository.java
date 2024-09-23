package com.kevin.cpoj.repository;

import com.kevin.cpoj.domain.SubmissionTestCaseResult;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SubmissionTestCaseResult entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubmissionTestCaseResultRepository extends JpaRepository<SubmissionTestCaseResult, Long> {}
