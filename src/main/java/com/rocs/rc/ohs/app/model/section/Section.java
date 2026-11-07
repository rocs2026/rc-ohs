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

    /** The year level of the section. */
    private Integer yearLevel;

    /** The semester of the section. */
    private String semester;

    /** The academic year of the section. */
    private String academicYear;

    /** The personnel assigned as adviser. */
    private Personnel adviser;


    /** Returns the unique ID of this section. */
    public Long getId() {
        return id;
    }


    /** Sets the unique ID of this section. */
    public void setId(Long newId) {
        this.id = newId;
    }

    /** Returns the short code that identifies this section. */
    public String getCode() {
        return code;
    }

    /** Sets the short code that identifies this section. */
    public void setCode(String newCode) {
        this.code = newCode;
    }

    /** Returns the full name of this section. */
    public String getName() {
        return name;
    }

    /** Sets the full name of this section. */
    public void setName(String newName) {
        this.name = newName;
    }

    /** Returns the department this section belongs to. */
    public Department getDepartment() {
        return department;
    }

    /** Sets the department this section belongs to. */
    public void setDepartment(Department newDepartment) {
        this.department = newDepartment;
    }

    /** Returns the year level of the students in this section. */
    public Integer getYearLevel() {
        return yearLevel;
    }

    /** Sets the year level of the students in this section. */
    public void setYearLevel(Integer newYearLevel) {
        this.yearLevel = newYearLevel;
    }

    /** Returns the semester in which this section is offered. */
    public String getSemester() {
        return semester;
    }

    /** Sets the semester in which this section is offered. */
    public void setSemester(String newSemester) {
        this.semester = newSemester;
    }

    /** Returns the academic year this section belongs to, such as 2026-2027. */
    public String getAcademicYear() {
        return academicYear;
    }

    /** Sets the academic year this section belongs to. */
    public void setAcademicYear(String newAcademicYear) {
        this.academicYear = newAcademicYear;
    }

    /** Returns the personnel member assigned as adviser of this section. */
    public Personnel getAdviser() {
        return adviser;
    }

    /** Sets the personnel member assigned as adviser of this section. */
    public void setAdviser(Personnel newAdviser) {
        this.adviser = newAdviser;
    }
}
