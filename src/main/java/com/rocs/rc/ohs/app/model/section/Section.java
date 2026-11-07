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


    /** @return the section ID */
    public Long getId() {
        return id;
    }


    /** @param newId the new section ID */
    public void setId(Long newId) {
        this.id = newId;
    }

    /** @return the section code */
    public String getCode() {
        return code;
    }

    /** @param newCode the new section code */
    public void setCode(String newCode) {
        this.code = newCode;
    }

    /** @return the section name */
    public String getName() {
        return name;
    }

    /** @param newName the new section name */
    public void setName(String newName) {
        this.name = newName;
    }

    /** @return the owning department */
    public Department getDepartment() {
        return department;
    }

    /** @param newDepartment the department to assign */
    public void setDepartment(Department newDepartment) {
        this.department = newDepartment;
    }

    /** @return the year level */
    public Integer getYearLevel() {
        return yearLevel;
    }

    /** @param newYearLevel the new year level */
    public void setYearLevel(Integer newYearLevel) {
        this.yearLevel = newYearLevel;
    }

    /** @return the semester */
    public String getSemester() {
        return semester;
    }

    /** @param newSemester the new semester */
    public void setSemester(String newSemester) {
        this.semester = newSemester;
    }

    /** @return the academic year */
    public String getAcademicYear() {
        return academicYear;
    }

    /** @param newAcademicYear the new academic year */
    public void setAcademicYear(String newAcademicYear) {
        this.academicYear = newAcademicYear;
    }

    /** @return the section adviser */
    public Personnel getAdviser() {
        return adviser;
    }

    /** @param newAdviser the personnel member to assign as adviser */
    public void setAdviser(Personnel newAdviser) {
        this.adviser = newAdviser;
    }
}
