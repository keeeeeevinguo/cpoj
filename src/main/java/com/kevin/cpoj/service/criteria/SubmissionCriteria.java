package com.kevin.cpoj.service.criteria;

import com.kevin.cpoj.domain.enumeration.ProgrammingLanguage;
import com.kevin.cpoj.domain.enumeration.SubmissionResultMessage;
import com.kevin.cpoj.domain.enumeration.SubmissionResultStatus;
import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.kevin.cpoj.domain.Submission} entity. This class is used
 * in {@link com.kevin.cpoj.web.rest.SubmissionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /submissions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SubmissionCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ProgrammingLanguage
     */
    public static class ProgrammingLanguageFilter extends Filter<ProgrammingLanguage> {

        public ProgrammingLanguageFilter() {}

        public ProgrammingLanguageFilter(ProgrammingLanguageFilter filter) {
            super(filter);
        }

        @Override
        public ProgrammingLanguageFilter copy() {
            return new ProgrammingLanguageFilter(this);
        }
    }

    /**
     * Class for filtering SubmissionResultStatus
     */
    public static class SubmissionResultStatusFilter extends Filter<SubmissionResultStatus> {

        public SubmissionResultStatusFilter() {}

        public SubmissionResultStatusFilter(SubmissionResultStatusFilter filter) {
            super(filter);
        }

        @Override
        public SubmissionResultStatusFilter copy() {
            return new SubmissionResultStatusFilter(this);
        }
    }

    /**
     * Class for filtering SubmissionResultMessage
     */
    public static class SubmissionResultMessageFilter extends Filter<SubmissionResultMessage> {

        public SubmissionResultMessageFilter() {}

        public SubmissionResultMessageFilter(SubmissionResultMessageFilter filter) {
            super(filter);
        }

        @Override
        public SubmissionResultMessageFilter copy() {
            return new SubmissionResultMessageFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private ProgrammingLanguageFilter programmingLanguage;

    private StringFilter code;

    private SubmissionResultStatusFilter overallResultStatus;

    private SubmissionResultMessageFilter overallResultMessage;

    private StringFilter overallResultMessageDetail;

    private IntegerFilter overallResultTries;

    private IntegerFilter overallResultScorePercentage;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private StringFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    private LongFilter submissionTestCaseResultId;

    private LongFilter problemId;

    private Boolean distinct;

    public SubmissionCriteria() {}

    public SubmissionCriteria(SubmissionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.programmingLanguage = other.programmingLanguage == null ? null : other.programmingLanguage.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.overallResultStatus = other.overallResultStatus == null ? null : other.overallResultStatus.copy();
        this.overallResultMessage = other.overallResultMessage == null ? null : other.overallResultMessage.copy();
        this.overallResultMessageDetail = other.overallResultMessageDetail == null ? null : other.overallResultMessageDetail.copy();
        this.overallResultTries = other.overallResultTries == null ? null : other.overallResultTries.copy();
        this.overallResultScorePercentage = other.overallResultScorePercentage == null ? null : other.overallResultScorePercentage.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.lastModifiedBy = other.lastModifiedBy == null ? null : other.lastModifiedBy.copy();
        this.lastModifiedDate = other.lastModifiedDate == null ? null : other.lastModifiedDate.copy();
        this.submissionTestCaseResultId = other.submissionTestCaseResultId == null ? null : other.submissionTestCaseResultId.copy();
        this.problemId = other.problemId == null ? null : other.problemId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public SubmissionCriteria copy() {
        return new SubmissionCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public ProgrammingLanguageFilter getProgrammingLanguage() {
        return programmingLanguage;
    }

    public ProgrammingLanguageFilter programmingLanguage() {
        if (programmingLanguage == null) {
            programmingLanguage = new ProgrammingLanguageFilter();
        }
        return programmingLanguage;
    }

    public void setProgrammingLanguage(ProgrammingLanguageFilter programmingLanguage) {
        this.programmingLanguage = programmingLanguage;
    }

    public StringFilter getCode() {
        return code;
    }

    public StringFilter code() {
        if (code == null) {
            code = new StringFilter();
        }
        return code;
    }

    public void setCode(StringFilter code) {
        this.code = code;
    }

    public SubmissionResultStatusFilter getOverallResultStatus() {
        return overallResultStatus;
    }

    public SubmissionResultStatusFilter overallResultStatus() {
        if (overallResultStatus == null) {
            overallResultStatus = new SubmissionResultStatusFilter();
        }
        return overallResultStatus;
    }

    public void setOverallResultStatus(SubmissionResultStatusFilter overallResultStatus) {
        this.overallResultStatus = overallResultStatus;
    }

    public SubmissionResultMessageFilter getOverallResultMessage() {
        return overallResultMessage;
    }

    public SubmissionResultMessageFilter overallResultMessage() {
        if (overallResultMessage == null) {
            overallResultMessage = new SubmissionResultMessageFilter();
        }
        return overallResultMessage;
    }

    public void setOverallResultMessage(SubmissionResultMessageFilter overallResultMessage) {
        this.overallResultMessage = overallResultMessage;
    }

    public StringFilter getOverallResultMessageDetail() {
        return overallResultMessageDetail;
    }

    public StringFilter overallResultMessageDetail() {
        if (overallResultMessageDetail == null) {
            overallResultMessageDetail = new StringFilter();
        }
        return overallResultMessageDetail;
    }

    public void setOverallResultMessageDetail(StringFilter overallResultMessageDetail) {
        this.overallResultMessageDetail = overallResultMessageDetail;
    }

    public IntegerFilter getOverallResultTries() {
        return overallResultTries;
    }

    public IntegerFilter overallResultTries() {
        if (overallResultTries == null) {
            overallResultTries = new IntegerFilter();
        }
        return overallResultTries;
    }

    public void setOverallResultTries(IntegerFilter overallResultTries) {
        this.overallResultTries = overallResultTries;
    }

    public IntegerFilter getOverallResultScorePercentage() {
        return overallResultScorePercentage;
    }

    public IntegerFilter overallResultScorePercentage() {
        if (overallResultScorePercentage == null) {
            overallResultScorePercentage = new IntegerFilter();
        }
        return overallResultScorePercentage;
    }

    public void setOverallResultScorePercentage(IntegerFilter overallResultScorePercentage) {
        this.overallResultScorePercentage = overallResultScorePercentage;
    }

    public StringFilter getCreatedBy() {
        return createdBy;
    }

    public StringFilter createdBy() {
        if (createdBy == null) {
            createdBy = new StringFilter();
        }
        return createdBy;
    }

    public void setCreatedBy(StringFilter createdBy) {
        this.createdBy = createdBy;
    }

    public InstantFilter getCreatedDate() {
        return createdDate;
    }

    public InstantFilter createdDate() {
        if (createdDate == null) {
            createdDate = new InstantFilter();
        }
        return createdDate;
    }

    public void setCreatedDate(InstantFilter createdDate) {
        this.createdDate = createdDate;
    }

    public StringFilter getLastModifiedBy() {
        return lastModifiedBy;
    }

    public StringFilter lastModifiedBy() {
        if (lastModifiedBy == null) {
            lastModifiedBy = new StringFilter();
        }
        return lastModifiedBy;
    }

    public void setLastModifiedBy(StringFilter lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public InstantFilter getLastModifiedDate() {
        return lastModifiedDate;
    }

    public InstantFilter lastModifiedDate() {
        if (lastModifiedDate == null) {
            lastModifiedDate = new InstantFilter();
        }
        return lastModifiedDate;
    }

    public void setLastModifiedDate(InstantFilter lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public LongFilter getSubmissionTestCaseResultId() {
        return submissionTestCaseResultId;
    }

    public LongFilter submissionTestCaseResultId() {
        if (submissionTestCaseResultId == null) {
            submissionTestCaseResultId = new LongFilter();
        }
        return submissionTestCaseResultId;
    }

    public void setSubmissionTestCaseResultId(LongFilter submissionTestCaseResultId) {
        this.submissionTestCaseResultId = submissionTestCaseResultId;
    }

    public LongFilter getProblemId() {
        return problemId;
    }

    public LongFilter problemId() {
        if (problemId == null) {
            problemId = new LongFilter();
        }
        return problemId;
    }

    public void setProblemId(LongFilter problemId) {
        this.problemId = problemId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SubmissionCriteria that = (SubmissionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(programmingLanguage, that.programmingLanguage) &&
            Objects.equals(code, that.code) &&
            Objects.equals(overallResultStatus, that.overallResultStatus) &&
            Objects.equals(overallResultMessage, that.overallResultMessage) &&
            Objects.equals(overallResultMessageDetail, that.overallResultMessageDetail) &&
            Objects.equals(overallResultTries, that.overallResultTries) &&
            Objects.equals(overallResultScorePercentage, that.overallResultScorePercentage) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(submissionTestCaseResultId, that.submissionTestCaseResultId) &&
            Objects.equals(problemId, that.problemId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            programmingLanguage,
            code,
            overallResultStatus,
            overallResultMessage,
            overallResultMessageDetail,
            overallResultTries,
            overallResultScorePercentage,
            createdBy,
            createdDate,
            lastModifiedBy,
            lastModifiedDate,
            submissionTestCaseResultId,
            problemId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubmissionCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (programmingLanguage != null ? "programmingLanguage=" + programmingLanguage + ", " : "") +
            (code != null ? "code=" + code + ", " : "") +
            (overallResultStatus != null ? "overallResultStatus=" + overallResultStatus + ", " : "") +
            (overallResultMessage != null ? "overallResultMessage=" + overallResultMessage + ", " : "") +
            (overallResultMessageDetail != null ? "overallResultMessageDetail=" + overallResultMessageDetail + ", " : "") +
            (overallResultTries != null ? "overallResultTries=" + overallResultTries + ", " : "") +
            (overallResultScorePercentage != null ? "overallResultScorePercentage=" + overallResultScorePercentage + ", " : "") +
            (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
            (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
            (lastModifiedBy != null ? "lastModifiedBy=" + lastModifiedBy + ", " : "") +
            (lastModifiedDate != null ? "lastModifiedDate=" + lastModifiedDate + ", " : "") +
            (submissionTestCaseResultId != null ? "submissionTestCaseResultId=" + submissionTestCaseResultId + ", " : "") +
            (problemId != null ? "problemId=" + problemId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
