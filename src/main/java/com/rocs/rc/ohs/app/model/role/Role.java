package com.rocs.rc.ohs.app.model.role;

/** Represents a role in the system.*/
public class Role {

    /** The unique ID of the role. */
    private Long id;

    /** The name of the role. */
    private String role;

    /** The description of the role. */
    private String description;


    /** Returns the unique ID of this personnel position. */
    public Long getId() {
        return id;
    }


    /** Sets the unique ID of this personnel position. */
    public void setId(Long newId) {
        this.id = newId;
    }

    /** Returns the role held by the personnel in this position. */
    public String getRole() {
        return role;
    }

    /** Sets the role held by the personnel in this position. */
    public void setRole(String newRole) {
        this.role = newRole;
    }

    /** Returns the description of this personnel position. */
    public String getDescription() {
        return description;
    }

    /** Sets the description of this personnel position. */
    public void setDescription(String newDescription) {
        this.description = newDescription;
    }
}
