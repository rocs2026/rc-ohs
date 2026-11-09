package com.rocs.rc.ohs.data.dao.forgotpassword.impl;

import com.rocs.rc.ohs.data.connection.ConnectionHelper;
import com.rocs.rc.ohs.data.dao.forgotpassword.ForgotPasswordDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Implements the data access operations for resetting a forgotten password.
 */
public class ForgotPasswordDaoImpl implements ForgotPasswordDao {

    private static final String PENDING = "Pending";
    private static final String VERIFIED = "Verified";
    private static final String COMPLETED = "Completed";
    private static final String FAILED = "Failed";
    private static final String METHOD = "Username";

    /**
     * Creates a password recovery request for the account that
     * matches the given Staff ID or email address.
     *
     * @param staffIdOrEmail The Staff ID or the email address.
     * @return The id of the new recovery request, or null if the input
     *         is blank or no account matches.
     */
    @Override
    public Long createRecovery(String staffIdOrEmail) {

        Long recoveryId = null;
        if (staffIdOrEmail != null && !staffIdOrEmail.trim().isEmpty()) {
            String value = staffIdOrEmail.trim();
            try (Connection conn = ConnectionHelper.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(
                         "select p.id from personnel p "
                                 + "join person pe on pe.id = p.person_id "
                                 + "join login l on l.person_id = pe.id "
                                 + "where lower(p.employee_id) = lower(?) "
                                 + "or lower(pe.email) = lower(?)")) {
                stmt.setString(1, value);
                stmt.setString(2, value);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        recoveryId = insertRecovery(conn, rs.getLong(1));
                    }
                }
            } catch (SQLException e) {
                System.out.println(
                        "An exception was thrown while adding an item. "
                                + e.getMessage());
            }
        }
        return recoveryId;
    }

    /**
     * Verifies the identity of the user of a recovery request using
     * the username of the account, and records the attempt.
     *
     * @param recoveryId The recovery request id.
     * @param username The username entered by the user.
     * @return True if the username matches and the request is now
     *         verified.
     */
    @Override
    public boolean verifyIdentity(long recoveryId, String username) {

        boolean verified = false;
        if (username != null && !username.trim().isEmpty()) {
            try (Connection conn = ConnectionHelper.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(
                         "select 1 from password_recovery r "
                                 + "join personnel p on p.id = r.personnel_id "
                                 + "join login l on l.person_id = p.person_id "
                                 + "where r.recovery_id = ? "
                                 + "and r.recovery_status = ? "
                                 + "and l.username = ?")) {
                stmt.setLong(1, recoveryId);
                stmt.setString(2, PENDING);
                stmt.setString(3, username.trim());

                boolean matched;
                try (ResultSet rs = stmt.executeQuery()) {
                    matched = rs.next();
                }
                update(conn, "insert into identity_verification "
                                + "(recovery_id, verification_method, "
                                + "verification_status) values (?, ?, ?)",
                        recoveryId, METHOD, matched ? VERIFIED : FAILED);
                if (matched) {
                    update(conn, "update password_recovery "
                            + "set recovery_status = ? "
                            + "where recovery_id = ?", VERIFIED, recoveryId);
                }
                verified = matched;
            } catch (SQLException e) {
                System.out.println(
                        "An exception was thrown while finding an item. "
                                + e.getMessage());
            }
        }
        return verified;
    }

    /**
     * Updates the password of the account of a verified recovery
     * request, then completes the request.
     *
     * @param recoveryId The recovery request id.
     * @param newPassword The new password.
     * @return True if the password was updated, or false if the request
     *         is not verified or the password is blank.
     */
    @Override
    public boolean updatePassword(long recoveryId, String newPassword) {

        boolean updated = false;
        if (newPassword != null && !newPassword.isEmpty()) {
            try (Connection conn = ConnectionHelper.getConnection()) {
                updated = update(conn, "update login set password = ? "
                                + "where person_id = (select p.person_id "
                                + "from personnel p "
                                + "join password_recovery r on r.personnel_id = p.id "
                                + "where r.recovery_id = ? "
                                + "and r.recovery_status = ?)",
                        newPassword, recoveryId, VERIFIED) > 0;
                if (updated) {
                    update(conn, "update password_recovery "
                            + "set recovery_status = ? "
                            + "where recovery_id = ?", COMPLETED, recoveryId);
                }
            } catch (SQLException e) {
                System.out.println(
                        "An exception was thrown while updating an item. "
                                + e.getMessage());
            }
        }
        return updated;
    }

    private Long insertRecovery(Connection conn, long personnelId)
            throws SQLException {

        Long recoveryId = null;
        // the column name array works for both Oracle and MySQL
        try (PreparedStatement stmt = conn.prepareStatement(
                "insert into password_recovery "
                        + "(personnel_id, recovery_status) values (?, ?)",
                new String[] {"RECOVERY_ID"})) {
            stmt.setLong(1, personnelId);
            stmt.setString(2, PENDING);
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    recoveryId = keys.getLong(1);
                }
            }
        }
        return recoveryId;
    }

    private int update(Connection conn, String sql, Object... params)
            throws SQLException {

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            return stmt.executeUpdate();
        }
    }
}
