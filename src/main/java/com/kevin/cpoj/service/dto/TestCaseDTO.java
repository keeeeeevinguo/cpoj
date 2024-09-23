package com.kevin.cpoj.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.persistence.Lob;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.kevin.cpoj.domain.TestCase} entity.
 */
public class TestCaseDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 1, max = 20)
    private String name;

    @NotNull
    @Min(value = 10)
    @Max(value = 10000)
    private Integer timeLimitInMS;

    @NotNull
    @Min(value = 100)
    @Max(value = 512)
    private Integer memoryLimitInMB;

    /**
     * If a submission passes this test case, it should get this weight percentage of the total points\nIf a submission passes all the test case of a problem, it should get 100% of the total points
     */
    @NotNull
    @Min(value = 1)
    @Max(value = 100)
    @Schema(
        description = "If a submission passes this test case, it should get this weight percentage of the total points\nIf a submission passes all the test case of a problem, it should get 100% of the total points",
        required = true
    )
    private Integer weightPercentage;

    @Lob
    private String inputData;

    @Lob
    private String outputData;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    private ProblemDTO problem;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTimeLimitInMS() {
        return timeLimitInMS;
    }

    public void setTimeLimitInMS(Integer timeLimitInMS) {
        this.timeLimitInMS = timeLimitInMS;
    }

    public Integer getMemoryLimitInMB() {
        return memoryLimitInMB;
    }

    public void setMemoryLimitInMB(Integer memoryLimitInMB) {
        this.memoryLimitInMB = memoryLimitInMB;
    }

    public Integer getWeightPercentage() {
        return weightPercentage;
    }

    public void setWeightPercentage(Integer weightPercentage) {
        this.weightPercentage = weightPercentage;
    }

    public String getInputData() {
        return inputData;
    }

    public void setInputData(String inputData) {
        this.inputData = inputData;
    }

    public String getOutputData() {
        return outputData;
    }

    public void setOutputData(String outputData) {
        this.outputData = outputData;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public ProblemDTO getProblem() {
        return problem;
    }

    public void setProblem(ProblemDTO problem) {
        this.problem = problem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TestCaseDTO)) {
            return false;
        }

        TestCaseDTO testCaseDTO = (TestCaseDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, testCaseDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TestCaseDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", timeLimitInMS=" + getTimeLimitInMS() +
            ", memoryLimitInMB=" + getMemoryLimitInMB() +
            ", weightPercentage=" + getWeightPercentage() +
            ", inputData='" + getInputData() + "'" +
            ", outputData='" + getOutputData() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", problem=" + getProblem() +
            "}";
    }
}
