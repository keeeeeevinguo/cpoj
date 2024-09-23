package com.kevin.cpoj.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kevin.cpoj.domain.enumeration.TestCaseResultMessage;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;

/**
 * A SubmissionTestCaseResult.
 */
@Entity
@Table(name = "submission_test_case_result")
public class SubmissionTestCaseResult extends AbstractAuditingEntity implements Serializable {

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
    @Enumerated(EnumType.STRING)
    @Column(name = "result_message", nullable = false)
    private TestCaseResultMessage resultMessage;

    @Size(min = 0, max = 1000)
    @Column(name = "result_message_detail", length = 1000)
    private String resultMessageDetail;

    @NotNull
    @Min(value = 0)
    @Max(value = 10000)
    @Column(name = "elapsed_time_in_ms", nullable = false)
    private Integer elapsedTimeInMS;

    @ManyToOne
    @JsonIgnoreProperties(value = { "submissionTestCaseResults", "problem" }, allowSetters = true)
    private Submission submission;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SubmissionTestCaseResult id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public SubmissionTestCaseResult name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TestCaseResultMessage getResultMessage() {
        return this.resultMessage;
    }

    public SubmissionTestCaseResult resultMessage(TestCaseResultMessage resultMessage) {
        this.setResultMessage(resultMessage);
        return this;
    }

    public void setResultMessage(TestCaseResultMessage resultMessage) {
        this.resultMessage = resultMessage;
    }

    public String getResultMessageDetail() {
        return this.resultMessageDetail;
    }

    public SubmissionTestCaseResult resultMessageDetail(String resultMessageDetail) {
        this.setResultMessageDetail(resultMessageDetail);
        return this;
    }

    public void setResultMessageDetail(String resultMessageDetail) {
        this.resultMessageDetail = resultMessageDetail;
    }

    public Integer getElapsedTimeInMS() {
        return this.elapsedTimeInMS;
    }

    public SubmissionTestCaseResult elapsedTimeInMS(Integer elapsedTimeInMS) {
        this.setElapsedTimeInMS(elapsedTimeInMS);
        return this;
    }

    public void setElapsedTimeInMS(Integer elapsedTimeInMS) {
        this.elapsedTimeInMS = elapsedTimeInMS;
    }

    public SubmissionTestCaseResult createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public SubmissionTestCaseResult createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public SubmissionTestCaseResult lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public SubmissionTestCaseResult lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public Submission getSubmission() {
        return this.submission;
    }

    public void setSubmission(Submission submission) {
        this.submission = submission;
    }

    public SubmissionTestCaseResult submission(Submission submission) {
        this.setSubmission(submission);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubmissionTestCaseResult)) {
            return false;
        }
        return id != null && id.equals(((SubmissionTestCaseResult) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubmissionTestCaseResult{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", resultMessage='" + getResultMessage() + "'" +
            ", resultMessageDetail='" + getResultMessageDetail() + "'" +
            ", elapsedTimeInMS=" + getElapsedTimeInMS() +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
