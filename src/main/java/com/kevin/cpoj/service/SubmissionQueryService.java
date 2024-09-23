package com.kevin.cpoj.service;

import com.kevin.cpoj.domain.*; // for static metamodels
import com.kevin.cpoj.domain.Submission;
import com.kevin.cpoj.repository.SubmissionRepository;
import com.kevin.cpoj.service.criteria.SubmissionCriteria;
import com.kevin.cpoj.service.dto.SubmissionDTO;
import com.kevin.cpoj.service.mapper.SubmissionMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Submission} entities in the database.
 * The main input is a {@link SubmissionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SubmissionDTO} or a {@link Page} of {@link SubmissionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SubmissionQueryService extends QueryService<Submission> {

    private final Logger log = LoggerFactory.getLogger(SubmissionQueryService.class);

    private final SubmissionRepository submissionRepository;

    private final SubmissionMapper submissionMapper;

    public SubmissionQueryService(SubmissionRepository submissionRepository, SubmissionMapper submissionMapper) {
        this.submissionRepository = submissionRepository;
        this.submissionMapper = submissionMapper;
    }

    /**
     * Return a {@link List} of {@link SubmissionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SubmissionDTO> findByCriteria(SubmissionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Submission> specification = createSpecification(criteria);
        return submissionMapper.toDto(submissionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SubmissionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SubmissionDTO> findByCriteria(SubmissionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Submission> specification = createSpecification(criteria);
        return submissionRepository.findAll(specification, page).map(submissionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SubmissionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Submission> specification = createSpecification(criteria);
        return submissionRepository.count(specification);
    }

    /**
     * Function to convert {@link SubmissionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Submission> createSpecification(SubmissionCriteria criteria) {
        Specification<Submission> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Submission_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Submission_.name));
            }
            if (criteria.getProgrammingLanguage() != null) {
                specification = specification.and(buildSpecification(criteria.getProgrammingLanguage(), Submission_.programmingLanguage));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), Submission_.code));
            }
            if (criteria.getOverallResultStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getOverallResultStatus(), Submission_.overallResultStatus));
            }
            if (criteria.getOverallResultMessage() != null) {
                specification = specification.and(buildSpecification(criteria.getOverallResultMessage(), Submission_.overallResultMessage));
            }
            if (criteria.getOverallResultMessageDetail() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getOverallResultMessageDetail(), Submission_.overallResultMessageDetail)
                    );
            }
            if (criteria.getOverallResultTries() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getOverallResultTries(), Submission_.overallResultTries));
            }
            if (criteria.getOverallResultScorePercentage() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getOverallResultScorePercentage(), Submission_.overallResultScorePercentage)
                    );
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), Submission_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), Submission_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), Submission_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), Submission_.lastModifiedDate));
            }
            if (criteria.getSubmissionTestCaseResultId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSubmissionTestCaseResultId(),
                            root -> root.join(Submission_.submissionTestCaseResults, JoinType.LEFT).get(SubmissionTestCaseResult_.id)
                        )
                    );
            }
            if (criteria.getProblemId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getProblemId(), root -> root.join(Submission_.problem, JoinType.LEFT).get(Problem_.id))
                    );
            }
        }
        return specification;
    }
}
