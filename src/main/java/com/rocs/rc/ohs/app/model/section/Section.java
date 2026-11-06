
package com.rocs.rc.ohs.app.model.section;

import com.rocs.rc.ohs.app.model.department.Department;
import com.rocs.rc.ohs.app.model.personnel.Personnel;

/**
 * Represents a section in the system.
 */
public class Section {

    /** The unique ID of the section. */
    private Long id;

    /** The code of the section. */
    private String code;

    /** The name of the section. */
    private String name;

    /** The department associated with the section. */
    private Department department;

    /** The year level of the section. */
    private Integer yearLevel;

    /** The semester of the section. */
    private String semester;

    /** The academic year of the section. */
    private String academicYear;

    /** The personnel assigned as adviser. */
    private Personnel adviser;

    /**
     * Retrieves the section ID.
     *
     * @return The section ID.
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the section ID.
     *
     * @param newId The section ID.
     */
    public void setId(Long newId) {
        this.id = newId;
    }

    /**
     * Retrieves the section code.
     *
     * @return The section code.
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets the section code.
     *
     * @param newCode The section code.
     */
    public void setCode(String newCode) {
        this.code = newCode;
    }

    /**
     * Retrieves the section name.
     *
     * @return The section name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the section name.
     *
     * @param newName The section name.
     */
    public void setName(String newName) {
        this.name = newName;
    }

    /**
     * Retrieves the department.
     *
     * @return The department.
     */
    public Department getDepartment() {
        return department;
    }

    /**
     * Sets the department.
     *
     * @param newDepartment The department.
     */
    public void setDepartment(Department newDepartment) {
        this.department = newDepartment;
    }

    /**
     * Retrieves the year level.
     *
     * @return The year level.
     */
    public Integer getYearLevel() {
        return yearLevel;
    }

    /**
     * Sets the year level.
     *
     * @param newYearLevel The year level.
     */
    public void setYearLevel(Integer newYearLevel) {
        this.yearLevel = newYearLevel;
    }

    /**
     * Retrieves the semester.
     *
     * @return The semester.
     */
    public String getSemester() {
        return semester;
    }

    /**
     * Sets the semester.
     *
     * @param newSemester The semester.
     */
    public void setSemester(String newSemester) {
        this.semester = newSemester;
    }

    /**
     * Retrieves the academic year.
     *
     * @return The academic year.
     */
    public String getAcademicYear() {
        return academicYear;
    }

    /**
     * Sets the academic year.
     *
     * @param newAcademicYear The academic year.
     */
    public void setAcademicYear(String newAcademicYear) {
        this.academicYear = newAcademicYear;
    }

    /**
     * Retrieves the adviser.
     *
     * @return The adviser.
     */
    public Personnel getAdviser() {
        return adviser;
    }

    /**
     * Sets the adviser.
     *
     * @param newAdviser The adviser.
     */
    public void setAdviser(Personnel newAdviser) {
        this.adviser = newAdviser;
    }
}
