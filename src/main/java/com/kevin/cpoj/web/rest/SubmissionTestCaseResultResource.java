package com.kevin.cpoj.web.rest;

import com.kevin.cpoj.repository.SubmissionTestCaseResultRepository;
import com.kevin.cpoj.service.SubmissionTestCaseResultService;
import com.kevin.cpoj.service.dto.SubmissionTestCaseResultDTO;
import com.kevin.cpoj.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.kevin.cpoj.domain.SubmissionTestCaseResult}.
 */
@RestController
@RequestMapping("/api")
public class SubmissionTestCaseResultResource {

    private final Logger log = LoggerFactory.getLogger(SubmissionTestCaseResultResource.class);

    private static final String ENTITY_NAME = "submissionTestCaseResult";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SubmissionTestCaseResultService submissionTestCaseResultService;

    private final SubmissionTestCaseResultRepository submissionTestCaseResultRepository;

    public SubmissionTestCaseResultResource(
        SubmissionTestCaseResultService submissionTestCaseResultService,
        SubmissionTestCaseResultRepository submissionTestCaseResultRepository
    ) {
        this.submissionTestCaseResultService = submissionTestCaseResultService;
        this.submissionTestCaseResultRepository = submissionTestCaseResultRepository;
    }

    /**
     * {@code POST  /submission-test-case-results} : Create a new submissionTestCaseResult.
     *
     * @param submissionTestCaseResultDTO the submissionTestCaseResultDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new submissionTestCaseResultDTO, or with status {@code 400 (Bad Request)} if the submissionTestCaseResult has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/submission-test-case-results")
    public ResponseEntity<SubmissionTestCaseResultDTO> createSubmissionTestCaseResult(
        @Valid @RequestBody SubmissionTestCaseResultDTO submissionTestCaseResultDTO
    ) throws URISyntaxException {
        log.debug("REST request to save SubmissionTestCaseResult : {}", submissionTestCaseResultDTO);
        if (submissionTestCaseResultDTO.getId() != null) {
            throw new BadRequestAlertException("A new submissionTestCaseResult cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SubmissionTestCaseResultDTO result = submissionTestCaseResultService.save(submissionTestCaseResultDTO);
        return ResponseEntity
            .created(new URI("/api/submission-test-case-results/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /submission-test-case-results/:id} : Updates an existing submissionTestCaseResult.
     *
     * @param id the id of the submissionTestCaseResultDTO to save.
     * @param submissionTestCaseResultDTO the submissionTestCaseResultDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated submissionTestCaseResultDTO,
     * or with status {@code 400 (Bad Request)} if the submissionTestCaseResultDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the submissionTestCaseResultDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/submission-test-case-results/{id}")
    public ResponseEntity<SubmissionTestCaseResultDTO> updateSubmissionTestCaseResult(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SubmissionTestCaseResultDTO submissionTestCaseResultDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SubmissionTestCaseResult : {}, {}", id, submissionTestCaseResultDTO);
        if (submissionTestCaseResultDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, submissionTestCaseResultDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!submissionTestCaseResultRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SubmissionTestCaseResultDTO result = submissionTestCaseResultService.save(submissionTestCaseResultDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, submissionTestCaseResultDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /submission-test-case-results/:id} : Partial updates given fields of an existing submissionTestCaseResult, field will ignore if it is null
     *
     * @param id the id of the submissionTestCaseResultDTO to save.
     * @param submissionTestCaseResultDTO the submissionTestCaseResultDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated submissionTestCaseResultDTO,
     * or with status {@code 400 (Bad Request)} if the submissionTestCaseResultDTO is not valid,
     * or with status {@code 404 (Not Found)} if the submissionTestCaseResultDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the submissionTestCaseResultDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/submission-test-case-results/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SubmissionTestCaseResultDTO> partialUpdateSubmissionTestCaseResult(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SubmissionTestCaseResultDTO submissionTestCaseResultDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SubmissionTestCaseResult partially : {}, {}", id, submissionTestCaseResultDTO);
        if (submissionTestCaseResultDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, submissionTestCaseResultDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!submissionTestCaseResultRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SubmissionTestCaseResultDTO> result = submissionTestCaseResultService.partialUpdate(submissionTestCaseResultDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, submissionTestCaseResultDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /submission-test-case-results} : get all the submissionTestCaseResults.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of submissionTestCaseResults in body.
     */
    @GetMapping("/submission-test-case-results")
    public ResponseEntity<List<SubmissionTestCaseResultDTO>> getAllSubmissionTestCaseResults(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of SubmissionTestCaseResults");
        Page<SubmissionTestCaseResultDTO> page = submissionTestCaseResultService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /submission-test-case-results/:id} : get the "id" submissionTestCaseResult.
     *
     * @param id the id of the submissionTestCaseResultDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the submissionTestCaseResultDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/submission-test-case-results/{id}")
    public ResponseEntity<SubmissionTestCaseResultDTO> getSubmissionTestCaseResult(@PathVariable Long id) {
        log.debug("REST request to get SubmissionTestCaseResult : {}", id);
        Optional<SubmissionTestCaseResultDTO> submissionTestCaseResultDTO = submissionTestCaseResultService.findOne(id);
        return ResponseUtil.wrapOrNotFound(submissionTestCaseResultDTO);
    }

    /**
     * {@code DELETE  /submission-test-case-results/:id} : delete the "id" submissionTestCaseResult.
     *
     * @param id the id of the submissionTestCaseResultDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/submission-test-case-results/{id}")
    public ResponseEntity<Void> deleteSubmissionTestCaseResult(@PathVariable Long id) {
        log.debug("REST request to delete SubmissionTestCaseResult : {}", id);
        submissionTestCaseResultService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
