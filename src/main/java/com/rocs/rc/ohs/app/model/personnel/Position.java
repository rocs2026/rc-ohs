
package com.rocs.rc.ohs.app.model.personnel;

import com.rocs.rc.ohs.app.model.department.Department;

/**
 * Represents a personnel position in the system.
 */
public class Position {

    /** The unique ID of the position. */
    private Long positionId;

    /** The name of the position. */
    private String positionName;

    /** The category of the position. */
    private String category;

    /** The department associated with the position. */
    private Department department;

    /**
     * Retrieves the position ID.
     *
     * @return The position ID.
     */
    public Long getPositionId() {
        return positionId;
    }

    /**
     * Sets the position ID.
     *
     * @param newPositionId The position ID.
     */
    public void setPositionId(Long newPositionId) {
        this.positionId = newPositionId;
    }

    /**
     * Retrieves the position name.
     *
     * @return The position name.
     */
    public String getPositionName() {
        return positionName;
    }

    /**
     * Sets the position name.
     *
     * @param newPositionName The position name.
     */
    public void setPositionName(String newPositionName) {
        this.positionName = newPositionName;
    }

    /**
     * Retrieves the position category.
     *
     * @return The position category.
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets the position category.
     *
     * @param newCategory The position category.
     */
    public void setCategory(String newCategory) {
        this.category = newCategory;
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
}
