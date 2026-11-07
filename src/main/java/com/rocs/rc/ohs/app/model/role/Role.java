package com.rocs.rc.ohs.app.model.role;

/** Represents a role in the system.*/
public class Role {

    /** The unique ID of the role. */
    private Long id;

    /** The name of the role. */
    private String role;

    /** The description of the role. */
    private String description;


    /** @return the role ID */
    public Long getId() {
        return id;
    }


    /** @param newId the new role ID */
    public void setId(Long newId) {
        this.id = newId;
    }

    /** @return the role name */
    public String getRole() {
        return role;
    }

    /** @param newRole the new role name */
    public void setRole(String newRole) {
        this.role = newRole;
    }

    /** @return the role description */
    public String getDescription() {
        return description;
    }

    /** @param newDescription the new role description */
    public void setDescription(String newDescription) {
        this.description = newDescription;
    }
}
