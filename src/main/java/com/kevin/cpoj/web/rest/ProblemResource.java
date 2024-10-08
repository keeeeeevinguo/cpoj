package com.kevin.cpoj.web.rest;

import com.kevin.cpoj.repository.ProblemRepository;
import com.kevin.cpoj.service.ProblemQueryService;
import com.kevin.cpoj.service.ProblemService;
import com.kevin.cpoj.service.criteria.ProblemCriteria;
import com.kevin.cpoj.service.dto.ProblemDTO;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.kevin.cpoj.domain.Problem}.
 */
@RestController
@RequestMapping("/api")
public class ProblemResource {

    private final Logger log = LoggerFactory.getLogger(ProblemResource.class);

    private static final String ENTITY_NAME = "problem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProblemService problemService;

    private final ProblemRepository problemRepository;

    private final ProblemQueryService problemQueryService;

    public ProblemResource(ProblemService problemService, ProblemRepository problemRepository, ProblemQueryService problemQueryService) {
        this.problemService = problemService;
        this.problemRepository = problemRepository;
        this.problemQueryService = problemQueryService;
    }

    /**
     * {@code POST  /problems} : Create a new problem.
     *
     * @param problemDTO the problemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new problemDTO, or with status {@code 400 (Bad Request)} if the problem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/problems")
    public ResponseEntity<ProblemDTO> createProblem(@Valid @RequestBody ProblemDTO problemDTO) throws URISyntaxException {
        log.debug("REST request to save Problem : {}", problemDTO);
        if (problemDTO.getId() != null) {
            throw new BadRequestAlertException("A new problem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProblemDTO result = problemService.save(problemDTO);
        return ResponseEntity
            .created(new URI("/api/problems/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /problems/:id} : Updates an existing problem.
     *
     * @param id the id of the problemDTO to save.
     * @param problemDTO the problemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated problemDTO,
     * or with status {@code 400 (Bad Request)} if the problemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the problemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/problems/{id}")
    public ResponseEntity<ProblemDTO> updateProblem(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProblemDTO problemDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Problem : {}, {}", id, problemDTO);
        if (problemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, problemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!problemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProblemDTO result = problemService.save(problemDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, problemDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /problems/:id} : Partial updates given fields of an existing problem, field will ignore if it is null
     *
     * @param id the id of the problemDTO to save.
     * @param problemDTO the problemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated problemDTO,
     * or with status {@code 400 (Bad Request)} if the problemDTO is not valid,
     * or with status {@code 404 (Not Found)} if the problemDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the problemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/problems/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProblemDTO> partialUpdateProblem(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProblemDTO problemDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Problem partially : {}, {}", id, problemDTO);
        if (problemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, problemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!problemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProblemDTO> result = problemService.partialUpdate(problemDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, problemDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /problems} : get all the problems.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of problems in body.
     */
    @GetMapping("/problems")
    public ResponseEntity<List<ProblemDTO>> getAllProblems(
        ProblemCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Problems by criteria: {}", criteria);
        Page<ProblemDTO> page = problemQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /problems/count} : count all the problems.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/problems/count")
    public ResponseEntity<Long> countProblems(ProblemCriteria criteria) {
        log.debug("REST request to count Problems by criteria: {}", criteria);
        return ResponseEntity.ok().body(problemQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /problems/:id} : get the "id" problem.
     *
     * @param id the id of the problemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the problemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/problems/{id}")
    public ResponseEntity<ProblemDTO> getProblem(@PathVariable Long id) {
        log.debug("REST request to get Problem : {}", id);
        Optional<ProblemDTO> problemDTO = problemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(problemDTO);
    }

    /**
     * {@code DELETE  /problems/:id} : delete the "id" problem.
     *
     * @param id the id of the problemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/problems/{id}")
    public ResponseEntity<Void> deleteProblem(@PathVariable Long id) {
        log.debug("REST request to delete Problem : {}", id);
        problemService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
