package com.rocs.rc.ohs.app.model.role;

/** Represents a role in the system.*/
public class Role {

    /** The unique ID of the role. */
    private Long id;

    /** The name of the role. */
    private String role;

    /** The description of the role. */
    private String description;


    public Long getId() {
        return id;
    }


    public void setId(Long newId) {
        this.id = newId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String newRole) {
        this.role = newRole;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String newDescription) {
        this.description = newDescription;
    }
}
