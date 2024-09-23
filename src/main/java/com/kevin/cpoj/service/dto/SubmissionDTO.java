package com.kevin.cpoj.service.dto;

import com.kevin.cpoj.domain.enumeration.ProgrammingLanguage;
import com.kevin.cpoj.domain.enumeration.SubmissionResultMessage;
import com.kevin.cpoj.domain.enumeration.SubmissionResultStatus;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the {@link com.kevin.cpoj.domain.Submission} entity.
 */
public class SubmissionDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 1, max = 20)
    private String name;

    @NotNull
    private ProgrammingLanguage programmingLanguage;

    @NotNull
    @Size(min = 1, max = 100000)
    private String code;

    @NotNull
    private SubmissionResultStatus overallResultStatus;

    @NotNull
    private SubmissionResultMessage overallResultMessage;

    @Size(min = 0, max = 1000)
    private String overallResultMessageDetail;

    @NotNull
    @Min(value = 0)
    @Max(value = 5)
    private Integer overallResultTries;

    @NotNull
    @Min(value = 0)
    @Max(value = 100)
    private Integer overallResultScorePercentage;

    private List<SubmissionTestCaseResultDTO> submissionTestCaseResults = new ArrayList<>();

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

    public ProgrammingLanguage getProgrammingLanguage() {
        return programmingLanguage;
    }

    public void setProgrammingLanguage(ProgrammingLanguage programmingLanguage) {
        this.programmingLanguage = programmingLanguage;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public SubmissionResultStatus getOverallResultStatus() {
        return overallResultStatus;
    }

    public void setOverallResultStatus(SubmissionResultStatus overallResultStatus) {
        this.overallResultStatus = overallResultStatus;
    }

    public SubmissionResultMessage getOverallResultMessage() {
        return overallResultMessage;
    }

    public void setOverallResultMessage(SubmissionResultMessage overallResultMessage) {
        this.overallResultMessage = overallResultMessage;
    }

    public String getOverallResultMessageDetail() {
        return overallResultMessageDetail;
    }

    public void setOverallResultMessageDetail(String overallResultMessageDetail) {
        this.overallResultMessageDetail = overallResultMessageDetail;
    }

    public Integer getOverallResultTries() {
        return overallResultTries;
    }

    public void setOverallResultTries(Integer overallResultTries) {
        this.overallResultTries = overallResultTries;
    }

    public Integer getOverallResultScorePercentage() {
        return overallResultScorePercentage;
    }

    public void setOverallResultScorePercentage(Integer overallResultScorePercentage) {
        this.overallResultScorePercentage = overallResultScorePercentage;
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

    public ProblemDTO getProblem()
    {
        return problem;
    }

    public void setProblem(ProblemDTO problem)
    {
        this.problem = problem;
    }

    public List<SubmissionTestCaseResultDTO> getSubmissionTestCaseResults()
    {
        return submissionTestCaseResults;
    }

    public void setSubmissionTestCaseResults(List<SubmissionTestCaseResultDTO> submissionTestCaseResults)
    {
        this.submissionTestCaseResults = submissionTestCaseResults;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof SubmissionDTO))
        {
            return false;
        }

        SubmissionDTO submissionDTO = (SubmissionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, submissionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubmissionDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", programmingLanguage='" + getProgrammingLanguage() + "'" +
            ", code='" + getCode() + "'" +
            ", overallResultStatus='" + getOverallResultStatus() + "'" +
            ", overallResultMessage='" + getOverallResultMessage() + "'" +
            ", overallResultMessageDetail='" + getOverallResultMessageDetail() + "'" +
            ", overallResultTries=" + getOverallResultTries() +
            ", overallResultScorePercentage=" + getOverallResultScorePercentage() +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", problem=" + getProblem() +
            "}";
    }
}
