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


    /**
     * Returns the role that determines this login's access in the OHS app.
     *
     * @return the assigned role
     */
    public Role getRole() {
        return role;
    }

    /**
     * Assigns the role that determines this login's access.
     *
     * @param newRole the role to assign
     */
    public void setRole(Role newRole) {
        this.role = newRole;
    }

    /**
     * Returns the person this login account belongs to.
     *
     * @return the linked person
     */
    public Person getPerson() {
        return person;
    }

    /**
     * Links this login account to the given person.
     *
     * @param newPerson the person to link
     */
    public void setPerson(Person newPerson) {
        this.person = newPerson;
    }

    /**
     * Returns the username used to sign in.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username used to sign in.
     *
     * @param newUsername the new username
     */
    public void setUsername(String newUsername) {
        this.username = newUsername;
    }

    /**
     * Returns the stored password of this login account.
     *
     * @return the stored password
     */
    public String getPassword() {
        return password;
    }


    /**
     * Sets the stored password of this login account.
     *
     * @param newPassword the new password
     */
    public void setPassword(String newPassword) {
        this.password = newPassword;
    }


    /**
     * Returns the date and time of the account's most recent login.
     *
     * @return the last login date and time
     */
    public Date getLastLoginDate() {
        return lastLoginDate;
    }


    /**
     * Sets the date and time of the account's most recent login.
     *
     * @param newLastLoginDate the latest login date and time
     */
    public void setLastLoginDate(Date newLastLoginDate) {
        this.lastLoginDate = newLastLoginDate;
    }
}
