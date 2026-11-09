package com.rocs.rc.ohs.data.dao.forgotpassword;

/**
 * Defines the data access operations for resetting a forgotten password.
 */


    public interface ForgotPasswordDao {
        /** Checks if the Staff ID or email belongs to an existing account. */
        boolean isRegistered(String staffIdOrEmail);

        /** Updates the password only if staffIdOrEmail and username match the same account. */
        boolean updatePassword(String staffIdOrEmail, String username, String newPassword);
    }

