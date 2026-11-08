package com.rocs.rc.ohs.app.model.section;

import com.rocs.rc.ohs.app.model.department.Department;
import com.rocs.rc.ohs.app.model.personnel.Personnel;

/** Represents a section in the system.*/
public class Section {

    /** The unique ID of the section. */
    private Long id;

    /** The code of the section. */
    private String code;

    /** The name of the section. */
    private String name;

    /** The department associated with the section. */
    private Department department;

    /** The year level of the section, such as Grade 7. */
    private String yearLevel;

    /** The semester of the section. */
    private String semester;

    /** The academic year of the section. */
    private String academicYear;

    /** The personnel assigned as adviser. */
    private Personnel adviser;


    /**
     * Returns the unique ID of this section.
     *
     * @return the section ID
     */
    public Long getId() {
        return id;
    }


    /**
     * Updates the unique ID used to identify this section.
     *
     * @param newId the new section ID
     */
    public void setId(Long newId) {
        this.id = newId;
    }

    /**
     * Returns the short code that identifies this section.
     *
     * @return the section code
     */
    public String getCode() {
        return code;
    }

    /**
     * Changes the short code that identifies this section.
     *
     * @param newCode the new section code
     */
    public void setCode(String newCode) {
        this.code = newCode;
    }

    /**
     * Returns the full name of this section.
     *
     * @return the section name
     */
    public String getName() {
        return name;
    }

    /**
     * Changes the full name of this section.
     *
     * @param newName the new section name
     */
    public void setName(String newName) {
        this.name = newName;
    }

    /**
     * Returns the department this section belongs to.
     *
     * @return the owning department
     */
    public Department getDepartment() {
        return department;
    }

    /**
     * Assigns this section to the given department.
     *
     * @param newDepartment the department to assign
     */
    public void setDepartment(Department newDepartment) {
        this.department = newDepartment;
    }

    /**
     * Returns the year level of the students in this section, such as Grade 7.
     *
     * @return the year level
     */
    public String getYearLevel() {
        return yearLevel;
    }

    /**
     * Changes the year level of the students in this section.
     *
     * @param newYearLevel the new year level, such as Grade 7
     */
    public void setYearLevel(String newYearLevel) {
        this.yearLevel = newYearLevel;
    }

    /**
     * Returns the semester in which this section is offered.
     *
     * @return the semester
     */
    public String getSemester() {
        return semester;
    }

    /**
     * Changes the semester in which this section is offered.
     *
     * @param newSemester the new semester
     */
    public void setSemester(String newSemester) {
        this.semester = newSemester;
    }

    /**
     * Returns the academic year of this section, such as 2026-2027.
     *
     * @return the academic year
     */
    public String getAcademicYear() {
        return academicYear;
    }

    /**
     * Changes the academic year of this section.
     *
     * @param newAcademicYear the new academic year
     */
    public void setAcademicYear(String newAcademicYear) {
        this.academicYear = newAcademicYear;
    }

    /**
     * Returns the personnel member assigned as adviser of this section.
     *
     * @return the section adviser
     */
    public Personnel getAdviser() {
        return adviser;
    }

    /**
     * Assigns the given personnel member as adviser of this section.
     *
     * @param newAdviser the personnel member to assign as adviser
     */
    public void setAdviser(Personnel newAdviser) {
        this.adviser = newAdviser;
    }
}
