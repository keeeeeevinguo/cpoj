package com.kevin.cpoj.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A Problem.
 */
@Entity
@Table(name = "problem")
public class Problem extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 3, max = 20)
    @Column(name = "name", length = 20, nullable = false, unique = true)
    private String name;

    @NotNull
    @Size(min = 5, max = 500)
    @Column(name = "title", length = 500, nullable = false, unique = true)
    private String title;

    @NotNull
    @Size(min = 10, max = 100000)
    @Column(name = "description", length = 100000, nullable = false)
    private String description;

    @OneToMany(mappedBy = "problem")
    @JsonIgnoreProperties(value = { "problem" }, allowSetters = true)
    private Set<TestCase> testCases = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Problem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Problem name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return this.title;
    }

    public Problem title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public Problem description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Problem createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public Problem createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public Problem lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public Problem lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public Set<TestCase> getTestCases() {
        return this.testCases;
    }

    public void setTestCases(Set<TestCase> testCases) {
        if (this.testCases != null) {
            this.testCases.forEach(i -> i.setProblem(null));
        }
        if (testCases != null) {
            testCases.forEach(i -> i.setProblem(this));
        }
        this.testCases = testCases;
    }

    public Problem testCases(Set<TestCase> testCases) {
        this.setTestCases(testCases);
        return this;
    }

    public Problem addTestCase(TestCase testCase) {
        this.testCases.add(testCase);
        testCase.setProblem(this);
        return this;
    }

    public Problem removeTestCase(TestCase testCase) {
        this.testCases.remove(testCase);
        testCase.setProblem(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Problem)) {
            return false;
        }
        return id != null && id.equals(((Problem) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Problem{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
