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


    /** Returns the role that determines this login's access in the OHS app. */
    public Role getRole() {
        return role;
    }

    /** Sets the role that determines this login's access. */
    public void setRole(Role newRole) {
        this.role = newRole;
    }

    /** Returns the person this login account belongs to. */
    public Person getPerson() {
        return person;
    }

    /** Sets the person this login account belongs to. */
    public void setPerson(Person newPerson) {
        this.person = newPerson;
    }

    /** Returns the username used to sign in. */
    public String getUsername() {
        return username;
    }

    /** Sets the username used to sign in. */
    public void setUsername(String newUsername) {
        this.username = newUsername;
    }

    /** Returns the stored password of this login account. */
    public String getPassword() {
        return password;
    }


    /** Sets the stored password of this login account. */
    public void setPassword(String newPassword) {
        this.password = newPassword;
    }


    /** Returns the date and time of the account's most recent login. */
    public Date getLastLoginDate() {
        return lastLoginDate;
    }


    /** Sets the date and time of the account's most recent login. */
    public void setLastLoginDate(Date newLastLoginDate) {
        this.lastLoginDate = newLastLoginDate;
    }


    /** Returns the ID of the role linked to this login. */
    public Long getRoleId() {
        return roleId;
    }


    /** Sets the ID of the role linked to this login. */
    public void setRoleId(Long newRoleId) {
        this.roleId = newRoleId;
    }


    /** Returns the ID of the person linked to this login. */
    public Long getPersonId() {
        return personId;
    }


    /** Sets the ID of the person linked to this login. */
    public void setPersonId(Long newPersonId) {
        this.personId = newPersonId;
    }
}
