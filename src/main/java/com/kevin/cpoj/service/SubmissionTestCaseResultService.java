package com.kevin.cpoj.service;

import com.kevin.cpoj.domain.SubmissionTestCaseResult;
import com.kevin.cpoj.repository.SubmissionTestCaseResultRepository;
import com.kevin.cpoj.service.dto.SubmissionTestCaseResultDTO;
import com.kevin.cpoj.service.mapper.SubmissionTestCaseResultMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SubmissionTestCaseResult}.
 */
@Service
@Transactional
public class SubmissionTestCaseResultService {

    private final Logger log = LoggerFactory.getLogger(SubmissionTestCaseResultService.class);

    private final SubmissionTestCaseResultRepository submissionTestCaseResultRepository;

    private final SubmissionTestCaseResultMapper submissionTestCaseResultMapper;

    public SubmissionTestCaseResultService(
        SubmissionTestCaseResultRepository submissionTestCaseResultRepository,
        SubmissionTestCaseResultMapper submissionTestCaseResultMapper
    ) {
        this.submissionTestCaseResultRepository = submissionTestCaseResultRepository;
        this.submissionTestCaseResultMapper = submissionTestCaseResultMapper;
    }

    /**
     * Save a submissionTestCaseResult.
     *
     * @param submissionTestCaseResultDTO the entity to save.
     * @return the persisted entity.
     */
    public SubmissionTestCaseResultDTO save(SubmissionTestCaseResultDTO submissionTestCaseResultDTO) {
        log.debug("Request to save SubmissionTestCaseResult : {}", submissionTestCaseResultDTO);
        SubmissionTestCaseResult submissionTestCaseResult = submissionTestCaseResultMapper.toEntity(submissionTestCaseResultDTO);
        submissionTestCaseResult = submissionTestCaseResultRepository.save(submissionTestCaseResult);
        return submissionTestCaseResultMapper.toDto(submissionTestCaseResult);
    }

    /**
     * Partially update a submissionTestCaseResult.
     *
     * @param submissionTestCaseResultDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SubmissionTestCaseResultDTO> partialUpdate(SubmissionTestCaseResultDTO submissionTestCaseResultDTO) {
        log.debug("Request to partially update SubmissionTestCaseResult : {}", submissionTestCaseResultDTO);

        return submissionTestCaseResultRepository
            .findById(submissionTestCaseResultDTO.getId())
            .map(existingSubmissionTestCaseResult -> {
                submissionTestCaseResultMapper.partialUpdate(existingSubmissionTestCaseResult, submissionTestCaseResultDTO);

                return existingSubmissionTestCaseResult;
            })
            .map(submissionTestCaseResultRepository::save)
            .map(submissionTestCaseResultMapper::toDto);
    }

    /**
     * Get all the submissionTestCaseResults.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SubmissionTestCaseResultDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SubmissionTestCaseResults");
        return submissionTestCaseResultRepository.findAll(pageable).map(submissionTestCaseResultMapper::toDto);
    }

    /**
     * Get one submissionTestCaseResult by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SubmissionTestCaseResultDTO> findOne(Long id) {
        log.debug("Request to get SubmissionTestCaseResult : {}", id);
        return submissionTestCaseResultRepository.findById(id).map(submissionTestCaseResultMapper::toDto);
    }

    /**
     * Delete the submissionTestCaseResult by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SubmissionTestCaseResult : {}", id);
        submissionTestCaseResultRepository.deleteById(id);
    }
}
