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


    public Role getRole() {
        return role;
    }

    public void setRole(Role newRole) {
        this.role = newRole;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person newPerson) {
        this.person = newPerson;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String newUsername) {
        this.username = newUsername;
    }

    public String getPassword() {
        return password;
    }


    public void setPassword(String newPassword) {
        this.password = newPassword;
    }


    public Date getLastLoginDate() {
        return lastLoginDate;
    }


    public void setLastLoginDate(Date newLastLoginDate) {
        this.lastLoginDate = newLastLoginDate;
    }


    public Long getRoleId() {
        return roleId;
    }


    public void setRoleId(Long newRoleId) {
        this.roleId = newRoleId;
    }


    public Long getPersonId() {
        return personId;
    }


    public void setPersonId(Long newPersonId) {
        this.personId = newPersonId;
    }
}
