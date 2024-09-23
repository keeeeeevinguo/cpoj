package com.kevin.cpoj.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;

/**
 * A TestCase.
 */
@Entity
@Table(name = "test_case")
public class TestCase extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "name", length = 20, nullable = false)
    private String name;

    @NotNull
    @Min(value = 10)
    @Max(value = 10000)
    @Column(name = "time_limit_in_ms", nullable = false)
    private Integer timeLimitInMS;

    @NotNull
    @Min(value = 100)
    @Max(value = 512)
    @Column(name = "memory_limit_in_mb", nullable = false)
    private Integer memoryLimitInMB;

    /**
     * If a submission passes this test case, it should get this weight percentage of the total points\nIf a submission passes all the test case of a problem, it should get 100% of the total points
     */
    @NotNull
    @Min(value = 1)
    @Max(value = 100)
    @Column(name = "weight_percentage", nullable = false)
    private Integer weightPercentage;

    @Lob
    @Column(name = "input_data", nullable = false)
    private String inputData;

    @Lob
    @Column(name = "output_data", nullable = false)
    private String outputData;

    @ManyToOne
    @JsonIgnoreProperties(value = { "testCases" }, allowSetters = true)
    private Problem problem;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TestCase id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public TestCase name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTimeLimitInMS() {
        return this.timeLimitInMS;
    }

    public TestCase timeLimitInMS(Integer timeLimitInMS) {
        this.setTimeLimitInMS(timeLimitInMS);
        return this;
    }

    public void setTimeLimitInMS(Integer timeLimitInMS) {
        this.timeLimitInMS = timeLimitInMS;
    }

    public Integer getMemoryLimitInMB() {
        return this.memoryLimitInMB;
    }

    public TestCase memoryLimitInMB(Integer memoryLimitInMB) {
        this.setMemoryLimitInMB(memoryLimitInMB);
        return this;
    }

    public void setMemoryLimitInMB(Integer memoryLimitInMB) {
        this.memoryLimitInMB = memoryLimitInMB;
    }

    public Integer getWeightPercentage() {
        return this.weightPercentage;
    }

    public TestCase weightPercentage(Integer weightPercentage) {
        this.setWeightPercentage(weightPercentage);
        return this;
    }

    public void setWeightPercentage(Integer weightPercentage) {
        this.weightPercentage = weightPercentage;
    }

    public String getInputData() {
        return this.inputData;
    }

    public TestCase inputData(String inputData) {
        this.setInputData(inputData);
        return this;
    }

    public void setInputData(String inputData) {
        this.inputData = inputData;
    }

    public String getOutputData() {
        return this.outputData;
    }

    public TestCase outputData(String outputData) {
        this.setOutputData(outputData);
        return this;
    }

    public void setOutputData(String outputData) {
        this.outputData = outputData;
    }

    public TestCase createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public TestCase createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public TestCase lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public TestCase lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public Problem getProblem() {
        return this.problem;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }

    public TestCase problem(Problem problem) {
        this.setProblem(problem);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TestCase)) {
            return false;
        }
        return id != null && id.equals(((TestCase) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TestCase{" +
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
            "}";
    }
}
