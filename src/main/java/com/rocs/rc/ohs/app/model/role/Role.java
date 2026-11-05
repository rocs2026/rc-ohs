
package com.rocs.rc.ohs.app.model.role;

/**
 * Represents a role in the system.
 */
public class Role {

    /** The unique ID of the role. */
    private Long id;

    /** The name of the role. */
    private String role;

    /** The description of the role. */
    private String description;

    /**
     * Retrieves the ID of the role.
     *
     * @return The ID of the role.
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the ID of the role.
     *
     * @param newId The ID of the role.
     */
    public void setId(Long newId) {
        this.id = newId;
    }

    /**
     * Retrieves the name of the role.
     *
     * @return The name of the role.
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the name of the role.
     *
     * @param newRole The name of the role.
     */
    public void setRole(String newRole) {
        this.role = newRole;
    }

    /**
     * Retrieves the description of the role.
     *
     * @return The description of the role.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the role.
     *
     * @param newDescription The description of the role.
     */
    public void setDescription(String newDescription) {
        this.description = newDescription;
    }
}
