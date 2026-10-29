package com.rocs.rc.ohs.app.model.login;

import java.util.Date;

/**
 * Represents a login account in the system.
 */
public class Login {

    /** The unique ID of the login user. */
    private Long id;

    /** The username used to log in. */
    private String username;

    /** The password used to log in. */
    private String password;

    /** The date and time of the user's last login. */
    private Date lastLoginDate;

    /** The role of the user (for example, admin or student). */
    private String role;

    /**
     * Retrieves the ID of the login user.
     *
     * @return The ID of the login user.
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the ID of the login user.
     *
     * @param id The ID of the login user.
     */
    public void setId(Long id) {
        this.id = id;
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
     * @param username The username of the login user.
     */
    public void setUsername(String username) {
        this.username = username;
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
     * @param password The password of the login user.
     */
    public void setPassword(String password) {
        this.password = password;
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
     * @param lastLoginDate The date of the last login.
     */
    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    /**
     * Retrieves the role of the login user.
     *
     * @return The role of the login user.
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the role of the login user.
     *
     * @param role The role of the login user.
     */
    public void setRole(String role) {
        this.role = role;
    }
}
