package com.kevin.cpoj.service.dto;

import com.kevin.cpoj.domain.enumeration.TestCaseResultMessage;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.kevin.cpoj.domain.SubmissionTestCaseResult} entity.
 */
public class SubmissionTestCaseResultDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 1, max = 20)
    private String name;

    @NotNull
    private TestCaseResultMessage resultMessage;

    @Size(min = 0, max = 1000)
    private String resultMessageDetail;

    @NotNull
    @Min(value = 0)
    @Max(value = 10000)
    private Integer elapsedTimeInMS;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    // private SubmissionDTO submission;

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

    public TestCaseResultMessage getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(TestCaseResultMessage resultMessage) {
        this.resultMessage = resultMessage;
    }

    public String getResultMessageDetail() {
        return resultMessageDetail;
    }

    public void setResultMessageDetail(String resultMessageDetail) {
        this.resultMessageDetail = resultMessageDetail;
    }

    public Integer getElapsedTimeInMS() {
        return elapsedTimeInMS;
    }

    public void setElapsedTimeInMS(Integer elapsedTimeInMS) {
        this.elapsedTimeInMS = elapsedTimeInMS;
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

    public Instant getLastModifiedDate()
    {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate)
    {
        this.lastModifiedDate = lastModifiedDate;
    }

//    public SubmissionDTO getSubmission() {
//        return submission;
//    }
//
//    public void setSubmission(SubmissionDTO submission) {
//        this.submission = submission;
//    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof SubmissionTestCaseResultDTO))
        {
            return false;
        }

        SubmissionTestCaseResultDTO submissionTestCaseResultDTO = (SubmissionTestCaseResultDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, submissionTestCaseResultDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubmissionTestCaseResultDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", resultMessage='" + getResultMessage() + "'" +
            ", resultMessageDetail='" + getResultMessageDetail() + "'" +
            ", elapsedTimeInMS=" + getElapsedTimeInMS() +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            // ", submission=" + getSubmission() +
            "}";
    }
}
