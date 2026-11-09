package com.rocs.rc.ohs.data.dao.forgotpassword;

/**
 * Defines the data access operations for resetting a forgotten password.
 */
public interface ForgotPasswordDao {

    /**
     * Creates a password recovery request for the account that
     * matches the given Staff ID or email address.
     *
     * @param staffIdOrEmail The Staff ID or the email address.
     * @return The id of the new recovery request, or null if the input
     *         is blank or no account matches.
     */
    Long createRecovery(String staffIdOrEmail);

    /**
     * Verifies the identity of the user of a recovery request using
     * the username of the account, and records the attempt.
     *
     * @param recoveryId The recovery request id.
     * @param username The username entered by the user.
     * @return True if the username matches and the request is now
     *         verified.
     */
    boolean verifyIdentity(long recoveryId, String username);

    /**
     * Updates the password of the account of a verified recovery
     * request, then completes the request.
     *
     * @param recoveryId The recovery request id.
     * @param newPassword The new password.
     * @return True if the password was updated, or false if the request
     *         is not verified or the password is blank.
     */
    boolean updatePassword(long recoveryId, String newPassword);
}
