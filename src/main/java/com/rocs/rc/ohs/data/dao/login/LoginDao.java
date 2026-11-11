package com.rocs.rc.ohs.data.dao.login;

import com.rocs.rc.ohs.app.model.login.Login;

/**
 * Defines the data access operations for login accounts
 * and for resetting a forgotten password.
 */
public interface LoginDao {

    /**
     * Retrieves the login account that matches the given username.
     *
     * @param username The username of the account to look up.
     * @return The {@link Login} account of the user.
     */
    Login login(String username);

    /**
     * Checks if the Staff ID or email belongs to an existing account.
     *
     * @param staffIdOrEmail The Staff ID or the email address.
     * @return True if an account matches, false if blank or not found.
     */
    boolean isRegistered(String staffIdOrEmail);

    /**
     * Updates the password only if the Staff ID or email and the
     * username belong to the same account.
     *
     * @param staffIdOrEmail The Staff ID or the email address.
     * @param username The username entered by the user.
     * @param newPassword The new password.
     * @return True if the password was updated, false if nothing matched.
     */
    boolean updatePassword(String staffIdOrEmail, String username,
                           String newPassword);
}