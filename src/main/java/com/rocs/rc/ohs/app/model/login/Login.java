package com.rocs.rc.ohs.app.model.login;

import com.rocs.rc.ohs.app.model.person.Person;
import com.rocs.rc.ohs.app.model.role.Role;

import java.util.Date;

/** Represents a login account in the system. */
public class Login {

    /** The role assigned to the login user. */
    private Role role;

    /** The person who owns the login account. */
    private Person person;

    /** The username used to log in. */
    private String username;

    /** The password used to log in. */
    private String password;

    /** The date and time of the user's last login. */
    private Date lastLoginDate;

    /** The ID of the role of the login user. */
    private Long roleId;

    /** The ID of the person who owns the login account. */
    private Long personId;


    /** @return the assigned role */
    public Role getRole() {
        return role;
    }

    /** @param newRole the role to assign */
    public void setRole(Role newRole) {
        this.role = newRole;
    }

    /** @return the linked person */
    public Person getPerson() {
        return person;
    }

    /** @param newPerson the person to link */
    public void setPerson(Person newPerson) {
        this.person = newPerson;
    }

    /** @return the username */
    public String getUsername() {
        return username;
    }

    /** @param newUsername the new username */
    public void setUsername(String newUsername) {
        this.username = newUsername;
    }

    /** @return the stored password */
    public String getPassword() {
        return password;
    }


    /** @param newPassword the new password */
    public void setPassword(String newPassword) {
        this.password = newPassword;
    }


    /** @return the last login date and time */
    public Date getLastLoginDate() {
        return lastLoginDate;
    }


    /** @param newLastLoginDate the latest login date and time */
    public void setLastLoginDate(Date newLastLoginDate) {
        this.lastLoginDate = newLastLoginDate;
    }


    /** @return the role ID */
    public Long getRoleId() {
        return roleId;
    }


    /** @param newRoleId the ID of the role to link */
    public void setRoleId(Long newRoleId) {
        this.roleId = newRoleId;
    }


    /** @return the person ID */
    public Long getPersonId() {
        return personId;
    }


    /** @param newPersonId the ID of the person to link */
    public void setPersonId(Long newPersonId) {
        this.personId = newPersonId;
    }
}
