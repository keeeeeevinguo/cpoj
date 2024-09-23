package com.kevin.cpoj.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kevin.cpoj.domain.enumeration.ProgrammingLanguage;
import com.kevin.cpoj.domain.enumeration.SubmissionResultMessage;
import com.kevin.cpoj.domain.enumeration.SubmissionResultStatus;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A Submission.
 */
@Entity
@Table(name = "submission")
public class Submission extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "name", length = 20, nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "programming_language")
    private ProgrammingLanguage programmingLanguage;

    @NotNull
    @Size(min = 1, max = 100000)
    @Column(name = "code", length = 100000, nullable = false)
    private String code;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "overall_result_status", nullable = false)
    private SubmissionResultStatus overallResultStatus;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "overall_result_message", nullable = false)
    private SubmissionResultMessage overallResultMessage;

    @Size(min = 0, max = 1000)
    @Column(name = "overall_result_message_detail", length = 1000)
    private String overallResultMessageDetail;

    @NotNull
    @Min(value = 0)
    @Max(value = 5)
    @Column(name = "overall_result_tries", nullable = false)
    private Integer overallResultTries;

    @NotNull
    @Min(value = 0)
    @Max(value = 100)
    @Column(name = "overall_result_score_percentage", nullable = false)
    private Integer overallResultScorePercentage;

    @OneToMany(mappedBy = "submission")
    @JsonIgnoreProperties(value = { "submission" }, allowSetters = true)
    private Set<SubmissionTestCaseResult> submissionTestCaseResults = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "testCases" }, allowSetters = true)
    private Problem problem;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Submission id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Submission name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProgrammingLanguage getProgrammingLanguage() {
        return this.programmingLanguage;
    }

    public Submission programmingLanguage(ProgrammingLanguage programmingLanguage) {
        this.setProgrammingLanguage(programmingLanguage);
        return this;
    }

    public void setProgrammingLanguage(ProgrammingLanguage programmingLanguage) {
        this.programmingLanguage = programmingLanguage;
    }

    public String getCode() {
        return this.code;
    }

    public Submission code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public SubmissionResultStatus getOverallResultStatus() {
        return this.overallResultStatus;
    }

    public Submission overallResultStatus(SubmissionResultStatus overallResultStatus) {
        this.setOverallResultStatus(overallResultStatus);
        return this;
    }

    public void setOverallResultStatus(SubmissionResultStatus overallResultStatus) {
        this.overallResultStatus = overallResultStatus;
    }

    public SubmissionResultMessage getOverallResultMessage() {
        return this.overallResultMessage;
    }

    public Submission overallResultMessage(SubmissionResultMessage overallResultMessage) {
        this.setOverallResultMessage(overallResultMessage);
        return this;
    }

    public void setOverallResultMessage(SubmissionResultMessage overallResultMessage) {
        this.overallResultMessage = overallResultMessage;
    }

    public String getOverallResultMessageDetail() {
        return this.overallResultMessageDetail;
    }

    public Submission overallResultMessageDetail(String overallResultMessageDetail) {
        this.setOverallResultMessageDetail(overallResultMessageDetail);
        return this;
    }

    public void setOverallResultMessageDetail(String overallResultMessageDetail) {
        this.overallResultMessageDetail = overallResultMessageDetail;
    }

    public Integer getOverallResultTries() {
        return this.overallResultTries;
    }

    public Submission overallResultTries(Integer overallResultTries) {
        this.setOverallResultTries(overallResultTries);
        return this;
    }

    public void setOverallResultTries(Integer overallResultTries) {
        this.overallResultTries = overallResultTries;
    }

    public Integer getOverallResultScorePercentage() {
        return this.overallResultScorePercentage;
    }

    public Submission overallResultScorePercentage(Integer overallResultScorePercentage) {
        this.setOverallResultScorePercentage(overallResultScorePercentage);
        return this;
    }

    public void setOverallResultScorePercentage(Integer overallResultScorePercentage) {
        this.overallResultScorePercentage = overallResultScorePercentage;
    }

    public Submission createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public Submission createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public Submission lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public Submission lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public Set<SubmissionTestCaseResult> getSubmissionTestCaseResults() {
        return this.submissionTestCaseResults;
    }

    public void setSubmissionTestCaseResults(Set<SubmissionTestCaseResult> submissionTestCaseResults) {
        if (this.submissionTestCaseResults != null) {
            this.submissionTestCaseResults.forEach(i -> i.setSubmission(null));
        }
        if (submissionTestCaseResults != null) {
            submissionTestCaseResults.forEach(i -> i.setSubmission(this));
        }
        this.submissionTestCaseResults = submissionTestCaseResults;
    }

    public Submission submissionTestCaseResults(Set<SubmissionTestCaseResult> submissionTestCaseResults) {
        this.setSubmissionTestCaseResults(submissionTestCaseResults);
        return this;
    }

    public Submission addSubmissionTestCaseResult(SubmissionTestCaseResult submissionTestCaseResult) {
        this.submissionTestCaseResults.add(submissionTestCaseResult);
        submissionTestCaseResult.setSubmission(this);
        return this;
    }

    public Submission removeSubmissionTestCaseResult(SubmissionTestCaseResult submissionTestCaseResult) {
        this.submissionTestCaseResults.remove(submissionTestCaseResult);
        submissionTestCaseResult.setSubmission(null);
        return this;
    }

    public Problem getProblem() {
        return this.problem;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }

    public Submission problem(Problem problem) {
        this.setProblem(problem);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Submission)) {
            return false;
        }
        return id != null && id.equals(((Submission) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Submission{" +
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
            "}";
    }
}
