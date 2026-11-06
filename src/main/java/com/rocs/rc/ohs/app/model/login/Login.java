
package com.rocs.rc.ohs.app.model.login;

import com.rocs.rc.ohs.app.model.person.Person;
import com.rocs.rc.ohs.app.model.role.Role;

import java.util.Date;

/**
 * Represents a login account in the system.
 */
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

    /**
     * Retrieves the role assigned to the login user.
     *
     * @return The role of the login user.
     */
    public Role getRole() {
        return role;
    }

    /**
     * Sets the role assigned to the login user.
     *
     * @param newRole The role of the login user.
     */
    public void setRole(Role newRole) {
        this.role = newRole;
    }

    /**
     * Retrieves the person who owns the login account.
     *
     * @return The person associated with the login account.
     */
    public Person getPerson() {
        return person;
    }

    /**
     * Sets the person who owns the login account.
     *
     * @param newPerson The person associated with the login account.
     */
    public void setPerson(Person newPerson) {
        this.person = newPerson;
    }

    /**
     * Retrieves the username of the login user.
     *
     * @return The username of the login user.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the login user.
     *
     * @param newUsername The username of the login user.
     */
    public void setUsername(String newUsername) {
        this.username = newUsername;
    }

    /**
     * Retrieves the password of the login user.
     *
     * @return The password of the login user.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the login user.
     *
     * @param newPassword The password of the login user.
     */
    public void setPassword(String newPassword) {
        this.password = newPassword;
    }

    /**
     * Retrieves the date of the user's last login.
     *
     * @return The date of the last login.
     */
    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    /**
     * Sets the date of the user's last login.
     *
     * @param newLastLoginDate The date of the last login.
     */
    public void setLastLoginDate(Date newLastLoginDate) {
        this.lastLoginDate = newLastLoginDate;
    }

    /**
     * Retrieves the ID of the role of the login user.
     *
     * @return The ID of the role of the login user.
     */
    public Long getRoleId() {
        return roleId;
    }

    /**
     * Sets the ID of the role of the login user.
     *
     * @param newRoleId The ID of the role of the login user.
     */
    public void setRoleId(Long newRoleId) {
        this.roleId = newRoleId;
    }

    /**
     * Retrieves the ID of the person who owns the login account.
     *
     * @return The ID of the person who owns the login account.
     */
    public Long getPersonId() {
        return personId;
    }

    /**
     * Sets the ID of the person who owns the login account.
     *
     * @param newPersonId The ID of the person who owns the login account.
     */
    public void setPersonId(Long newPersonId) {
        this.personId = newPersonId;
    }
}
