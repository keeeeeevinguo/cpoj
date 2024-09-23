package com.kevin.cpoj.service;

import com.kevin.cpoj.domain.TestCase;
import com.kevin.cpoj.repository.TestCaseRepository;
import com.kevin.cpoj.service.dto.TestCaseDTO;
import com.kevin.cpoj.service.mapper.TestCaseMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TestCase}.
 */
@Service
@Transactional
public class TestCaseService {

    private final Logger log = LoggerFactory.getLogger(TestCaseService.class);

    private final TestCaseRepository testCaseRepository;

    private final TestCaseMapper testCaseMapper;

    public TestCaseService(TestCaseRepository testCaseRepository, TestCaseMapper testCaseMapper) {
        this.testCaseRepository = testCaseRepository;
        this.testCaseMapper = testCaseMapper;
    }

    /**
     * Save a testCase.
     *
     * @param testCaseDTO the entity to save.
     * @return the persisted entity.
     */
    public TestCaseDTO save(TestCaseDTO testCaseDTO) {
        log.debug("Request to save TestCase : {}", testCaseDTO);
        TestCase testCase = testCaseMapper.toEntity(testCaseDTO);
        testCase = testCaseRepository.save(testCase);
        return testCaseMapper.toDto(testCase);
    }

    /**
     * Partially update a testCase.
     *
     * @param testCaseDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TestCaseDTO> partialUpdate(TestCaseDTO testCaseDTO) {
        log.debug("Request to partially update TestCase : {}", testCaseDTO);

        return testCaseRepository
            .findById(testCaseDTO.getId())
            .map(existingTestCase -> {
                testCaseMapper.partialUpdate(existingTestCase, testCaseDTO);

                return existingTestCase;
            })
            .map(testCaseRepository::save)
            .map(testCaseMapper::toDto);
    }

    /**
     * Get all the testCases.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TestCaseDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TestCases");
        return testCaseRepository.findAll(pageable).map(testCaseMapper::toDto);
    }

    /**
     * Get one testCase by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TestCaseDTO> findOne(Long id) {
        log.debug("Request to get TestCase : {}", id);
        return testCaseRepository.findById(id).map(testCaseMapper::toDto);
    }

    /**
     * Delete the testCase by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TestCase : {}", id);
        testCaseRepository.deleteById(id);
    }
}
