package com.kevin.cpoj.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.kevin.cpoj.IntegrationTest;
import com.kevin.cpoj.domain.SubmissionTestCaseResult;
import com.kevin.cpoj.domain.enumeration.TestCaseResultMessage;
import com.kevin.cpoj.repository.SubmissionTestCaseResultRepository;
import com.kevin.cpoj.service.dto.SubmissionTestCaseResultDTO;
import com.kevin.cpoj.service.mapper.SubmissionTestCaseResultMapper;
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
 * Integration tests for the {@link SubmissionTestCaseResultResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SubmissionTestCaseResultResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final TestCaseResultMessage DEFAULT_RESULT_MESSAGE = TestCaseResultMessage.NA;
    private static final TestCaseResultMessage UPDATED_RESULT_MESSAGE = TestCaseResultMessage.PASS;

    private static final String DEFAULT_RESULT_MESSAGE_DETAIL = "AAAAAAAAAA";
    private static final String UPDATED_RESULT_MESSAGE_DETAIL = "BBBBBBBBBB";

    private static final Integer DEFAULT_ELAPSED_TIME_IN_MS = 0;
    private static final Integer UPDATED_ELAPSED_TIME_IN_MS = 1;

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/submission-test-case-results";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SubmissionTestCaseResultRepository submissionTestCaseResultRepository;

    @Autowired
    private SubmissionTestCaseResultMapper submissionTestCaseResultMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSubmissionTestCaseResultMockMvc;

    private SubmissionTestCaseResult submissionTestCaseResult;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubmissionTestCaseResult createEntity(EntityManager em) {
        SubmissionTestCaseResult submissionTestCaseResult = new SubmissionTestCaseResult()
            .name(DEFAULT_NAME)
            .resultMessage(DEFAULT_RESULT_MESSAGE)
            .resultMessageDetail(DEFAULT_RESULT_MESSAGE_DETAIL)
            .elapsedTimeInMS(DEFAULT_ELAPSED_TIME_IN_MS)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return submissionTestCaseResult;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubmissionTestCaseResult createUpdatedEntity(EntityManager em) {
        SubmissionTestCaseResult submissionTestCaseResult = new SubmissionTestCaseResult()
            .name(UPDATED_NAME)
            .resultMessage(UPDATED_RESULT_MESSAGE)
            .resultMessageDetail(UPDATED_RESULT_MESSAGE_DETAIL)
            .elapsedTimeInMS(UPDATED_ELAPSED_TIME_IN_MS)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return submissionTestCaseResult;
    }

    @BeforeEach
    public void initTest() {
        submissionTestCaseResult = createEntity(em);
    }

    @Test
    @Transactional
    void createSubmissionTestCaseResult() throws Exception {
        int databaseSizeBeforeCreate = submissionTestCaseResultRepository.findAll().size();
        // Create the SubmissionTestCaseResult
        SubmissionTestCaseResultDTO submissionTestCaseResultDTO = submissionTestCaseResultMapper.toDto(submissionTestCaseResult);
        restSubmissionTestCaseResultMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(submissionTestCaseResultDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SubmissionTestCaseResult in the database
        List<SubmissionTestCaseResult> submissionTestCaseResultList = submissionTestCaseResultRepository.findAll();
        assertThat(submissionTestCaseResultList).hasSize(databaseSizeBeforeCreate + 1);
        SubmissionTestCaseResult testSubmissionTestCaseResult = submissionTestCaseResultList.get(submissionTestCaseResultList.size() - 1);
        assertThat(testSubmissionTestCaseResult.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSubmissionTestCaseResult.getResultMessage()).isEqualTo(DEFAULT_RESULT_MESSAGE);
        assertThat(testSubmissionTestCaseResult.getResultMessageDetail()).isEqualTo(DEFAULT_RESULT_MESSAGE_DETAIL);
        assertThat(testSubmissionTestCaseResult.getElapsedTimeInMS()).isEqualTo(DEFAULT_ELAPSED_TIME_IN_MS);
        assertThat(testSubmissionTestCaseResult.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testSubmissionTestCaseResult.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testSubmissionTestCaseResult.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testSubmissionTestCaseResult.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void createSubmissionTestCaseResultWithExistingId() throws Exception {
        // Create the SubmissionTestCaseResult with an existing ID
        submissionTestCaseResult.setId(1L);
        SubmissionTestCaseResultDTO submissionTestCaseResultDTO = submissionTestCaseResultMapper.toDto(submissionTestCaseResult);

        int databaseSizeBeforeCreate = submissionTestCaseResultRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubmissionTestCaseResultMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(submissionTestCaseResultDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubmissionTestCaseResult in the database
        List<SubmissionTestCaseResult> submissionTestCaseResultList = submissionTestCaseResultRepository.findAll();
        assertThat(submissionTestCaseResultList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = submissionTestCaseResultRepository.findAll().size();
        // set the field null
        submissionTestCaseResult.setName(null);

        // Create the SubmissionTestCaseResult, which fails.
        SubmissionTestCaseResultDTO submissionTestCaseResultDTO = submissionTestCaseResultMapper.toDto(submissionTestCaseResult);

        restSubmissionTestCaseResultMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(submissionTestCaseResultDTO))
            )
            .andExpect(status().isBadRequest());

        List<SubmissionTestCaseResult> submissionTestCaseResultList = submissionTestCaseResultRepository.findAll();
        assertThat(submissionTestCaseResultList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkResultMessageIsRequired() throws Exception {
        int databaseSizeBeforeTest = submissionTestCaseResultRepository.findAll().size();
        // set the field null
        submissionTestCaseResult.setResultMessage(null);

        // Create the SubmissionTestCaseResult, which fails.
        SubmissionTestCaseResultDTO submissionTestCaseResultDTO = submissionTestCaseResultMapper.toDto(submissionTestCaseResult);

        restSubmissionTestCaseResultMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(submissionTestCaseResultDTO))
            )
            .andExpect(status().isBadRequest());

        List<SubmissionTestCaseResult> submissionTestCaseResultList = submissionTestCaseResultRepository.findAll();
        assertThat(submissionTestCaseResultList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkElapsedTimeInMSIsRequired() throws Exception {
        int databaseSizeBeforeTest = submissionTestCaseResultRepository.findAll().size();
        // set the field null
        submissionTestCaseResult.setElapsedTimeInMS(null);

        // Create the SubmissionTestCaseResult, which fails.
        SubmissionTestCaseResultDTO submissionTestCaseResultDTO = submissionTestCaseResultMapper.toDto(submissionTestCaseResult);

        restSubmissionTestCaseResultMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(submissionTestCaseResultDTO))
            )
            .andExpect(status().isBadRequest());

        List<SubmissionTestCaseResult> submissionTestCaseResultList = submissionTestCaseResultRepository.findAll();
        assertThat(submissionTestCaseResultList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedByIsRequired() throws Exception {
        int databaseSizeBeforeTest = submissionTestCaseResultRepository.findAll().size();
        // set the field null
        submissionTestCaseResult.setCreatedBy(null);

        // Create the SubmissionTestCaseResult, which fails.
        SubmissionTestCaseResultDTO submissionTestCaseResultDTO = submissionTestCaseResultMapper.toDto(submissionTestCaseResult);

        restSubmissionTestCaseResultMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(submissionTestCaseResultDTO))
            )
            .andExpect(status().isBadRequest());

        List<SubmissionTestCaseResult> submissionTestCaseResultList = submissionTestCaseResultRepository.findAll();
        assertThat(submissionTestCaseResultList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastModifiedByIsRequired() throws Exception {
        int databaseSizeBeforeTest = submissionTestCaseResultRepository.findAll().size();
        // set the field null
        submissionTestCaseResult.setLastModifiedBy(null);

        // Create the SubmissionTestCaseResult, which fails.
        SubmissionTestCaseResultDTO submissionTestCaseResultDTO = submissionTestCaseResultMapper.toDto(submissionTestCaseResult);

        restSubmissionTestCaseResultMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(submissionTestCaseResultDTO))
            )
            .andExpect(status().isBadRequest());

        List<SubmissionTestCaseResult> submissionTestCaseResultList = submissionTestCaseResultRepository.findAll();
        assertThat(submissionTestCaseResultList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSubmissionTestCaseResults() throws Exception {
        // Initialize the database
        submissionTestCaseResultRepository.saveAndFlush(submissionTestCaseResult);

        // Get all the submissionTestCaseResultList
        restSubmissionTestCaseResultMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(submissionTestCaseResult.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].resultMessage").value(hasItem(DEFAULT_RESULT_MESSAGE.toString())))
            .andExpect(jsonPath("$.[*].resultMessageDetail").value(hasItem(DEFAULT_RESULT_MESSAGE_DETAIL)))
            .andExpect(jsonPath("$.[*].elapsedTimeInMS").value(hasItem(DEFAULT_ELAPSED_TIME_IN_MS)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getSubmissionTestCaseResult() throws Exception {
        // Initialize the database
        submissionTestCaseResultRepository.saveAndFlush(submissionTestCaseResult);

        // Get the submissionTestCaseResult
        restSubmissionTestCaseResultMockMvc
            .perform(get(ENTITY_API_URL_ID, submissionTestCaseResult.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(submissionTestCaseResult.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.resultMessage").value(DEFAULT_RESULT_MESSAGE.toString()))
            .andExpect(jsonPath("$.resultMessageDetail").value(DEFAULT_RESULT_MESSAGE_DETAIL))
            .andExpect(jsonPath("$.elapsedTimeInMS").value(DEFAULT_ELAPSED_TIME_IN_MS))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingSubmissionTestCaseResult() throws Exception {
        // Get the submissionTestCaseResult
        restSubmissionTestCaseResultMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSubmissionTestCaseResult() throws Exception {
        // Initialize the database
        submissionTestCaseResultRepository.saveAndFlush(submissionTestCaseResult);

        int databaseSizeBeforeUpdate = submissionTestCaseResultRepository.findAll().size();

        // Update the submissionTestCaseResult
        SubmissionTestCaseResult updatedSubmissionTestCaseResult = submissionTestCaseResultRepository
            .findById(submissionTestCaseResult.getId())
            .get();
        // Disconnect from session so that the updates on updatedSubmissionTestCaseResult are not directly saved in db
        em.detach(updatedSubmissionTestCaseResult);
        updatedSubmissionTestCaseResult
            .name(UPDATED_NAME)
            .resultMessage(UPDATED_RESULT_MESSAGE)
            .resultMessageDetail(UPDATED_RESULT_MESSAGE_DETAIL)
            .elapsedTimeInMS(UPDATED_ELAPSED_TIME_IN_MS)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        SubmissionTestCaseResultDTO submissionTestCaseResultDTO = submissionTestCaseResultMapper.toDto(updatedSubmissionTestCaseResult);

        restSubmissionTestCaseResultMockMvc
            .perform(
                put(ENTITY_API_URL_ID, submissionTestCaseResultDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(submissionTestCaseResultDTO))
            )
            .andExpect(status().isOk());

        // Validate the SubmissionTestCaseResult in the database
        List<SubmissionTestCaseResult> submissionTestCaseResultList = submissionTestCaseResultRepository.findAll();
        assertThat(submissionTestCaseResultList).hasSize(databaseSizeBeforeUpdate);
        SubmissionTestCaseResult testSubmissionTestCaseResult = submissionTestCaseResultList.get(submissionTestCaseResultList.size() - 1);
        assertThat(testSubmissionTestCaseResult.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSubmissionTestCaseResult.getResultMessage()).isEqualTo(UPDATED_RESULT_MESSAGE);
        assertThat(testSubmissionTestCaseResult.getResultMessageDetail()).isEqualTo(UPDATED_RESULT_MESSAGE_DETAIL);
        assertThat(testSubmissionTestCaseResult.getElapsedTimeInMS()).isEqualTo(UPDATED_ELAPSED_TIME_IN_MS);
        assertThat(testSubmissionTestCaseResult.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSubmissionTestCaseResult.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testSubmissionTestCaseResult.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testSubmissionTestCaseResult.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingSubmissionTestCaseResult() throws Exception {
        int databaseSizeBeforeUpdate = submissionTestCaseResultRepository.findAll().size();
        submissionTestCaseResult.setId(count.incrementAndGet());

        // Create the SubmissionTestCaseResult
        SubmissionTestCaseResultDTO submissionTestCaseResultDTO = submissionTestCaseResultMapper.toDto(submissionTestCaseResult);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubmissionTestCaseResultMockMvc
            .perform(
                put(ENTITY_API_URL_ID, submissionTestCaseResultDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(submissionTestCaseResultDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubmissionTestCaseResult in the database
        List<SubmissionTestCaseResult> submissionTestCaseResultList = submissionTestCaseResultRepository.findAll();
        assertThat(submissionTestCaseResultList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSubmissionTestCaseResult() throws Exception {
        int databaseSizeBeforeUpdate = submissionTestCaseResultRepository.findAll().size();
        submissionTestCaseResult.setId(count.incrementAndGet());

        // Create the SubmissionTestCaseResult
        SubmissionTestCaseResultDTO submissionTestCaseResultDTO = submissionTestCaseResultMapper.toDto(submissionTestCaseResult);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubmissionTestCaseResultMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(submissionTestCaseResultDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubmissionTestCaseResult in the database
        List<SubmissionTestCaseResult> submissionTestCaseResultList = submissionTestCaseResultRepository.findAll();
        assertThat(submissionTestCaseResultList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSubmissionTestCaseResult() throws Exception {
        int databaseSizeBeforeUpdate = submissionTestCaseResultRepository.findAll().size();
        submissionTestCaseResult.setId(count.incrementAndGet());

        // Create the SubmissionTestCaseResult
        SubmissionTestCaseResultDTO submissionTestCaseResultDTO = submissionTestCaseResultMapper.toDto(submissionTestCaseResult);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubmissionTestCaseResultMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(submissionTestCaseResultDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SubmissionTestCaseResult in the database
        List<SubmissionTestCaseResult> submissionTestCaseResultList = submissionTestCaseResultRepository.findAll();
        assertThat(submissionTestCaseResultList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSubmissionTestCaseResultWithPatch() throws Exception {
        // Initialize the database
        submissionTestCaseResultRepository.saveAndFlush(submissionTestCaseResult);

        int databaseSizeBeforeUpdate = submissionTestCaseResultRepository.findAll().size();

        // Update the submissionTestCaseResult using partial update
        SubmissionTestCaseResult partialUpdatedSubmissionTestCaseResult = new SubmissionTestCaseResult();
        partialUpdatedSubmissionTestCaseResult.setId(submissionTestCaseResult.getId());

        partialUpdatedSubmissionTestCaseResult
            .resultMessage(UPDATED_RESULT_MESSAGE)
            .resultMessageDetail(UPDATED_RESULT_MESSAGE_DETAIL)
            .createdDate(UPDATED_CREATED_DATE);

        restSubmissionTestCaseResultMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubmissionTestCaseResult.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSubmissionTestCaseResult))
            )
            .andExpect(status().isOk());

        // Validate the SubmissionTestCaseResult in the database
        List<SubmissionTestCaseResult> submissionTestCaseResultList = submissionTestCaseResultRepository.findAll();
        assertThat(submissionTestCaseResultList).hasSize(databaseSizeBeforeUpdate);
        SubmissionTestCaseResult testSubmissionTestCaseResult = submissionTestCaseResultList.get(submissionTestCaseResultList.size() - 1);
        assertThat(testSubmissionTestCaseResult.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSubmissionTestCaseResult.getResultMessage()).isEqualTo(UPDATED_RESULT_MESSAGE);
        assertThat(testSubmissionTestCaseResult.getResultMessageDetail()).isEqualTo(UPDATED_RESULT_MESSAGE_DETAIL);
        assertThat(testSubmissionTestCaseResult.getElapsedTimeInMS()).isEqualTo(DEFAULT_ELAPSED_TIME_IN_MS);
        assertThat(testSubmissionTestCaseResult.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testSubmissionTestCaseResult.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testSubmissionTestCaseResult.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testSubmissionTestCaseResult.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateSubmissionTestCaseResultWithPatch() throws Exception {
        // Initialize the database
        submissionTestCaseResultRepository.saveAndFlush(submissionTestCaseResult);

        int databaseSizeBeforeUpdate = submissionTestCaseResultRepository.findAll().size();

        // Update the submissionTestCaseResult using partial update
        SubmissionTestCaseResult partialUpdatedSubmissionTestCaseResult = new SubmissionTestCaseResult();
        partialUpdatedSubmissionTestCaseResult.setId(submissionTestCaseResult.getId());

        partialUpdatedSubmissionTestCaseResult
            .name(UPDATED_NAME)
            .resultMessage(UPDATED_RESULT_MESSAGE)
            .resultMessageDetail(UPDATED_RESULT_MESSAGE_DETAIL)
            .elapsedTimeInMS(UPDATED_ELAPSED_TIME_IN_MS)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restSubmissionTestCaseResultMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubmissionTestCaseResult.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSubmissionTestCaseResult))
            )
            .andExpect(status().isOk());

        // Validate the SubmissionTestCaseResult in the database
        List<SubmissionTestCaseResult> submissionTestCaseResultList = submissionTestCaseResultRepository.findAll();
        assertThat(submissionTestCaseResultList).hasSize(databaseSizeBeforeUpdate);
        SubmissionTestCaseResult testSubmissionTestCaseResult = submissionTestCaseResultList.get(submissionTestCaseResultList.size() - 1);
        assertThat(testSubmissionTestCaseResult.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSubmissionTestCaseResult.getResultMessage()).isEqualTo(UPDATED_RESULT_MESSAGE);
        assertThat(testSubmissionTestCaseResult.getResultMessageDetail()).isEqualTo(UPDATED_RESULT_MESSAGE_DETAIL);
        assertThat(testSubmissionTestCaseResult.getElapsedTimeInMS()).isEqualTo(UPDATED_ELAPSED_TIME_IN_MS);
        assertThat(testSubmissionTestCaseResult.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSubmissionTestCaseResult.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testSubmissionTestCaseResult.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testSubmissionTestCaseResult.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingSubmissionTestCaseResult() throws Exception {
        int databaseSizeBeforeUpdate = submissionTestCaseResultRepository.findAll().size();
        submissionTestCaseResult.setId(count.incrementAndGet());

        // Create the SubmissionTestCaseResult
        SubmissionTestCaseResultDTO submissionTestCaseResultDTO = submissionTestCaseResultMapper.toDto(submissionTestCaseResult);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubmissionTestCaseResultMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, submissionTestCaseResultDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(submissionTestCaseResultDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubmissionTestCaseResult in the database
        List<SubmissionTestCaseResult> submissionTestCaseResultList = submissionTestCaseResultRepository.findAll();
        assertThat(submissionTestCaseResultList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSubmissionTestCaseResult() throws Exception {
        int databaseSizeBeforeUpdate = submissionTestCaseResultRepository.findAll().size();
        submissionTestCaseResult.setId(count.incrementAndGet());

        // Create the SubmissionTestCaseResult
        SubmissionTestCaseResultDTO submissionTestCaseResultDTO = submissionTestCaseResultMapper.toDto(submissionTestCaseResult);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubmissionTestCaseResultMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(submissionTestCaseResultDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubmissionTestCaseResult in the database
        List<SubmissionTestCaseResult> submissionTestCaseResultList = submissionTestCaseResultRepository.findAll();
        assertThat(submissionTestCaseResultList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSubmissionTestCaseResult() throws Exception {
        int databaseSizeBeforeUpdate = submissionTestCaseResultRepository.findAll().size();
        submissionTestCaseResult.setId(count.incrementAndGet());

        // Create the SubmissionTestCaseResult
        SubmissionTestCaseResultDTO submissionTestCaseResultDTO = submissionTestCaseResultMapper.toDto(submissionTestCaseResult);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubmissionTestCaseResultMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(submissionTestCaseResultDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SubmissionTestCaseResult in the database
        List<SubmissionTestCaseResult> submissionTestCaseResultList = submissionTestCaseResultRepository.findAll();
        assertThat(submissionTestCaseResultList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSubmissionTestCaseResult() throws Exception {
        // Initialize the database
        submissionTestCaseResultRepository.saveAndFlush(submissionTestCaseResult);

        int databaseSizeBeforeDelete = submissionTestCaseResultRepository.findAll().size();

        // Delete the submissionTestCaseResult
        restSubmissionTestCaseResultMockMvc
            .perform(delete(ENTITY_API_URL_ID, submissionTestCaseResult.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SubmissionTestCaseResult> submissionTestCaseResultList = submissionTestCaseResultRepository.findAll();
        assertThat(submissionTestCaseResultList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
