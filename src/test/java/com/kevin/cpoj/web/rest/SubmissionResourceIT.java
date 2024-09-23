package com.kevin.cpoj.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.kevin.cpoj.IntegrationTest;
import com.kevin.cpoj.domain.Problem;
import com.kevin.cpoj.domain.Submission;
import com.kevin.cpoj.domain.SubmissionTestCaseResult;
import com.kevin.cpoj.domain.enumeration.ProgrammingLanguage;
import com.kevin.cpoj.domain.enumeration.SubmissionResultMessage;
import com.kevin.cpoj.domain.enumeration.SubmissionResultStatus;
import com.kevin.cpoj.repository.SubmissionRepository;
import com.kevin.cpoj.service.criteria.SubmissionCriteria;
import com.kevin.cpoj.service.dto.SubmissionDTO;
import com.kevin.cpoj.service.mapper.SubmissionMapper;
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
 * Integration tests for the {@link SubmissionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SubmissionResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final ProgrammingLanguage DEFAULT_PROGRAMMING_LANGUAGE = ProgrammingLanguage.JAVA;
    private static final ProgrammingLanguage UPDATED_PROGRAMMING_LANGUAGE = ProgrammingLanguage.PYTHON3;

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final SubmissionResultStatus DEFAULT_OVERALL_RESULT_STATUS = SubmissionResultStatus.WAIT;
    private static final SubmissionResultStatus UPDATED_OVERALL_RESULT_STATUS = SubmissionResultStatus.JUDGING;

    private static final SubmissionResultMessage DEFAULT_OVERALL_RESULT_MESSAGE = SubmissionResultMessage.NA;
    private static final SubmissionResultMessage UPDATED_OVERALL_RESULT_MESSAGE = SubmissionResultMessage.PASS;

    private static final String DEFAULT_OVERALL_RESULT_MESSAGE_DETAIL = "AAAAAAAAAA";
    private static final String UPDATED_OVERALL_RESULT_MESSAGE_DETAIL = "BBBBBBBBBB";

    private static final Integer DEFAULT_OVERALL_RESULT_TRIES = 0;
    private static final Integer UPDATED_OVERALL_RESULT_TRIES = 1;
    private static final Integer SMALLER_OVERALL_RESULT_TRIES = 0 - 1;

    private static final Integer DEFAULT_OVERALL_RESULT_SCORE_PERCENTAGE = 0;
    private static final Integer UPDATED_OVERALL_RESULT_SCORE_PERCENTAGE = 1;
    private static final Integer SMALLER_OVERALL_RESULT_SCORE_PERCENTAGE = 0 - 1;

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/submissions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private SubmissionMapper submissionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSubmissionMockMvc;

    private Submission submission;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Submission createEntity(EntityManager em) {
        Submission submission = new Submission()
            .name(DEFAULT_NAME)
            .programmingLanguage(DEFAULT_PROGRAMMING_LANGUAGE)
            .code(DEFAULT_CODE)
            .overallResultStatus(DEFAULT_OVERALL_RESULT_STATUS)
            .overallResultMessage(DEFAULT_OVERALL_RESULT_MESSAGE)
            .overallResultMessageDetail(DEFAULT_OVERALL_RESULT_MESSAGE_DETAIL)
            .overallResultTries(DEFAULT_OVERALL_RESULT_TRIES)
            .overallResultScorePercentage(DEFAULT_OVERALL_RESULT_SCORE_PERCENTAGE)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return submission;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Submission createUpdatedEntity(EntityManager em) {
        Submission submission = new Submission()
            .name(UPDATED_NAME)
            .programmingLanguage(UPDATED_PROGRAMMING_LANGUAGE)
            .code(UPDATED_CODE)
            .overallResultStatus(UPDATED_OVERALL_RESULT_STATUS)
            .overallResultMessage(UPDATED_OVERALL_RESULT_MESSAGE)
            .overallResultMessageDetail(UPDATED_OVERALL_RESULT_MESSAGE_DETAIL)
            .overallResultTries(UPDATED_OVERALL_RESULT_TRIES)
            .overallResultScorePercentage(UPDATED_OVERALL_RESULT_SCORE_PERCENTAGE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return submission;
    }

    @BeforeEach
    public void initTest() {
        submission = createEntity(em);
    }

    @Test
    @Transactional
    void createSubmission() throws Exception {
        int databaseSizeBeforeCreate = submissionRepository.findAll().size();
        // Create the Submission
        SubmissionDTO submissionDTO = submissionMapper.toDto(submission);
        restSubmissionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(submissionDTO)))
            .andExpect(status().isCreated());

        // Validate the Submission in the database
        List<Submission> submissionList = submissionRepository.findAll();
        assertThat(submissionList).hasSize(databaseSizeBeforeCreate + 1);
        Submission testSubmission = submissionList.get(submissionList.size() - 1);
        assertThat(testSubmission.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSubmission.getProgrammingLanguage()).isEqualTo(DEFAULT_PROGRAMMING_LANGUAGE);
        assertThat(testSubmission.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testSubmission.getOverallResultStatus()).isEqualTo(DEFAULT_OVERALL_RESULT_STATUS);
        assertThat(testSubmission.getOverallResultMessage()).isEqualTo(DEFAULT_OVERALL_RESULT_MESSAGE);
        assertThat(testSubmission.getOverallResultMessageDetail()).isEqualTo(DEFAULT_OVERALL_RESULT_MESSAGE_DETAIL);
        assertThat(testSubmission.getOverallResultTries()).isEqualTo(DEFAULT_OVERALL_RESULT_TRIES);
        assertThat(testSubmission.getOverallResultScorePercentage()).isEqualTo(DEFAULT_OVERALL_RESULT_SCORE_PERCENTAGE);
        assertThat(testSubmission.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testSubmission.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testSubmission.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testSubmission.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void createSubmissionWithExistingId() throws Exception {
        // Create the Submission with an existing ID
        submission.setId(1L);
        SubmissionDTO submissionDTO = submissionMapper.toDto(submission);

        int databaseSizeBeforeCreate = submissionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubmissionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(submissionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Submission in the database
        List<Submission> submissionList = submissionRepository.findAll();
        assertThat(submissionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = submissionRepository.findAll().size();
        // set the field null
        submission.setName(null);

        // Create the Submission, which fails.
        SubmissionDTO submissionDTO = submissionMapper.toDto(submission);

        restSubmissionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(submissionDTO)))
            .andExpect(status().isBadRequest());

        List<Submission> submissionList = submissionRepository.findAll();
        assertThat(submissionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = submissionRepository.findAll().size();
        // set the field null
        submission.setCode(null);

        // Create the Submission, which fails.
        SubmissionDTO submissionDTO = submissionMapper.toDto(submission);

        restSubmissionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(submissionDTO)))
            .andExpect(status().isBadRequest());

        List<Submission> submissionList = submissionRepository.findAll();
        assertThat(submissionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOverallResultStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = submissionRepository.findAll().size();
        // set the field null
        submission.setOverallResultStatus(null);

        // Create the Submission, which fails.
        SubmissionDTO submissionDTO = submissionMapper.toDto(submission);

        restSubmissionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(submissionDTO)))
            .andExpect(status().isBadRequest());

        List<Submission> submissionList = submissionRepository.findAll();
        assertThat(submissionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOverallResultMessageIsRequired() throws Exception {
        int databaseSizeBeforeTest = submissionRepository.findAll().size();
        // set the field null
        submission.setOverallResultMessage(null);

        // Create the Submission, which fails.
        SubmissionDTO submissionDTO = submissionMapper.toDto(submission);

        restSubmissionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(submissionDTO)))
            .andExpect(status().isBadRequest());

        List<Submission> submissionList = submissionRepository.findAll();
        assertThat(submissionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOverallResultTriesIsRequired() throws Exception {
        int databaseSizeBeforeTest = submissionRepository.findAll().size();
        // set the field null
        submission.setOverallResultTries(null);

        // Create the Submission, which fails.
        SubmissionDTO submissionDTO = submissionMapper.toDto(submission);

        restSubmissionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(submissionDTO)))
            .andExpect(status().isBadRequest());

        List<Submission> submissionList = submissionRepository.findAll();
        assertThat(submissionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOverallResultScorePercentageIsRequired() throws Exception {
        int databaseSizeBeforeTest = submissionRepository.findAll().size();
        // set the field null
        submission.setOverallResultScorePercentage(null);

        // Create the Submission, which fails.
        SubmissionDTO submissionDTO = submissionMapper.toDto(submission);

        restSubmissionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(submissionDTO)))
            .andExpect(status().isBadRequest());

        List<Submission> submissionList = submissionRepository.findAll();
        assertThat(submissionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedByIsRequired() throws Exception {
        int databaseSizeBeforeTest = submissionRepository.findAll().size();
        // set the field null
        submission.setCreatedBy(null);

        // Create the Submission, which fails.
        SubmissionDTO submissionDTO = submissionMapper.toDto(submission);

        restSubmissionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(submissionDTO)))
            .andExpect(status().isBadRequest());

        List<Submission> submissionList = submissionRepository.findAll();
        assertThat(submissionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastModifiedByIsRequired() throws Exception {
        int databaseSizeBeforeTest = submissionRepository.findAll().size();
        // set the field null
        submission.setLastModifiedBy(null);

        // Create the Submission, which fails.
        SubmissionDTO submissionDTO = submissionMapper.toDto(submission);

        restSubmissionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(submissionDTO)))
            .andExpect(status().isBadRequest());

        List<Submission> submissionList = submissionRepository.findAll();
        assertThat(submissionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSubmissions() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        // Get all the submissionList
        restSubmissionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(submission.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].programmingLanguage").value(hasItem(DEFAULT_PROGRAMMING_LANGUAGE.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].overallResultStatus").value(hasItem(DEFAULT_OVERALL_RESULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].overallResultMessage").value(hasItem(DEFAULT_OVERALL_RESULT_MESSAGE.toString())))
            .andExpect(jsonPath("$.[*].overallResultMessageDetail").value(hasItem(DEFAULT_OVERALL_RESULT_MESSAGE_DETAIL)))
            .andExpect(jsonPath("$.[*].overallResultTries").value(hasItem(DEFAULT_OVERALL_RESULT_TRIES)))
            .andExpect(jsonPath("$.[*].overallResultScorePercentage").value(hasItem(DEFAULT_OVERALL_RESULT_SCORE_PERCENTAGE)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getSubmission() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        // Get the submission
        restSubmissionMockMvc
            .perform(get(ENTITY_API_URL_ID, submission.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(submission.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.programmingLanguage").value(DEFAULT_PROGRAMMING_LANGUAGE.toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.overallResultStatus").value(DEFAULT_OVERALL_RESULT_STATUS.toString()))
            .andExpect(jsonPath("$.overallResultMessage").value(DEFAULT_OVERALL_RESULT_MESSAGE.toString()))
            .andExpect(jsonPath("$.overallResultMessageDetail").value(DEFAULT_OVERALL_RESULT_MESSAGE_DETAIL))
            .andExpect(jsonPath("$.overallResultTries").value(DEFAULT_OVERALL_RESULT_TRIES))
            .andExpect(jsonPath("$.overallResultScorePercentage").value(DEFAULT_OVERALL_RESULT_SCORE_PERCENTAGE))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getSubmissionsByIdFiltering() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        Long id = submission.getId();

        defaultSubmissionShouldBeFound("id.equals=" + id);
        defaultSubmissionShouldNotBeFound("id.notEquals=" + id);

        defaultSubmissionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSubmissionShouldNotBeFound("id.greaterThan=" + id);

        defaultSubmissionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSubmissionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSubmissionsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        // Get all the submissionList where name equals to DEFAULT_NAME
        defaultSubmissionShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the submissionList where name equals to UPDATED_NAME
        defaultSubmissionShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSubmissionsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        // Get all the submissionList where name not equals to DEFAULT_NAME
        defaultSubmissionShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the submissionList where name not equals to UPDATED_NAME
        defaultSubmissionShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSubmissionsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        // Get all the submissionList where name in DEFAULT_NAME or UPDATED_NAME
        defaultSubmissionShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the submissionList where name equals to UPDATED_NAME
        defaultSubmissionShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSubmissionsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        // Get all the submissionList where name is not null
        defaultSubmissionShouldBeFound("name.specified=true");

        // Get all the submissionList where name is null
        defaultSubmissionShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllSubmissionsByNameContainsSomething() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        // Get all the submissionList where name contains DEFAULT_NAME
        defaultSubmissionShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the submissionList where name contains UPDATED_NAME
        defaultSubmissionShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSubmissionsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        // Get all the submissionList where name does not contain DEFAULT_NAME
        defaultSubmissionShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the submissionList where name does not contain UPDATED_NAME
        defaultSubmissionShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSubmissionsByProgrammingLanguageIsEqualToSomething() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        // Get all the submissionList where programmingLanguage equals to DEFAULT_PROGRAMMING_LANGUAGE
        defaultSubmissionShouldBeFound("programmingLanguage.equals=" + DEFAULT_PROGRAMMING_LANGUAGE);

        // Get all the submissionList where programmingLanguage equals to UPDATED_PROGRAMMING_LANGUAGE
        defaultSubmissionShouldNotBeFound("programmingLanguage.equals=" + UPDATED_PROGRAMMING_LANGUAGE);
    }

    @Test
    @Transactional
    void getAllSubmissionsByProgrammingLanguageIsNotEqualToSomething() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        // Get all the submissionList where programmingLanguage not equals to DEFAULT_PROGRAMMING_LANGUAGE
        defaultSubmissionShouldNotBeFound("programmingLanguage.notEquals=" + DEFAULT_PROGRAMMING_LANGUAGE);

        // Get all the submissionList where programmingLanguage not equals to UPDATED_PROGRAMMING_LANGUAGE
        defaultSubmissionShouldBeFound("programmingLanguage.notEquals=" + UPDATED_PROGRAMMING_LANGUAGE);
    }

    @Test
    @Transactional
    void getAllSubmissionsByProgrammingLanguageIsInShouldWork() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        // Get all the submissionList where programmingLanguage in DEFAULT_PROGRAMMING_LANGUAGE or UPDATED_PROGRAMMING_LANGUAGE
        defaultSubmissionShouldBeFound("programmingLanguage.in=" + DEFAULT_PROGRAMMING_LANGUAGE + "," + UPDATED_PROGRAMMING_LANGUAGE);

        // Get all the submissionList where programmingLanguage equals to UPDATED_PROGRAMMING_LANGUAGE
        defaultSubmissionShouldNotBeFound("programmingLanguage.in=" + UPDATED_PROGRAMMING_LANGUAGE);
    }

    @Test
    @Transactional
    void getAllSubmissionsByProgrammingLanguageIsNullOrNotNull() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        // Get all the submissionList where programmingLanguage is not null
        defaultSubmissionShouldBeFound("programmingLanguage.specified=true");

        // Get all the submissionList where programmingLanguage is null
        defaultSubmissionShouldNotBeFound("programmingLanguage.specified=false");
    }

    @Test
    @Transactional
    void getAllSubmissionsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        // Get all the submissionList where code equals to DEFAULT_CODE
        defaultSubmissionShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the submissionList where code equals to UPDATED_CODE
        defaultSubmissionShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllSubmissionsByCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        // Get all the submissionList where code not equals to DEFAULT_CODE
        defaultSubmissionShouldNotBeFound("code.notEquals=" + DEFAULT_CODE);

        // Get all the submissionList where code not equals to UPDATED_CODE
        defaultSubmissionShouldBeFound("code.notEquals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllSubmissionsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        // Get all the submissionList where code in DEFAULT_CODE or UPDATED_CODE
        defaultSubmissionShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the submissionList where code equals to UPDATED_CODE
        defaultSubmissionShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllSubmissionsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        // Get all the submissionList where code is not null
        defaultSubmissionShouldBeFound("code.specified=true");

        // Get all the submissionList where code is null
        defaultSubmissionShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllSubmissionsByCodeContainsSomething() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        // Get all the submissionList where code contains DEFAULT_CODE
        defaultSubmissionShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the submissionList where code contains UPDATED_CODE
        defaultSubmissionShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllSubmissionsByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        // Get all the submissionList where code does not contain DEFAULT_CODE
        defaultSubmissionShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the submissionList where code does not contain UPDATED_CODE
        defaultSubmissionShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllSubmissionsByOverallResultStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        // Get all the submissionList where overallResultStatus equals to DEFAULT_OVERALL_RESULT_STATUS
        defaultSubmissionShouldBeFound("overallResultStatus.equals=" + DEFAULT_OVERALL_RESULT_STATUS);

        // Get all the submissionList where overallResultStatus equals to UPDATED_OVERALL_RESULT_STATUS
        defaultSubmissionShouldNotBeFound("overallResultStatus.equals=" + UPDATED_OVERALL_RESULT_STATUS);
    }

    @Test
    @Transactional
    void getAllSubmissionsByOverallResultStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        // Get all the submissionList where overallResultStatus not equals to DEFAULT_OVERALL_RESULT_STATUS
        defaultSubmissionShouldNotBeFound("overallResultStatus.notEquals=" + DEFAULT_OVERALL_RESULT_STATUS);

        // Get all the submissionList where overallResultStatus not equals to UPDATED_OVERALL_RESULT_STATUS
        defaultSubmissionShouldBeFound("overallResultStatus.notEquals=" + UPDATED_OVERALL_RESULT_STATUS);
    }

    @Test
    @Transactional
    void getAllSubmissionsByOverallResultStatusIsInShouldWork() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        // Get all the submissionList where overallResultStatus in DEFAULT_OVERALL_RESULT_STATUS or UPDATED_OVERALL_RESULT_STATUS
        defaultSubmissionShouldBeFound("overallResultStatus.in=" + DEFAULT_OVERALL_RESULT_STATUS + "," + UPDATED_OVERALL_RESULT_STATUS);

        // Get all the submissionList where overallResultStatus equals to UPDATED_OVERALL_RESULT_STATUS
        defaultSubmissionShouldNotBeFound("overallResultStatus.in=" + UPDATED_OVERALL_RESULT_STATUS);
    }

    @Test
    @Transactional
    void getAllSubmissionsByOverallResultStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        // Get all the submissionList where overallResultStatus is not null
        defaultSubmissionShouldBeFound("overallResultStatus.specified=true");

        // Get all the submissionList where overallResultStatus is null
        defaultSubmissionShouldNotBeFound("overallResultStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllSubmissionsByOverallResultMessageIsEqualToSomething() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        // Get all the submissionList where overallResultMessage equals to DEFAULT_OVERALL_RESULT_MESSAGE
        defaultSubmissionShouldBeFound("overallResultMessage.equals=" + DEFAULT_OVERALL_RESULT_MESSAGE);

        // Get all the submissionList where overallResultMessage equals to UPDATED_OVERALL_RESULT_MESSAGE
        defaultSubmissionShouldNotBeFound("overallResultMessage.equals=" + UPDATED_OVERALL_RESULT_MESSAGE);
    }

    @Test
    @Transactional
    void getAllSubmissionsByOverallResultMessageIsNotEqualToSomething() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        // Get all the submissionList where overallResultMessage not equals to DEFAULT_OVERALL_RESULT_MESSAGE
        defaultSubmissionShouldNotBeFound("overallResultMessage.notEquals=" + DEFAULT_OVERALL_RESULT_MESSAGE);

        // Get all the submissionList where overallResultMessage not equals to UPDATED_OVERALL_RESULT_MESSAGE
        defaultSubmissionShouldBeFound("overallResultMessage.notEquals=" + UPDATED_OVERALL_RESULT_MESSAGE);
    }

    @Test
    @Transactional
    void getAllSubmissionsByOverallResultMessageIsInShouldWork() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        // Get all the submissionList where overallResultMessage in DEFAULT_OVERALL_RESULT_MESSAGE or UPDATED_OVERALL_RESULT_MESSAGE
        defaultSubmissionShouldBeFound("overallResultMessage.in=" + DEFAULT_OVERALL_RESULT_MESSAGE + "," + UPDATED_OVERALL_RESULT_MESSAGE);

        // Get all the submissionList where overallResultMessage equals to UPDATED_OVERALL_RESULT_MESSAGE
        defaultSubmissionShouldNotBeFound("overallResultMessage.in=" + UPDATED_OVERALL_RESULT_MESSAGE);
    }

    @Test
    @Transactional
    void getAllSubmissionsByOverallResultMessageIsNullOrNotNull() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        // Get all the submissionList where overallResultMessage is not null
        defaultSubmissionShouldBeFound("overallResultMessage.specified=true");

        // Get all the submissionList where overallResultMessage is null
        defaultSubmissionShouldNotBeFound("overallResultMessage.specified=false");
    }

    @Test
    @Transactional
    void getAllSubmissionsByOverallResultMessageDetailIsEqualToSomething() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        // Get all the submissionList where overallResultMessageDetail equals to DEFAULT_OVERALL_RESULT_MESSAGE_DETAIL
        defaultSubmissionShouldBeFound("overallResultMessageDetail.equals=" + DEFAULT_OVERALL_RESULT_MESSAGE_DETAIL);

        // Get all the submissionList where overallResultMessageDetail equals to UPDATED_OVERALL_RESULT_MESSAGE_DETAIL
        defaultSubmissionShouldNotBeFound("overallResultMessageDetail.equals=" + UPDATED_OVERALL_RESULT_MESSAGE_DETAIL);
    }

    @Test
    @Transactional
    void getAllSubmissionsByOverallResultMessageDetailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        // Get all the submissionList where overallResultMessageDetail not equals to DEFAULT_OVERALL_RESULT_MESSAGE_DETAIL
        defaultSubmissionShouldNotBeFound("overallResultMessageDetail.notEquals=" + DEFAULT_OVERALL_RESULT_MESSAGE_DETAIL);

        // Get all the submissionList where overallResultMessageDetail not equals to UPDATED_OVERALL_RESULT_MESSAGE_DETAIL
        defaultSubmissionShouldBeFound("overallResultMessageDetail.notEquals=" + UPDATED_OVERALL_RESULT_MESSAGE_DETAIL);
    }

    @Test
    @Transactional
    void getAllSubmissionsByOverallResultMessageDetailIsInShouldWork() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        // Get all the submissionList where overallResultMessageDetail in DEFAULT_OVERALL_RESULT_MESSAGE_DETAIL or UPDATED_OVERALL_RESULT_MESSAGE_DETAIL
        defaultSubmissionShouldBeFound(
            "overallResultMessageDetail.in=" + DEFAULT_OVERALL_RESULT_MESSAGE_DETAIL + "," + UPDATED_OVERALL_RESULT_MESSAGE_DETAIL
        );

        // Get all the submissionList where overallResultMessageDetail equals to UPDATED_OVERALL_RESULT_MESSAGE_DETAIL
        defaultSubmissionShouldNotBeFound("overallResultMessageDetail.in=" + UPDATED_OVERALL_RESULT_MESSAGE_DETAIL);
    }

    @Test
    @Transactional
    void getAllSubmissionsByOverallResultMessageDetailIsNullOrNotNull() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        // Get all the submissionList where overallResultMessageDetail is not null
        defaultSubmissionShouldBeFound("overallResultMessageDetail.specified=true");

        // Get all the submissionList where overallResultMessageDetail is null
        defaultSubmissionShouldNotBeFound("overallResultMessageDetail.specified=false");
    }

    @Test
    @Transactional
    void getAllSubmissionsByOverallResultMessageDetailContainsSomething() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        // Get all the submissionList where overallResultMessageDetail contains DEFAULT_OVERALL_RESULT_MESSAGE_DETAIL
        defaultSubmissionShouldBeFound("overallResultMessageDetail.contains=" + DEFAULT_OVERALL_RESULT_MESSAGE_DETAIL);

        // Get all the submissionList where overallResultMessageDetail contains UPDATED_OVERALL_RESULT_MESSAGE_DETAIL
        defaultSubmissionShouldNotBeFound("overallResultMessageDetail.contains=" + UPDATED_OVERALL_RESULT_MESSAGE_DETAIL);
    }

    @Test
    @Transactional
    void getAllSubmissionsByOverallResultMessageDetailNotContainsSomething() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        // Get all the submissionList where overallResultMessageDetail does not contain DEFAULT_OVERALL_RESULT_MESSAGE_DETAIL
        defaultSubmissionShouldNotBeFound("overallResultMessageDetail.doesNotContain=" + DEFAULT_OVERALL_RESULT_MESSAGE_DETAIL);

        // Get all the submissionList where overallResultMessageDetail does not contain UPDATED_OVERALL_RESULT_MESSAGE_DETAIL
        defaultSubmissionShouldBeFound("overallResultMessageDetail.doesNotContain=" + UPDATED_OVERALL_RESULT_MESSAGE_DETAIL);
    }

    @Test
    @Transactional
    void getAllSubmissionsByOverallResultTriesIsEqualToSomething() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        // Get all the submissionList where overallResultTries equals to DEFAULT_OVERALL_RESULT_TRIES
        defaultSubmissionShouldBeFound("overallResultTries.equals=" + DEFAULT_OVERALL_RESULT_TRIES);

        // Get all the submissionList where overallResultTries equals to UPDATED_OVERALL_RESULT_TRIES
        defaultSubmissionShouldNotBeFound("overallResultTries.equals=" + UPDATED_OVERALL_RESULT_TRIES);
    }

    @Test
    @Transactional
    void getAllSubmissionsByOverallResultTriesIsNotEqualToSomething() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        // Get all the submissionList where overallResultTries not equals to DEFAULT_OVERALL_RESULT_TRIES
        defaultSubmissionShouldNotBeFound("overallResultTries.notEquals=" + DEFAULT_OVERALL_RESULT_TRIES);

        // Get all the submissionList where overallResultTries not equals to UPDATED_OVERALL_RESULT_TRIES
        defaultSubmissionShouldBeFound("overallResultTries.notEquals=" + UPDATED_OVERALL_RESULT_TRIES);
    }

    @Test
    @Transactional
    void getAllSubmissionsByOverallResultTriesIsInShouldWork() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        // Get all the submissionList where overallResultTries in DEFAULT_OVERALL_RESULT_TRIES or UPDATED_OVERALL_RESULT_TRIES
        defaultSubmissionShouldBeFound("overallResultTries.in=" + DEFAULT_OVERALL_RESULT_TRIES + "," + UPDATED_OVERALL_RESULT_TRIES);

        // Get all the submissionList where overallResultTries equals to UPDATED_OVERALL_RESULT_TRIES
        defaultSubmissionShouldNotBeFound("overallResultTries.in=" + UPDATED_OVERALL_RESULT_TRIES);
    }

    @Test
    @Transactional
    void getAllSubmissionsByOverallResultTriesIsNullOrNotNull() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        // Get all the submissionList where overallResultTries is not null
        defaultSubmissionShouldBeFound("overallResultTries.specified=true");

        // Get all the submissionList where overallResultTries is null
        defaultSubmissionShouldNotBeFound("overallResultTries.specified=false");
    }

    @Test
    @Transactional
    void getAllSubmissionsByOverallResultTriesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        // Get all the submissionList where overallResultTries is greater than or equal to DEFAULT_OVERALL_RESULT_TRIES
        defaultSubmissionShouldBeFound("overallResultTries.greaterThanOrEqual=" + DEFAULT_OVERALL_RESULT_TRIES);

        // Get all the submissionList where overallResultTries is greater than or equal to (DEFAULT_OVERALL_RESULT_TRIES + 1)
        defaultSubmissionShouldNotBeFound("overallResultTries.greaterThanOrEqual=" + (DEFAULT_OVERALL_RESULT_TRIES + 1));
    }

    @Test
    @Transactional
    void getAllSubmissionsByOverallResultTriesIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        // Get all the submissionList where overallResultTries is less than or equal to DEFAULT_OVERALL_RESULT_TRIES
        defaultSubmissionShouldBeFound("overallResultTries.lessThanOrEqual=" + DEFAULT_OVERALL_RESULT_TRIES);

        // Get all the submissionList where overallResultTries is less than or equal to SMALLER_OVERALL_RESULT_TRIES
        defaultSubmissionShouldNotBeFound("overallResultTries.lessThanOrEqual=" + SMALLER_OVERALL_RESULT_TRIES);
    }

    @Test
    @Transactional
    void getAllSubmissionsByOverallResultTriesIsLessThanSomething() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        // Get all the submissionList where overallResultTries is less than DEFAULT_OVERALL_RESULT_TRIES
        defaultSubmissionShouldNotBeFound("overallResultTries.lessThan=" + DEFAULT_OVERALL_RESULT_TRIES);

        // Get all the submissionList where overallResultTries is less than (DEFAULT_OVERALL_RESULT_TRIES + 1)
        defaultSubmissionShouldBeFound("overallResultTries.lessThan=" + (DEFAULT_OVERALL_RESULT_TRIES + 1));
    }

    @Test
    @Transactional
    void getAllSubmissionsByOverallResultTriesIsGreaterThanSomething() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        // Get all the submissionList where overallResultTries is greater than DEFAULT_OVERALL_RESULT_TRIES
        defaultSubmissionShouldNotBeFound("overallResultTries.greaterThan=" + DEFAULT_OVERALL_RESULT_TRIES);

        // Get all the submissionList where overallResultTries is greater than SMALLER_OVERALL_RESULT_TRIES
        defaultSubmissionShouldBeFound("overallResultTries.greaterThan=" + SMALLER_OVERALL_RESULT_TRIES);
    }

    @Test
    @Transactional
    void getAllSubmissionsByOverallResultScorePercentageIsEqualToSomething() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        // Get all the submissionList where overallResultScorePercentage equals to DEFAULT_OVERALL_RESULT_SCORE_PERCENTAGE
        defaultSubmissionShouldBeFound("overallResultScorePercentage.equals=" + DEFAULT_OVERALL_RESULT_SCORE_PERCENTAGE);

        // Get all the submissionList where overallResultScorePercentage equals to UPDATED_OVERALL_RESULT_SCORE_PERCENTAGE
        defaultSubmissionShouldNotBeFound("overallResultScorePercentage.equals=" + UPDATED_OVERALL_RESULT_SCORE_PERCENTAGE);
    }

    @Test
    @Transactional
    void getAllSubmissionsByOverallResultScorePercentageIsNotEqualToSomething() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        // Get all the submissionList where overallResultScorePercentage not equals to DEFAULT_OVERALL_RESULT_SCORE_PERCENTAGE
        defaultSubmissionShouldNotBeFound("overallResultScorePercentage.notEquals=" + DEFAULT_OVERALL_RESULT_SCORE_PERCENTAGE);

        // Get all the submissionList where overallResultScorePercentage not equals to UPDATED_OVERALL_RESULT_SCORE_PERCENTAGE
        defaultSubmissionShouldBeFound("overallResultScorePercentage.notEquals=" + UPDATED_OVERALL_RESULT_SCORE_PERCENTAGE);
    }

    @Test
    @Transactional
    void getAllSubmissionsByOverallResultScorePercentageIsInShouldWork() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        // Get all the submissionList where overallResultScorePercentage in DEFAULT_OVERALL_RESULT_SCORE_PERCENTAGE or UPDATED_OVERALL_RESULT_SCORE_PERCENTAGE
        defaultSubmissionShouldBeFound(
            "overallResultScorePercentage.in=" + DEFAULT_OVERALL_RESULT_SCORE_PERCENTAGE + "," + UPDATED_OVERALL_RESULT_SCORE_PERCENTAGE
        );

        // Get all the submissionList where overallResultScorePercentage equals to UPDATED_OVERALL_RESULT_SCORE_PERCENTAGE
        defaultSubmissionShouldNotBeFound("overallResultScorePercentage.in=" + UPDATED_OVERALL_RESULT_SCORE_PERCENTAGE);
    }

    @Test
    @Transactional
    void getAllSubmissionsByOverallResultScorePercentageIsNullOrNotNull() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        // Get all the submissionList where overallResultScorePercentage is not null
        defaultSubmissionShouldBeFound("overallResultScorePercentage.specified=true");

        // Get all the submissionList where overallResultScorePercentage is null
        defaultSubmissionShouldNotBeFound("overallResultScorePercentage.specified=false");
    }

    @Test
    @Transactional
    void getAllSubmissionsByOverallResultScorePercentageIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        // Get all the submissionList where overallResultScorePercentage is greater than or equal to DEFAULT_OVERALL_RESULT_SCORE_PERCENTAGE
        defaultSubmissionShouldBeFound("overallResultScorePercentage.greaterThanOrEqual=" + DEFAULT_OVERALL_RESULT_SCORE_PERCENTAGE);

        // Get all the submissionList where overallResultScorePercentage is greater than or equal to (DEFAULT_OVERALL_RESULT_SCORE_PERCENTAGE + 1)
        defaultSubmissionShouldNotBeFound(
            "overallResultScorePercentage.greaterThanOrEqual=" + (DEFAULT_OVERALL_RESULT_SCORE_PERCENTAGE + 1)
        );
    }

    @Test
    @Transactional
    void getAllSubmissionsByOverallResultScorePercentageIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        // Get all the submissionList where overallResultScorePercentage is less than or equal to DEFAULT_OVERALL_RESULT_SCORE_PERCENTAGE
        defaultSubmissionShouldBeFound("overallResultScorePercentage.lessThanOrEqual=" + DEFAULT_OVERALL_RESULT_SCORE_PERCENTAGE);

        // Get all the submissionList where overallResultScorePercentage is less than or equal to SMALLER_OVERALL_RESULT_SCORE_PERCENTAGE
        defaultSubmissionShouldNotBeFound("overallResultScorePercentage.lessThanOrEqual=" + SMALLER_OVERALL_RESULT_SCORE_PERCENTAGE);
    }

    @Test
    @Transactional
    void getAllSubmissionsByOverallResultScorePercentageIsLessThanSomething() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        // Get all the submissionList where overallResultScorePercentage is less than DEFAULT_OVERALL_RESULT_SCORE_PERCENTAGE
        defaultSubmissionShouldNotBeFound("overallResultScorePercentage.lessThan=" + DEFAULT_OVERALL_RESULT_SCORE_PERCENTAGE);

        // Get all the submissionList where overallResultScorePercentage is less than (DEFAULT_OVERALL_RESULT_SCORE_PERCENTAGE + 1)
        defaultSubmissionShouldBeFound("overallResultScorePercentage.lessThan=" + (DEFAULT_OVERALL_RESULT_SCORE_PERCENTAGE + 1));
    }

    @Test
    @Transactional
    void getAllSubmissionsByOverallResultScorePercentageIsGreaterThanSomething() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        // Get all the submissionList where overallResultScorePercentage is greater than DEFAULT_OVERALL_RESULT_SCORE_PERCENTAGE
        defaultSubmissionShouldNotBeFound("overallResultScorePercentage.greaterThan=" + DEFAULT_OVERALL_RESULT_SCORE_PERCENTAGE);

        // Get all the submissionList where overallResultScorePercentage is greater than SMALLER_OVERALL_RESULT_SCORE_PERCENTAGE
        defaultSubmissionShouldBeFound("overallResultScorePercentage.greaterThan=" + SMALLER_OVERALL_RESULT_SCORE_PERCENTAGE);
    }

    @Test
    @Transactional
    void getAllSubmissionsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        // Get all the submissionList where createdBy equals to DEFAULT_CREATED_BY
        defaultSubmissionShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the submissionList where createdBy equals to UPDATED_CREATED_BY
        defaultSubmissionShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllSubmissionsByCreatedByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        // Get all the submissionList where createdBy not equals to DEFAULT_CREATED_BY
        defaultSubmissionShouldNotBeFound("createdBy.notEquals=" + DEFAULT_CREATED_BY);

        // Get all the submissionList where createdBy not equals to UPDATED_CREATED_BY
        defaultSubmissionShouldBeFound("createdBy.notEquals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllSubmissionsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        // Get all the submissionList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultSubmissionShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the submissionList where createdBy equals to UPDATED_CREATED_BY
        defaultSubmissionShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllSubmissionsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        // Get all the submissionList where createdBy is not null
        defaultSubmissionShouldBeFound("createdBy.specified=true");

        // Get all the submissionList where createdBy is null
        defaultSubmissionShouldNotBeFound("createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllSubmissionsByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        // Get all the submissionList where createdBy contains DEFAULT_CREATED_BY
        defaultSubmissionShouldBeFound("createdBy.contains=" + DEFAULT_CREATED_BY);

        // Get all the submissionList where createdBy contains UPDATED_CREATED_BY
        defaultSubmissionShouldNotBeFound("createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllSubmissionsByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        // Get all the submissionList where createdBy does not contain DEFAULT_CREATED_BY
        defaultSubmissionShouldNotBeFound("createdBy.doesNotContain=" + DEFAULT_CREATED_BY);

        // Get all the submissionList where createdBy does not contain UPDATED_CREATED_BY
        defaultSubmissionShouldBeFound("createdBy.doesNotContain=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllSubmissionsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        // Get all the submissionList where createdDate equals to DEFAULT_CREATED_DATE
        defaultSubmissionShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the submissionList where createdDate equals to UPDATED_CREATED_DATE
        defaultSubmissionShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllSubmissionsByCreatedDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        // Get all the submissionList where createdDate not equals to DEFAULT_CREATED_DATE
        defaultSubmissionShouldNotBeFound("createdDate.notEquals=" + DEFAULT_CREATED_DATE);

        // Get all the submissionList where createdDate not equals to UPDATED_CREATED_DATE
        defaultSubmissionShouldBeFound("createdDate.notEquals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllSubmissionsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        // Get all the submissionList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultSubmissionShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the submissionList where createdDate equals to UPDATED_CREATED_DATE
        defaultSubmissionShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllSubmissionsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        // Get all the submissionList where createdDate is not null
        defaultSubmissionShouldBeFound("createdDate.specified=true");

        // Get all the submissionList where createdDate is null
        defaultSubmissionShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSubmissionsByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        // Get all the submissionList where lastModifiedBy equals to DEFAULT_LAST_MODIFIED_BY
        defaultSubmissionShouldBeFound("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the submissionList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultSubmissionShouldNotBeFound("lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllSubmissionsByLastModifiedByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        // Get all the submissionList where lastModifiedBy not equals to DEFAULT_LAST_MODIFIED_BY
        defaultSubmissionShouldNotBeFound("lastModifiedBy.notEquals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the submissionList where lastModifiedBy not equals to UPDATED_LAST_MODIFIED_BY
        defaultSubmissionShouldBeFound("lastModifiedBy.notEquals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllSubmissionsByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        // Get all the submissionList where lastModifiedBy in DEFAULT_LAST_MODIFIED_BY or UPDATED_LAST_MODIFIED_BY
        defaultSubmissionShouldBeFound("lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY);

        // Get all the submissionList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultSubmissionShouldNotBeFound("lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllSubmissionsByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        // Get all the submissionList where lastModifiedBy is not null
        defaultSubmissionShouldBeFound("lastModifiedBy.specified=true");

        // Get all the submissionList where lastModifiedBy is null
        defaultSubmissionShouldNotBeFound("lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllSubmissionsByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        // Get all the submissionList where lastModifiedBy contains DEFAULT_LAST_MODIFIED_BY
        defaultSubmissionShouldBeFound("lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the submissionList where lastModifiedBy contains UPDATED_LAST_MODIFIED_BY
        defaultSubmissionShouldNotBeFound("lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllSubmissionsByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        // Get all the submissionList where lastModifiedBy does not contain DEFAULT_LAST_MODIFIED_BY
        defaultSubmissionShouldNotBeFound("lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the submissionList where lastModifiedBy does not contain UPDATED_LAST_MODIFIED_BY
        defaultSubmissionShouldBeFound("lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllSubmissionsByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        // Get all the submissionList where lastModifiedDate equals to DEFAULT_LAST_MODIFIED_DATE
        defaultSubmissionShouldBeFound("lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the submissionList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultSubmissionShouldNotBeFound("lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllSubmissionsByLastModifiedDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        // Get all the submissionList where lastModifiedDate not equals to DEFAULT_LAST_MODIFIED_DATE
        defaultSubmissionShouldNotBeFound("lastModifiedDate.notEquals=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the submissionList where lastModifiedDate not equals to UPDATED_LAST_MODIFIED_DATE
        defaultSubmissionShouldBeFound("lastModifiedDate.notEquals=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllSubmissionsByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        // Get all the submissionList where lastModifiedDate in DEFAULT_LAST_MODIFIED_DATE or UPDATED_LAST_MODIFIED_DATE
        defaultSubmissionShouldBeFound("lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE);

        // Get all the submissionList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultSubmissionShouldNotBeFound("lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllSubmissionsByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        // Get all the submissionList where lastModifiedDate is not null
        defaultSubmissionShouldBeFound("lastModifiedDate.specified=true");

        // Get all the submissionList where lastModifiedDate is null
        defaultSubmissionShouldNotBeFound("lastModifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSubmissionsBySubmissionTestCaseResultIsEqualToSomething() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);
        SubmissionTestCaseResult submissionTestCaseResult;
        if (TestUtil.findAll(em, SubmissionTestCaseResult.class).isEmpty()) {
            submissionTestCaseResult = SubmissionTestCaseResultResourceIT.createEntity(em);
            em.persist(submissionTestCaseResult);
            em.flush();
        } else {
            submissionTestCaseResult = TestUtil.findAll(em, SubmissionTestCaseResult.class).get(0);
        }
        em.persist(submissionTestCaseResult);
        em.flush();
        submission.addSubmissionTestCaseResult(submissionTestCaseResult);
        submissionRepository.saveAndFlush(submission);
        Long submissionTestCaseResultId = submissionTestCaseResult.getId();

        // Get all the submissionList where submissionTestCaseResult equals to submissionTestCaseResultId
        defaultSubmissionShouldBeFound("submissionTestCaseResultId.equals=" + submissionTestCaseResultId);

        // Get all the submissionList where submissionTestCaseResult equals to (submissionTestCaseResultId + 1)
        defaultSubmissionShouldNotBeFound("submissionTestCaseResultId.equals=" + (submissionTestCaseResultId + 1));
    }

    @Test
    @Transactional
    void getAllSubmissionsByProblemIsEqualToSomething() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);
        Problem problem;
        if (TestUtil.findAll(em, Problem.class).isEmpty()) {
            problem = ProblemResourceIT.createEntity(em);
            em.persist(problem);
            em.flush();
        } else {
            problem = TestUtil.findAll(em, Problem.class).get(0);
        }
        em.persist(problem);
        em.flush();
        submission.setProblem(problem);
        submissionRepository.saveAndFlush(submission);
        Long problemId = problem.getId();

        // Get all the submissionList where problem equals to problemId
        defaultSubmissionShouldBeFound("problemId.equals=" + problemId);

        // Get all the submissionList where problem equals to (problemId + 1)
        defaultSubmissionShouldNotBeFound("problemId.equals=" + (problemId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSubmissionShouldBeFound(String filter) throws Exception {
        restSubmissionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(submission.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].programmingLanguage").value(hasItem(DEFAULT_PROGRAMMING_LANGUAGE.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].overallResultStatus").value(hasItem(DEFAULT_OVERALL_RESULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].overallResultMessage").value(hasItem(DEFAULT_OVERALL_RESULT_MESSAGE.toString())))
            .andExpect(jsonPath("$.[*].overallResultMessageDetail").value(hasItem(DEFAULT_OVERALL_RESULT_MESSAGE_DETAIL)))
            .andExpect(jsonPath("$.[*].overallResultTries").value(hasItem(DEFAULT_OVERALL_RESULT_TRIES)))
            .andExpect(jsonPath("$.[*].overallResultScorePercentage").value(hasItem(DEFAULT_OVERALL_RESULT_SCORE_PERCENTAGE)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restSubmissionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSubmissionShouldNotBeFound(String filter) throws Exception {
        restSubmissionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSubmissionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSubmission() throws Exception {
        // Get the submission
        restSubmissionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSubmission() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        int databaseSizeBeforeUpdate = submissionRepository.findAll().size();

        // Update the submission
        Submission updatedSubmission = submissionRepository.findById(submission.getId()).get();
        // Disconnect from session so that the updates on updatedSubmission are not directly saved in db
        em.detach(updatedSubmission);
        updatedSubmission
            .name(UPDATED_NAME)
            .programmingLanguage(UPDATED_PROGRAMMING_LANGUAGE)
            .code(UPDATED_CODE)
            .overallResultStatus(UPDATED_OVERALL_RESULT_STATUS)
            .overallResultMessage(UPDATED_OVERALL_RESULT_MESSAGE)
            .overallResultMessageDetail(UPDATED_OVERALL_RESULT_MESSAGE_DETAIL)
            .overallResultTries(UPDATED_OVERALL_RESULT_TRIES)
            .overallResultScorePercentage(UPDATED_OVERALL_RESULT_SCORE_PERCENTAGE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        SubmissionDTO submissionDTO = submissionMapper.toDto(updatedSubmission);

        restSubmissionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, submissionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(submissionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Submission in the database
        List<Submission> submissionList = submissionRepository.findAll();
        assertThat(submissionList).hasSize(databaseSizeBeforeUpdate);
        Submission testSubmission = submissionList.get(submissionList.size() - 1);
        assertThat(testSubmission.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSubmission.getProgrammingLanguage()).isEqualTo(UPDATED_PROGRAMMING_LANGUAGE);
        assertThat(testSubmission.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testSubmission.getOverallResultStatus()).isEqualTo(UPDATED_OVERALL_RESULT_STATUS);
        assertThat(testSubmission.getOverallResultMessage()).isEqualTo(UPDATED_OVERALL_RESULT_MESSAGE);
        assertThat(testSubmission.getOverallResultMessageDetail()).isEqualTo(UPDATED_OVERALL_RESULT_MESSAGE_DETAIL);
        assertThat(testSubmission.getOverallResultTries()).isEqualTo(UPDATED_OVERALL_RESULT_TRIES);
        assertThat(testSubmission.getOverallResultScorePercentage()).isEqualTo(UPDATED_OVERALL_RESULT_SCORE_PERCENTAGE);
        assertThat(testSubmission.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSubmission.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testSubmission.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testSubmission.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingSubmission() throws Exception {
        int databaseSizeBeforeUpdate = submissionRepository.findAll().size();
        submission.setId(count.incrementAndGet());

        // Create the Submission
        SubmissionDTO submissionDTO = submissionMapper.toDto(submission);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubmissionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, submissionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(submissionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Submission in the database
        List<Submission> submissionList = submissionRepository.findAll();
        assertThat(submissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSubmission() throws Exception {
        int databaseSizeBeforeUpdate = submissionRepository.findAll().size();
        submission.setId(count.incrementAndGet());

        // Create the Submission
        SubmissionDTO submissionDTO = submissionMapper.toDto(submission);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubmissionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(submissionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Submission in the database
        List<Submission> submissionList = submissionRepository.findAll();
        assertThat(submissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSubmission() throws Exception {
        int databaseSizeBeforeUpdate = submissionRepository.findAll().size();
        submission.setId(count.incrementAndGet());

        // Create the Submission
        SubmissionDTO submissionDTO = submissionMapper.toDto(submission);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubmissionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(submissionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Submission in the database
        List<Submission> submissionList = submissionRepository.findAll();
        assertThat(submissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSubmissionWithPatch() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        int databaseSizeBeforeUpdate = submissionRepository.findAll().size();

        // Update the submission using partial update
        Submission partialUpdatedSubmission = new Submission();
        partialUpdatedSubmission.setId(submission.getId());

        partialUpdatedSubmission
            .name(UPDATED_NAME)
            .programmingLanguage(UPDATED_PROGRAMMING_LANGUAGE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restSubmissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubmission.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSubmission))
            )
            .andExpect(status().isOk());

        // Validate the Submission in the database
        List<Submission> submissionList = submissionRepository.findAll();
        assertThat(submissionList).hasSize(databaseSizeBeforeUpdate);
        Submission testSubmission = submissionList.get(submissionList.size() - 1);
        assertThat(testSubmission.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSubmission.getProgrammingLanguage()).isEqualTo(UPDATED_PROGRAMMING_LANGUAGE);
        assertThat(testSubmission.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testSubmission.getOverallResultStatus()).isEqualTo(DEFAULT_OVERALL_RESULT_STATUS);
        assertThat(testSubmission.getOverallResultMessage()).isEqualTo(DEFAULT_OVERALL_RESULT_MESSAGE);
        assertThat(testSubmission.getOverallResultMessageDetail()).isEqualTo(DEFAULT_OVERALL_RESULT_MESSAGE_DETAIL);
        assertThat(testSubmission.getOverallResultTries()).isEqualTo(DEFAULT_OVERALL_RESULT_TRIES);
        assertThat(testSubmission.getOverallResultScorePercentage()).isEqualTo(DEFAULT_OVERALL_RESULT_SCORE_PERCENTAGE);
        assertThat(testSubmission.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSubmission.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testSubmission.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testSubmission.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateSubmissionWithPatch() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        int databaseSizeBeforeUpdate = submissionRepository.findAll().size();

        // Update the submission using partial update
        Submission partialUpdatedSubmission = new Submission();
        partialUpdatedSubmission.setId(submission.getId());

        partialUpdatedSubmission
            .name(UPDATED_NAME)
            .programmingLanguage(UPDATED_PROGRAMMING_LANGUAGE)
            .code(UPDATED_CODE)
            .overallResultStatus(UPDATED_OVERALL_RESULT_STATUS)
            .overallResultMessage(UPDATED_OVERALL_RESULT_MESSAGE)
            .overallResultMessageDetail(UPDATED_OVERALL_RESULT_MESSAGE_DETAIL)
            .overallResultTries(UPDATED_OVERALL_RESULT_TRIES)
            .overallResultScorePercentage(UPDATED_OVERALL_RESULT_SCORE_PERCENTAGE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restSubmissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubmission.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSubmission))
            )
            .andExpect(status().isOk());

        // Validate the Submission in the database
        List<Submission> submissionList = submissionRepository.findAll();
        assertThat(submissionList).hasSize(databaseSizeBeforeUpdate);
        Submission testSubmission = submissionList.get(submissionList.size() - 1);
        assertThat(testSubmission.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSubmission.getProgrammingLanguage()).isEqualTo(UPDATED_PROGRAMMING_LANGUAGE);
        assertThat(testSubmission.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testSubmission.getOverallResultStatus()).isEqualTo(UPDATED_OVERALL_RESULT_STATUS);
        assertThat(testSubmission.getOverallResultMessage()).isEqualTo(UPDATED_OVERALL_RESULT_MESSAGE);
        assertThat(testSubmission.getOverallResultMessageDetail()).isEqualTo(UPDATED_OVERALL_RESULT_MESSAGE_DETAIL);
        assertThat(testSubmission.getOverallResultTries()).isEqualTo(UPDATED_OVERALL_RESULT_TRIES);
        assertThat(testSubmission.getOverallResultScorePercentage()).isEqualTo(UPDATED_OVERALL_RESULT_SCORE_PERCENTAGE);
        assertThat(testSubmission.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSubmission.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testSubmission.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testSubmission.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingSubmission() throws Exception {
        int databaseSizeBeforeUpdate = submissionRepository.findAll().size();
        submission.setId(count.incrementAndGet());

        // Create the Submission
        SubmissionDTO submissionDTO = submissionMapper.toDto(submission);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubmissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, submissionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(submissionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Submission in the database
        List<Submission> submissionList = submissionRepository.findAll();
        assertThat(submissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSubmission() throws Exception {
        int databaseSizeBeforeUpdate = submissionRepository.findAll().size();
        submission.setId(count.incrementAndGet());

        // Create the Submission
        SubmissionDTO submissionDTO = submissionMapper.toDto(submission);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubmissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(submissionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Submission in the database
        List<Submission> submissionList = submissionRepository.findAll();
        assertThat(submissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSubmission() throws Exception {
        int databaseSizeBeforeUpdate = submissionRepository.findAll().size();
        submission.setId(count.incrementAndGet());

        // Create the Submission
        SubmissionDTO submissionDTO = submissionMapper.toDto(submission);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubmissionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(submissionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Submission in the database
        List<Submission> submissionList = submissionRepository.findAll();
        assertThat(submissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSubmission() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        int databaseSizeBeforeDelete = submissionRepository.findAll().size();

        // Delete the submission
        restSubmissionMockMvc
            .perform(delete(ENTITY_API_URL_ID, submission.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Submission> submissionList = submissionRepository.findAll();
        assertThat(submissionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
