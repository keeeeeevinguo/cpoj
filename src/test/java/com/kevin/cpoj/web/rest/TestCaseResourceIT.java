package com.kevin.cpoj.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.kevin.cpoj.IntegrationTest;
import com.kevin.cpoj.domain.TestCase;
import com.kevin.cpoj.repository.TestCaseRepository;
import com.kevin.cpoj.service.dto.TestCaseDTO;
import com.kevin.cpoj.service.mapper.TestCaseMapper;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link TestCaseResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TestCaseResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_TIME_LIMIT_IN_MS = 10;
    private static final Integer UPDATED_TIME_LIMIT_IN_MS = 11;

    private static final Integer DEFAULT_MEMORY_LIMIT_IN_MB = 100;
    private static final Integer UPDATED_MEMORY_LIMIT_IN_MB = 101;

    private static final Integer DEFAULT_WEIGHT_PERCENTAGE = 1;
    private static final Integer UPDATED_WEIGHT_PERCENTAGE = 2;

    private static final String DEFAULT_INPUT_DATA = "AAAAAAAAAA";
    private static final String UPDATED_INPUT_DATA = "BBBBBBBBBB";

    private static final String DEFAULT_OUTPUT_DATA = "AAAAAAAAAA";
    private static final String UPDATED_OUTPUT_DATA = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/test-cases";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TestCaseRepository testCaseRepository;

    @Autowired
    private TestCaseMapper testCaseMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTestCaseMockMvc;

    private TestCase testCase;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestCase createEntity(EntityManager em) {
        TestCase testCase = new TestCase()
            .name(DEFAULT_NAME)
            .timeLimitInMS(DEFAULT_TIME_LIMIT_IN_MS)
            .memoryLimitInMB(DEFAULT_MEMORY_LIMIT_IN_MB)
            .weightPercentage(DEFAULT_WEIGHT_PERCENTAGE)
            .inputData(DEFAULT_INPUT_DATA)
            .outputData(DEFAULT_OUTPUT_DATA)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return testCase;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestCase createUpdatedEntity(EntityManager em) {
        TestCase testCase = new TestCase()
            .name(UPDATED_NAME)
            .timeLimitInMS(UPDATED_TIME_LIMIT_IN_MS)
            .memoryLimitInMB(UPDATED_MEMORY_LIMIT_IN_MB)
            .weightPercentage(UPDATED_WEIGHT_PERCENTAGE)
            .inputData(UPDATED_INPUT_DATA)
            .outputData(UPDATED_OUTPUT_DATA)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return testCase;
    }

    @BeforeEach
    public void initTest() {
        testCase = createEntity(em);
    }

    @Test
    @Transactional
    void createTestCase() throws Exception {
        int databaseSizeBeforeCreate = testCaseRepository.findAll().size();
        // Create the TestCase
        TestCaseDTO testCaseDTO = testCaseMapper.toDto(testCase);
        restTestCaseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testCaseDTO)))
            .andExpect(status().isCreated());

        // Validate the TestCase in the database
        List<TestCase> testCaseList = testCaseRepository.findAll();
        assertThat(testCaseList).hasSize(databaseSizeBeforeCreate + 1);
        TestCase testTestCase = testCaseList.get(testCaseList.size() - 1);
        assertThat(testTestCase.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTestCase.getTimeLimitInMS()).isEqualTo(DEFAULT_TIME_LIMIT_IN_MS);
        assertThat(testTestCase.getMemoryLimitInMB()).isEqualTo(DEFAULT_MEMORY_LIMIT_IN_MB);
        assertThat(testTestCase.getWeightPercentage()).isEqualTo(DEFAULT_WEIGHT_PERCENTAGE);
        assertThat(testTestCase.getInputData()).isEqualTo(DEFAULT_INPUT_DATA);
        assertThat(testTestCase.getOutputData()).isEqualTo(DEFAULT_OUTPUT_DATA);
        assertThat(testTestCase.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testTestCase.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testTestCase.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testTestCase.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void createTestCaseWithExistingId() throws Exception {
        // Create the TestCase with an existing ID
        testCase.setId(1L);
        TestCaseDTO testCaseDTO = testCaseMapper.toDto(testCase);

        int databaseSizeBeforeCreate = testCaseRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTestCaseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testCaseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TestCase in the database
        List<TestCase> testCaseList = testCaseRepository.findAll();
        assertThat(testCaseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = testCaseRepository.findAll().size();
        // set the field null
        testCase.setName(null);

        // Create the TestCase, which fails.
        TestCaseDTO testCaseDTO = testCaseMapper.toDto(testCase);

        restTestCaseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testCaseDTO)))
            .andExpect(status().isBadRequest());

        List<TestCase> testCaseList = testCaseRepository.findAll();
        assertThat(testCaseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTimeLimitInMSIsRequired() throws Exception {
        int databaseSizeBeforeTest = testCaseRepository.findAll().size();
        // set the field null
        testCase.setTimeLimitInMS(null);

        // Create the TestCase, which fails.
        TestCaseDTO testCaseDTO = testCaseMapper.toDto(testCase);

        restTestCaseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testCaseDTO)))
            .andExpect(status().isBadRequest());

        List<TestCase> testCaseList = testCaseRepository.findAll();
        assertThat(testCaseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMemoryLimitInMBIsRequired() throws Exception {
        int databaseSizeBeforeTest = testCaseRepository.findAll().size();
        // set the field null
        testCase.setMemoryLimitInMB(null);

        // Create the TestCase, which fails.
        TestCaseDTO testCaseDTO = testCaseMapper.toDto(testCase);

        restTestCaseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testCaseDTO)))
            .andExpect(status().isBadRequest());

        List<TestCase> testCaseList = testCaseRepository.findAll();
        assertThat(testCaseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkWeightPercentageIsRequired() throws Exception {
        int databaseSizeBeforeTest = testCaseRepository.findAll().size();
        // set the field null
        testCase.setWeightPercentage(null);

        // Create the TestCase, which fails.
        TestCaseDTO testCaseDTO = testCaseMapper.toDto(testCase);

        restTestCaseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testCaseDTO)))
            .andExpect(status().isBadRequest());

        List<TestCase> testCaseList = testCaseRepository.findAll();
        assertThat(testCaseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedByIsRequired() throws Exception {
        int databaseSizeBeforeTest = testCaseRepository.findAll().size();
        // set the field null
        testCase.setCreatedBy(null);

        // Create the TestCase, which fails.
        TestCaseDTO testCaseDTO = testCaseMapper.toDto(testCase);

        restTestCaseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testCaseDTO)))
            .andExpect(status().isBadRequest());

        List<TestCase> testCaseList = testCaseRepository.findAll();
        assertThat(testCaseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastModifiedByIsRequired() throws Exception {
        int databaseSizeBeforeTest = testCaseRepository.findAll().size();
        // set the field null
        testCase.setLastModifiedBy(null);

        // Create the TestCase, which fails.
        TestCaseDTO testCaseDTO = testCaseMapper.toDto(testCase);

        restTestCaseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testCaseDTO)))
            .andExpect(status().isBadRequest());

        List<TestCase> testCaseList = testCaseRepository.findAll();
        assertThat(testCaseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTestCases() throws Exception {
        // Initialize the database
        testCaseRepository.saveAndFlush(testCase);

        // Get all the testCaseList
        restTestCaseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testCase.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].timeLimitInMS").value(hasItem(DEFAULT_TIME_LIMIT_IN_MS)))
            .andExpect(jsonPath("$.[*].memoryLimitInMB").value(hasItem(DEFAULT_MEMORY_LIMIT_IN_MB)))
            .andExpect(jsonPath("$.[*].weightPercentage").value(hasItem(DEFAULT_WEIGHT_PERCENTAGE)))
            .andExpect(jsonPath("$.[*].inputData").value(hasItem(DEFAULT_INPUT_DATA.toString())))
            .andExpect(jsonPath("$.[*].outputData").value(hasItem(DEFAULT_OUTPUT_DATA.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getTestCase() throws Exception {
        // Initialize the database
        testCaseRepository.saveAndFlush(testCase);

        // Get the testCase
        restTestCaseMockMvc
            .perform(get(ENTITY_API_URL_ID, testCase.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(testCase.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.timeLimitInMS").value(DEFAULT_TIME_LIMIT_IN_MS))
            .andExpect(jsonPath("$.memoryLimitInMB").value(DEFAULT_MEMORY_LIMIT_IN_MB))
            .andExpect(jsonPath("$.weightPercentage").value(DEFAULT_WEIGHT_PERCENTAGE))
            .andExpect(jsonPath("$.inputData").value(DEFAULT_INPUT_DATA.toString()))
            .andExpect(jsonPath("$.outputData").value(DEFAULT_OUTPUT_DATA.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingTestCase() throws Exception {
        // Get the testCase
        restTestCaseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTestCase() throws Exception {
        // Initialize the database
        testCaseRepository.saveAndFlush(testCase);

        int databaseSizeBeforeUpdate = testCaseRepository.findAll().size();

        // Update the testCase
        TestCase updatedTestCase = testCaseRepository.findById(testCase.getId()).get();
        // Disconnect from session so that the updates on updatedTestCase are not directly saved in db
        em.detach(updatedTestCase);
        updatedTestCase
            .name(UPDATED_NAME)
            .timeLimitInMS(UPDATED_TIME_LIMIT_IN_MS)
            .memoryLimitInMB(UPDATED_MEMORY_LIMIT_IN_MB)
            .weightPercentage(UPDATED_WEIGHT_PERCENTAGE)
            .inputData(UPDATED_INPUT_DATA)
            .outputData(UPDATED_OUTPUT_DATA)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        TestCaseDTO testCaseDTO = testCaseMapper.toDto(updatedTestCase);

        restTestCaseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, testCaseDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testCaseDTO))
            )
            .andExpect(status().isOk());

        // Validate the TestCase in the database
        List<TestCase> testCaseList = testCaseRepository.findAll();
        assertThat(testCaseList).hasSize(databaseSizeBeforeUpdate);
        TestCase testTestCase = testCaseList.get(testCaseList.size() - 1);
        assertThat(testTestCase.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTestCase.getTimeLimitInMS()).isEqualTo(UPDATED_TIME_LIMIT_IN_MS);
        assertThat(testTestCase.getMemoryLimitInMB()).isEqualTo(UPDATED_MEMORY_LIMIT_IN_MB);
        assertThat(testTestCase.getWeightPercentage()).isEqualTo(UPDATED_WEIGHT_PERCENTAGE);
        assertThat(testTestCase.getInputData()).isEqualTo(UPDATED_INPUT_DATA);
        assertThat(testTestCase.getOutputData()).isEqualTo(UPDATED_OUTPUT_DATA);
        assertThat(testTestCase.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testTestCase.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testTestCase.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testTestCase.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingTestCase() throws Exception {
        int databaseSizeBeforeUpdate = testCaseRepository.findAll().size();
        testCase.setId(count.incrementAndGet());

        // Create the TestCase
        TestCaseDTO testCaseDTO = testCaseMapper.toDto(testCase);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestCaseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, testCaseDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testCaseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestCase in the database
        List<TestCase> testCaseList = testCaseRepository.findAll();
        assertThat(testCaseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTestCase() throws Exception {
        int databaseSizeBeforeUpdate = testCaseRepository.findAll().size();
        testCase.setId(count.incrementAndGet());

        // Create the TestCase
        TestCaseDTO testCaseDTO = testCaseMapper.toDto(testCase);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestCaseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testCaseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestCase in the database
        List<TestCase> testCaseList = testCaseRepository.findAll();
        assertThat(testCaseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTestCase() throws Exception {
        int databaseSizeBeforeUpdate = testCaseRepository.findAll().size();
        testCase.setId(count.incrementAndGet());

        // Create the TestCase
        TestCaseDTO testCaseDTO = testCaseMapper.toDto(testCase);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestCaseMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testCaseDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestCase in the database
        List<TestCase> testCaseList = testCaseRepository.findAll();
        assertThat(testCaseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTestCaseWithPatch() throws Exception {
        // Initialize the database
        testCaseRepository.saveAndFlush(testCase);

        int databaseSizeBeforeUpdate = testCaseRepository.findAll().size();

        // Update the testCase using partial update
        TestCase partialUpdatedTestCase = new TestCase();
        partialUpdatedTestCase.setId(testCase.getId());

        partialUpdatedTestCase
            .name(UPDATED_NAME)
            .timeLimitInMS(UPDATED_TIME_LIMIT_IN_MS)
            .inputData(UPDATED_INPUT_DATA)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restTestCaseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestCase.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestCase))
            )
            .andExpect(status().isOk());

        // Validate the TestCase in the database
        List<TestCase> testCaseList = testCaseRepository.findAll();
        assertThat(testCaseList).hasSize(databaseSizeBeforeUpdate);
        TestCase testTestCase = testCaseList.get(testCaseList.size() - 1);
        assertThat(testTestCase.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTestCase.getTimeLimitInMS()).isEqualTo(UPDATED_TIME_LIMIT_IN_MS);
        assertThat(testTestCase.getMemoryLimitInMB()).isEqualTo(DEFAULT_MEMORY_LIMIT_IN_MB);
        assertThat(testTestCase.getWeightPercentage()).isEqualTo(DEFAULT_WEIGHT_PERCENTAGE);
        assertThat(testTestCase.getInputData()).isEqualTo(UPDATED_INPUT_DATA);
        assertThat(testTestCase.getOutputData()).isEqualTo(DEFAULT_OUTPUT_DATA);
        assertThat(testTestCase.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testTestCase.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testTestCase.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testTestCase.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateTestCaseWithPatch() throws Exception {
        // Initialize the database
        testCaseRepository.saveAndFlush(testCase);

        int databaseSizeBeforeUpdate = testCaseRepository.findAll().size();

        // Update the testCase using partial update
        TestCase partialUpdatedTestCase = new TestCase();
        partialUpdatedTestCase.setId(testCase.getId());

        partialUpdatedTestCase
            .name(UPDATED_NAME)
            .timeLimitInMS(UPDATED_TIME_LIMIT_IN_MS)
            .memoryLimitInMB(UPDATED_MEMORY_LIMIT_IN_MB)
            .weightPercentage(UPDATED_WEIGHT_PERCENTAGE)
            .inputData(UPDATED_INPUT_DATA)
            .outputData(UPDATED_OUTPUT_DATA)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restTestCaseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestCase.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestCase))
            )
            .andExpect(status().isOk());

        // Validate the TestCase in the database
        List<TestCase> testCaseList = testCaseRepository.findAll();
        assertThat(testCaseList).hasSize(databaseSizeBeforeUpdate);
        TestCase testTestCase = testCaseList.get(testCaseList.size() - 1);
        assertThat(testTestCase.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTestCase.getTimeLimitInMS()).isEqualTo(UPDATED_TIME_LIMIT_IN_MS);
        assertThat(testTestCase.getMemoryLimitInMB()).isEqualTo(UPDATED_MEMORY_LIMIT_IN_MB);
        assertThat(testTestCase.getWeightPercentage()).isEqualTo(UPDATED_WEIGHT_PERCENTAGE);
        assertThat(testTestCase.getInputData()).isEqualTo(UPDATED_INPUT_DATA);
        assertThat(testTestCase.getOutputData()).isEqualTo(UPDATED_OUTPUT_DATA);
        assertThat(testTestCase.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testTestCase.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testTestCase.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testTestCase.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingTestCase() throws Exception {
        int databaseSizeBeforeUpdate = testCaseRepository.findAll().size();
        testCase.setId(count.incrementAndGet());

        // Create the TestCase
        TestCaseDTO testCaseDTO = testCaseMapper.toDto(testCase);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestCaseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, testCaseDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testCaseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestCase in the database
        List<TestCase> testCaseList = testCaseRepository.findAll();
        assertThat(testCaseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTestCase() throws Exception {
        int databaseSizeBeforeUpdate = testCaseRepository.findAll().size();
        testCase.setId(count.incrementAndGet());

        // Create the TestCase
        TestCaseDTO testCaseDTO = testCaseMapper.toDto(testCase);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestCaseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testCaseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestCase in the database
        List<TestCase> testCaseList = testCaseRepository.findAll();
        assertThat(testCaseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTestCase() throws Exception {
        int databaseSizeBeforeUpdate = testCaseRepository.findAll().size();
        testCase.setId(count.incrementAndGet());

        // Create the TestCase
        TestCaseDTO testCaseDTO = testCaseMapper.toDto(testCase);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestCaseMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(testCaseDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestCase in the database
        List<TestCase> testCaseList = testCaseRepository.findAll();
        assertThat(testCaseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTestCase() throws Exception {
        // Initialize the database
        testCaseRepository.saveAndFlush(testCase);

        int databaseSizeBeforeDelete = testCaseRepository.findAll().size();

        // Delete the testCase
        restTestCaseMockMvc
            .perform(delete(ENTITY_API_URL_ID, testCase.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TestCase> testCaseList = testCaseRepository.findAll();
        assertThat(testCaseList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
