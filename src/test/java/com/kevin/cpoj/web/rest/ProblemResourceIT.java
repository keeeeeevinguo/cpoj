package com.kevin.cpoj.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.kevin.cpoj.IntegrationTest;
import com.kevin.cpoj.domain.Problem;
import com.kevin.cpoj.domain.TestCase;
import com.kevin.cpoj.repository.ProblemRepository;
import com.kevin.cpoj.service.criteria.ProblemCriteria;
import com.kevin.cpoj.service.dto.ProblemDTO;
import com.kevin.cpoj.service.mapper.ProblemMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ProblemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProblemResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/problems";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProblemRepository problemRepository;

    @Autowired
    private ProblemMapper problemMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProblemMockMvc;

    private Problem problem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Problem createEntity(EntityManager em) {
        Problem problem = new Problem()
            .name(DEFAULT_NAME)
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return problem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Problem createUpdatedEntity(EntityManager em) {
        Problem problem = new Problem()
            .name(UPDATED_NAME)
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return problem;
    }

    @BeforeEach
    public void initTest() {
        problem = createEntity(em);
    }

    @Test
    @Transactional
    void createProblem() throws Exception {
        int databaseSizeBeforeCreate = problemRepository.findAll().size();
        // Create the Problem
        ProblemDTO problemDTO = problemMapper.toDto(problem);
        restProblemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(problemDTO)))
            .andExpect(status().isCreated());

        // Validate the Problem in the database
        List<Problem> problemList = problemRepository.findAll();
        assertThat(problemList).hasSize(databaseSizeBeforeCreate + 1);
        Problem testProblem = problemList.get(problemList.size() - 1);
        assertThat(testProblem.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProblem.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testProblem.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProblem.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testProblem.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testProblem.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testProblem.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void createProblemWithExistingId() throws Exception {
        // Create the Problem with an existing ID
        problem.setId(1L);
        ProblemDTO problemDTO = problemMapper.toDto(problem);

        int databaseSizeBeforeCreate = problemRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProblemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(problemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Problem in the database
        List<Problem> problemList = problemRepository.findAll();
        assertThat(problemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = problemRepository.findAll().size();
        // set the field null
        problem.setName(null);

        // Create the Problem, which fails.
        ProblemDTO problemDTO = problemMapper.toDto(problem);

        restProblemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(problemDTO)))
            .andExpect(status().isBadRequest());

        List<Problem> problemList = problemRepository.findAll();
        assertThat(problemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = problemRepository.findAll().size();
        // set the field null
        problem.setTitle(null);

        // Create the Problem, which fails.
        ProblemDTO problemDTO = problemMapper.toDto(problem);

        restProblemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(problemDTO)))
            .andExpect(status().isBadRequest());

        List<Problem> problemList = problemRepository.findAll();
        assertThat(problemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = problemRepository.findAll().size();
        // set the field null
        problem.setDescription(null);

        // Create the Problem, which fails.
        ProblemDTO problemDTO = problemMapper.toDto(problem);

        restProblemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(problemDTO)))
            .andExpect(status().isBadRequest());

        List<Problem> problemList = problemRepository.findAll();
        assertThat(problemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedByIsRequired() throws Exception {
        int databaseSizeBeforeTest = problemRepository.findAll().size();
        // set the field null
        problem.setCreatedBy(null);

        // Create the Problem, which fails.
        ProblemDTO problemDTO = problemMapper.toDto(problem);

        restProblemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(problemDTO)))
            .andExpect(status().isBadRequest());

        List<Problem> problemList = problemRepository.findAll();
        assertThat(problemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastModifiedByIsRequired() throws Exception {
        int databaseSizeBeforeTest = problemRepository.findAll().size();
        // set the field null
        problem.setLastModifiedBy(null);

        // Create the Problem, which fails.
        ProblemDTO problemDTO = problemMapper.toDto(problem);

        restProblemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(problemDTO)))
            .andExpect(status().isBadRequest());

        List<Problem> problemList = problemRepository.findAll();
        assertThat(problemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProblems() throws Exception {
        // Initialize the database
        problemRepository.saveAndFlush(problem);

        // Get all the problemList
        restProblemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(problem.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getProblem() throws Exception {
        // Initialize the database
        problemRepository.saveAndFlush(problem);

        // Get the problem
        restProblemMockMvc
            .perform(get(ENTITY_API_URL_ID, problem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(problem.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getProblemsByIdFiltering() throws Exception {
        // Initialize the database
        problemRepository.saveAndFlush(problem);

        Long id = problem.getId();

        defaultProblemShouldBeFound("id.equals=" + id);
        defaultProblemShouldNotBeFound("id.notEquals=" + id);

        defaultProblemShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProblemShouldNotBeFound("id.greaterThan=" + id);

        defaultProblemShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProblemShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProblemsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        problemRepository.saveAndFlush(problem);

        // Get all the problemList where name equals to DEFAULT_NAME
        defaultProblemShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the problemList where name equals to UPDATED_NAME
        defaultProblemShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProblemsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        problemRepository.saveAndFlush(problem);

        // Get all the problemList where name not equals to DEFAULT_NAME
        defaultProblemShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the problemList where name not equals to UPDATED_NAME
        defaultProblemShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProblemsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        problemRepository.saveAndFlush(problem);

        // Get all the problemList where name in DEFAULT_NAME or UPDATED_NAME
        defaultProblemShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the problemList where name equals to UPDATED_NAME
        defaultProblemShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProblemsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        problemRepository.saveAndFlush(problem);

        // Get all the problemList where name is not null
        defaultProblemShouldBeFound("name.specified=true");

        // Get all the problemList where name is null
        defaultProblemShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllProblemsByNameContainsSomething() throws Exception {
        // Initialize the database
        problemRepository.saveAndFlush(problem);

        // Get all the problemList where name contains DEFAULT_NAME
        defaultProblemShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the problemList where name contains UPDATED_NAME
        defaultProblemShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProblemsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        problemRepository.saveAndFlush(problem);

        // Get all the problemList where name does not contain DEFAULT_NAME
        defaultProblemShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the problemList where name does not contain UPDATED_NAME
        defaultProblemShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProblemsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        problemRepository.saveAndFlush(problem);

        // Get all the problemList where title equals to DEFAULT_TITLE
        defaultProblemShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the problemList where title equals to UPDATED_TITLE
        defaultProblemShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllProblemsByTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        problemRepository.saveAndFlush(problem);

        // Get all the problemList where title not equals to DEFAULT_TITLE
        defaultProblemShouldNotBeFound("title.notEquals=" + DEFAULT_TITLE);

        // Get all the problemList where title not equals to UPDATED_TITLE
        defaultProblemShouldBeFound("title.notEquals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllProblemsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        problemRepository.saveAndFlush(problem);

        // Get all the problemList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultProblemShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the problemList where title equals to UPDATED_TITLE
        defaultProblemShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllProblemsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        problemRepository.saveAndFlush(problem);

        // Get all the problemList where title is not null
        defaultProblemShouldBeFound("title.specified=true");

        // Get all the problemList where title is null
        defaultProblemShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    void getAllProblemsByTitleContainsSomething() throws Exception {
        // Initialize the database
        problemRepository.saveAndFlush(problem);

        // Get all the problemList where title contains DEFAULT_TITLE
        defaultProblemShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the problemList where title contains UPDATED_TITLE
        defaultProblemShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllProblemsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        problemRepository.saveAndFlush(problem);

        // Get all the problemList where title does not contain DEFAULT_TITLE
        defaultProblemShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the problemList where title does not contain UPDATED_TITLE
        defaultProblemShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllProblemsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        problemRepository.saveAndFlush(problem);

        // Get all the problemList where description equals to DEFAULT_DESCRIPTION
        defaultProblemShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the problemList where description equals to UPDATED_DESCRIPTION
        defaultProblemShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProblemsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        problemRepository.saveAndFlush(problem);

        // Get all the problemList where description not equals to DEFAULT_DESCRIPTION
        defaultProblemShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the problemList where description not equals to UPDATED_DESCRIPTION
        defaultProblemShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProblemsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        problemRepository.saveAndFlush(problem);

        // Get all the problemList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultProblemShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the problemList where description equals to UPDATED_DESCRIPTION
        defaultProblemShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProblemsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        problemRepository.saveAndFlush(problem);

        // Get all the problemList where description is not null
        defaultProblemShouldBeFound("description.specified=true");

        // Get all the problemList where description is null
        defaultProblemShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllProblemsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        problemRepository.saveAndFlush(problem);

        // Get all the problemList where description contains DEFAULT_DESCRIPTION
        defaultProblemShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the problemList where description contains UPDATED_DESCRIPTION
        defaultProblemShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProblemsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        problemRepository.saveAndFlush(problem);

        // Get all the problemList where description does not contain DEFAULT_DESCRIPTION
        defaultProblemShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the problemList where description does not contain UPDATED_DESCRIPTION
        defaultProblemShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProblemsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        problemRepository.saveAndFlush(problem);

        // Get all the problemList where createdBy equals to DEFAULT_CREATED_BY
        defaultProblemShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the problemList where createdBy equals to UPDATED_CREATED_BY
        defaultProblemShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllProblemsByCreatedByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        problemRepository.saveAndFlush(problem);

        // Get all the problemList where createdBy not equals to DEFAULT_CREATED_BY
        defaultProblemShouldNotBeFound("createdBy.notEquals=" + DEFAULT_CREATED_BY);

        // Get all the problemList where createdBy not equals to UPDATED_CREATED_BY
        defaultProblemShouldBeFound("createdBy.notEquals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllProblemsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        problemRepository.saveAndFlush(problem);

        // Get all the problemList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultProblemShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the problemList where createdBy equals to UPDATED_CREATED_BY
        defaultProblemShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllProblemsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        problemRepository.saveAndFlush(problem);

        // Get all the problemList where createdBy is not null
        defaultProblemShouldBeFound("createdBy.specified=true");

        // Get all the problemList where createdBy is null
        defaultProblemShouldNotBeFound("createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllProblemsByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        problemRepository.saveAndFlush(problem);

        // Get all the problemList where createdBy contains DEFAULT_CREATED_BY
        defaultProblemShouldBeFound("createdBy.contains=" + DEFAULT_CREATED_BY);

        // Get all the problemList where createdBy contains UPDATED_CREATED_BY
        defaultProblemShouldNotBeFound("createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllProblemsByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        problemRepository.saveAndFlush(problem);

        // Get all the problemList where createdBy does not contain DEFAULT_CREATED_BY
        defaultProblemShouldNotBeFound("createdBy.doesNotContain=" + DEFAULT_CREATED_BY);

        // Get all the problemList where createdBy does not contain UPDATED_CREATED_BY
        defaultProblemShouldBeFound("createdBy.doesNotContain=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllProblemsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        problemRepository.saveAndFlush(problem);

        // Get all the problemList where createdDate equals to DEFAULT_CREATED_DATE
        defaultProblemShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the problemList where createdDate equals to UPDATED_CREATED_DATE
        defaultProblemShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllProblemsByCreatedDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        problemRepository.saveAndFlush(problem);

        // Get all the problemList where createdDate not equals to DEFAULT_CREATED_DATE
        defaultProblemShouldNotBeFound("createdDate.notEquals=" + DEFAULT_CREATED_DATE);

        // Get all the problemList where createdDate not equals to UPDATED_CREATED_DATE
        defaultProblemShouldBeFound("createdDate.notEquals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllProblemsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        problemRepository.saveAndFlush(problem);

        // Get all the problemList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultProblemShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the problemList where createdDate equals to UPDATED_CREATED_DATE
        defaultProblemShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllProblemsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        problemRepository.saveAndFlush(problem);

        // Get all the problemList where createdDate is not null
        defaultProblemShouldBeFound("createdDate.specified=true");

        // Get all the problemList where createdDate is null
        defaultProblemShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllProblemsByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        problemRepository.saveAndFlush(problem);

        // Get all the problemList where lastModifiedBy equals to DEFAULT_LAST_MODIFIED_BY
        defaultProblemShouldBeFound("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the problemList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultProblemShouldNotBeFound("lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllProblemsByLastModifiedByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        problemRepository.saveAndFlush(problem);

        // Get all the problemList where lastModifiedBy not equals to DEFAULT_LAST_MODIFIED_BY
        defaultProblemShouldNotBeFound("lastModifiedBy.notEquals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the problemList where lastModifiedBy not equals to UPDATED_LAST_MODIFIED_BY
        defaultProblemShouldBeFound("lastModifiedBy.notEquals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllProblemsByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        problemRepository.saveAndFlush(problem);

        // Get all the problemList where lastModifiedBy in DEFAULT_LAST_MODIFIED_BY or UPDATED_LAST_MODIFIED_BY
        defaultProblemShouldBeFound("lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY);

        // Get all the problemList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultProblemShouldNotBeFound("lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllProblemsByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        problemRepository.saveAndFlush(problem);

        // Get all the problemList where lastModifiedBy is not null
        defaultProblemShouldBeFound("lastModifiedBy.specified=true");

        // Get all the problemList where lastModifiedBy is null
        defaultProblemShouldNotBeFound("lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllProblemsByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        problemRepository.saveAndFlush(problem);

        // Get all the problemList where lastModifiedBy contains DEFAULT_LAST_MODIFIED_BY
        defaultProblemShouldBeFound("lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the problemList where lastModifiedBy contains UPDATED_LAST_MODIFIED_BY
        defaultProblemShouldNotBeFound("lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllProblemsByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        problemRepository.saveAndFlush(problem);

        // Get all the problemList where lastModifiedBy does not contain DEFAULT_LAST_MODIFIED_BY
        defaultProblemShouldNotBeFound("lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the problemList where lastModifiedBy does not contain UPDATED_LAST_MODIFIED_BY
        defaultProblemShouldBeFound("lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllProblemsByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        problemRepository.saveAndFlush(problem);

        // Get all the problemList where lastModifiedDate equals to DEFAULT_LAST_MODIFIED_DATE
        defaultProblemShouldBeFound("lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the problemList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultProblemShouldNotBeFound("lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllProblemsByLastModifiedDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        problemRepository.saveAndFlush(problem);

        // Get all the problemList where lastModifiedDate not equals to DEFAULT_LAST_MODIFIED_DATE
        defaultProblemShouldNotBeFound("lastModifiedDate.notEquals=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the problemList where lastModifiedDate not equals to UPDATED_LAST_MODIFIED_DATE
        defaultProblemShouldBeFound("lastModifiedDate.notEquals=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllProblemsByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        problemRepository.saveAndFlush(problem);

        // Get all the problemList where lastModifiedDate in DEFAULT_LAST_MODIFIED_DATE or UPDATED_LAST_MODIFIED_DATE
        defaultProblemShouldBeFound("lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE);

        // Get all the problemList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultProblemShouldNotBeFound("lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllProblemsByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        problemRepository.saveAndFlush(problem);

        // Get all the problemList where lastModifiedDate is not null
        defaultProblemShouldBeFound("lastModifiedDate.specified=true");

        // Get all the problemList where lastModifiedDate is null
        defaultProblemShouldNotBeFound("lastModifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllProblemsByTestCaseIsEqualToSomething() throws Exception {
        // Initialize the database
        problemRepository.saveAndFlush(problem);
        TestCase testCase;
        if (TestUtil.findAll(em, TestCase.class).isEmpty()) {
            testCase = TestCaseResourceIT.createEntity(em);
            em.persist(testCase);
            em.flush();
        } else {
            testCase = TestUtil.findAll(em, TestCase.class).get(0);
        }
        em.persist(testCase);
        em.flush();
        problem.addTestCase(testCase);
        problemRepository.saveAndFlush(problem);
        Long testCaseId = testCase.getId();

        // Get all the problemList where testCase equals to testCaseId
        defaultProblemShouldBeFound("testCaseId.equals=" + testCaseId);

        // Get all the problemList where testCase equals to (testCaseId + 1)
        defaultProblemShouldNotBeFound("testCaseId.equals=" + (testCaseId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProblemShouldBeFound(String filter) throws Exception {
        restProblemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(problem.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restProblemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProblemShouldNotBeFound(String filter) throws Exception {
        restProblemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProblemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProblem() throws Exception {
        // Get the problem
        restProblemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProblem() throws Exception {
        // Initialize the database
        problemRepository.saveAndFlush(problem);

        int databaseSizeBeforeUpdate = problemRepository.findAll().size();

        // Update the problem
        Problem updatedProblem = problemRepository.findById(problem.getId()).get();
        // Disconnect from session so that the updates on updatedProblem are not directly saved in db
        em.detach(updatedProblem);
        updatedProblem
            .name(UPDATED_NAME)
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        ProblemDTO problemDTO = problemMapper.toDto(updatedProblem);

        restProblemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, problemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(problemDTO))
            )
            .andExpect(status().isOk());

        // Validate the Problem in the database
        List<Problem> problemList = problemRepository.findAll();
        assertThat(problemList).hasSize(databaseSizeBeforeUpdate);
        Problem testProblem = problemList.get(problemList.size() - 1);
        assertThat(testProblem.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProblem.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testProblem.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProblem.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testProblem.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testProblem.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testProblem.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingProblem() throws Exception {
        int databaseSizeBeforeUpdate = problemRepository.findAll().size();
        problem.setId(count.incrementAndGet());

        // Create the Problem
        ProblemDTO problemDTO = problemMapper.toDto(problem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProblemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, problemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(problemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Problem in the database
        List<Problem> problemList = problemRepository.findAll();
        assertThat(problemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProblem() throws Exception {
        int databaseSizeBeforeUpdate = problemRepository.findAll().size();
        problem.setId(count.incrementAndGet());

        // Create the Problem
        ProblemDTO problemDTO = problemMapper.toDto(problem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProblemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(problemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Problem in the database
        List<Problem> problemList = problemRepository.findAll();
        assertThat(problemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProblem() throws Exception {
        int databaseSizeBeforeUpdate = problemRepository.findAll().size();
        problem.setId(count.incrementAndGet());

        // Create the Problem
        ProblemDTO problemDTO = problemMapper.toDto(problem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProblemMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(problemDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Problem in the database
        List<Problem> problemList = problemRepository.findAll();
        assertThat(problemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProblemWithPatch() throws Exception {
        // Initialize the database
        problemRepository.saveAndFlush(problem);

        int databaseSizeBeforeUpdate = problemRepository.findAll().size();

        // Update the problem using partial update
        Problem partialUpdatedProblem = new Problem();
        partialUpdatedProblem.setId(problem.getId());

        partialUpdatedProblem
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restProblemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProblem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProblem))
            )
            .andExpect(status().isOk());

        // Validate the Problem in the database
        List<Problem> problemList = problemRepository.findAll();
        assertThat(problemList).hasSize(databaseSizeBeforeUpdate);
        Problem testProblem = problemList.get(problemList.size() - 1);
        assertThat(testProblem.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProblem.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testProblem.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProblem.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testProblem.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testProblem.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testProblem.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateProblemWithPatch() throws Exception {
        // Initialize the database
        problemRepository.saveAndFlush(problem);

        int databaseSizeBeforeUpdate = problemRepository.findAll().size();

        // Update the problem using partial update
        Problem partialUpdatedProblem = new Problem();
        partialUpdatedProblem.setId(problem.getId());

        partialUpdatedProblem
            .name(UPDATED_NAME)
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restProblemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProblem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProblem))
            )
            .andExpect(status().isOk());

        // Validate the Problem in the database
        List<Problem> problemList = problemRepository.findAll();
        assertThat(problemList).hasSize(databaseSizeBeforeUpdate);
        Problem testProblem = problemList.get(problemList.size() - 1);
        assertThat(testProblem.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProblem.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testProblem.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProblem.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testProblem.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testProblem.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testProblem.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingProblem() throws Exception {
        int databaseSizeBeforeUpdate = problemRepository.findAll().size();
        problem.setId(count.incrementAndGet());

        // Create the Problem
        ProblemDTO problemDTO = problemMapper.toDto(problem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProblemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, problemDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(problemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Problem in the database
        List<Problem> problemList = problemRepository.findAll();
        assertThat(problemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProblem() throws Exception {
        int databaseSizeBeforeUpdate = problemRepository.findAll().size();
        problem.setId(count.incrementAndGet());

        // Create the Problem
        ProblemDTO problemDTO = problemMapper.toDto(problem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProblemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(problemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Problem in the database
        List<Problem> problemList = problemRepository.findAll();
        assertThat(problemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProblem() throws Exception {
        int databaseSizeBeforeUpdate = problemRepository.findAll().size();
        problem.setId(count.incrementAndGet());

        // Create the Problem
        ProblemDTO problemDTO = problemMapper.toDto(problem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProblemMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(problemDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Problem in the database
        List<Problem> problemList = problemRepository.findAll();
        assertThat(problemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProblem() throws Exception {
        // Initialize the database
        problemRepository.saveAndFlush(problem);

        int databaseSizeBeforeDelete = problemRepository.findAll().size();

        // Delete the problem
        restProblemMockMvc
            .perform(delete(ENTITY_API_URL_ID, problem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Problem> problemList = problemRepository.findAll();
        assertThat(problemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
