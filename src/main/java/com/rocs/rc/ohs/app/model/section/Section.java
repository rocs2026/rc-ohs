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


    public Long getId() {
        return id;
    }


    public void setId(Long newId) {
        this.id = newId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String newCode) {
        this.code = newCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department newDepartment) {
        this.department = newDepartment;
    }

    public Integer getYearLevel() {
        return yearLevel;
    }

    public void setYearLevel(Integer newYearLevel) {
        this.yearLevel = newYearLevel;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String newSemester) {
        this.semester = newSemester;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(String newAcademicYear) {
        this.academicYear = newAcademicYear;
    }

    public Personnel getAdviser() {
        return adviser;
    }

    public void setAdviser(Personnel newAdviser) {
        this.adviser = newAdviser;
    }
}
