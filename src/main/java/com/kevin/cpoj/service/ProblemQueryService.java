package com.kevin.cpoj.service;

import com.kevin.cpoj.domain.*; // for static metamodels
import com.kevin.cpoj.domain.Problem;
import com.kevin.cpoj.repository.ProblemRepository;
import com.kevin.cpoj.service.criteria.ProblemCriteria;
import com.kevin.cpoj.service.dto.ProblemDTO;
import com.kevin.cpoj.service.mapper.ProblemMapper;
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
 * Service for executing complex queries for {@link Problem} entities in the database.
 * The main input is a {@link ProblemCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProblemDTO} or a {@link Page} of {@link ProblemDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProblemQueryService extends QueryService<Problem> {

    private final Logger log = LoggerFactory.getLogger(ProblemQueryService.class);

    private final ProblemRepository problemRepository;

    private final ProblemMapper problemMapper;

    public ProblemQueryService(ProblemRepository problemRepository, ProblemMapper problemMapper) {
        this.problemRepository = problemRepository;
        this.problemMapper = problemMapper;
    }

    /**
     * Return a {@link List} of {@link ProblemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProblemDTO> findByCriteria(ProblemCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Problem> specification = createSpecification(criteria);
        return problemMapper.toDto(problemRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ProblemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProblemDTO> findByCriteria(ProblemCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Problem> specification = createSpecification(criteria);
        return problemRepository.findAll(specification, page).map(problemMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProblemCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Problem> specification = createSpecification(criteria);
        return problemRepository.count(specification);
    }

    /**
     * Function to convert {@link ProblemCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Problem> createSpecification(ProblemCriteria criteria) {
        Specification<Problem> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Problem_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Problem_.name));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Problem_.title));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Problem_.description));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), Problem_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), Problem_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), Problem_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), Problem_.lastModifiedDate));
            }
            if (criteria.getTestCaseId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getTestCaseId(), root -> root.join(Problem_.testCases, JoinType.LEFT).get(TestCase_.id))
                    );
            }
        }
        return specification;
    }
}
