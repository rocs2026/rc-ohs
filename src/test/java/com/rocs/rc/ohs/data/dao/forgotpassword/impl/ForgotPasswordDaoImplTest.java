package com.rocs.rc.ohs.data.dao.forgotpassword.impl;

import com.rocs.rc.ohs.data.connection.ConnectionHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests the {@link ForgotPasswordDaoImpl} class.
 */
class ForgotPasswordDaoImplTest {

    /** Username used for testing. */
    private String username;

    /** Email address used for testing. */
    private String email;

    /** Password used for testing. */
    private String password;

    /** Token used for testing. */
    private String token;

    private MockedStatic<ConnectionHelper> connectionHelper;
    private PreparedStatement insertRecovery;
    private PreparedStatement insertVerification;
    private PreparedStatement updateStatus;
    private PreparedStatement updatePassword;
    private ResultSet accountResult;
    private ResultSet keysResult;
    private ResultSet usernameResult;
    private ForgotPasswordDaoImpl dao;

    @BeforeEach
    void setUp() throws SQLException {
        Connection connection = mock(Connection.class);
        PreparedStatement findAccount = mock(PreparedStatement.class);
        PreparedStatement checkUsername = mock(PreparedStatement.class);
        insertRecovery = mock(PreparedStatement.class);
        insertVerification = mock(PreparedStatement.class);
        updateStatus = mock(PreparedStatement.class);
        updatePassword = mock(PreparedStatement.class);
        accountResult = mock(ResultSet.class);
        keysResult = mock(ResultSet.class);
        usernameResult = mock(ResultSet.class);

        when(connection.prepareStatement(startsWith("select p.id")))
                .thenReturn(findAccount);
        when(connection.prepareStatement(
                startsWith("insert into password_recovery"),
                any(String[].class))).thenReturn(insertRecovery);
        when(connection.prepareStatement(
                startsWith("select 1 from password_recovery")))
                .thenReturn(checkUsername);
        when(connection.prepareStatement(
                startsWith("insert into identity_verification")))
                .thenReturn(insertVerification);
        when(connection.prepareStatement(
                startsWith("update password_recovery")))
                .thenReturn(updateStatus);
        when(connection.prepareStatement(startsWith("update login")))
                .thenReturn(updatePassword);

        when(findAccount.executeQuery()).thenReturn(accountResult);
        when(insertRecovery.getGeneratedKeys()).thenReturn(keysResult);
        when(checkUsername.executeQuery()).thenReturn(usernameResult);

        connectionHelper = mockStatic(ConnectionHelper.class);
        connectionHelper.when(ConnectionHelper::getConnection)
                .thenReturn(connection);

        dao = new ForgotPasswordDaoImpl();
    }

    @AfterEach
    void tearDown() {
        connectionHelper.close();
    }

    // CA 2: registered Staff ID or email, so the request can proceed

    @Test
    void createRecovery_returnsRecoveryId_whenAccountIsRegistered()
            throws SQLException {
        when(accountResult.next()).thenReturn(true);
        when(accountResult.getLong(1)).thenReturn(5L);
        when(keysResult.next()).thenReturn(true);
        when(keysResult.getLong(1)).thenReturn(7L);

        assertEquals(Long.valueOf(7L), dao.createRecovery(" EMP-001 "));
        verify(insertRecovery).setLong(1, 5L);
        verify(insertRecovery).setString(2, "Pending");
    }

    // CA 3: not registered, so no reset request is created

    @Test
    void createRecovery_returnsNullAndCreatesNothing_whenNotRegistered()
            throws SQLException {
        when(accountResult.next()).thenReturn(false);

        assertNull(dao.createRecovery("EMP-999"));
        verify(insertRecovery, never()).executeUpdate();
    }

    // CA 4: empty Staff ID or email

    @Test
    void createRecovery_returnsNullWithoutConnecting_whenBlank() {
        assertNull(dao.createRecovery(null));
        assertNull(dao.createRecovery("   "));

        connectionHelper.verify(ConnectionHelper::getConnection, never());
    }

    // CA 5: identity verification

    @Test
    void verifyIdentity_verifiesRequest_whenUsernameMatches()
            throws SQLException {
        when(usernameResult.next()).thenReturn(true);

        assertTrue(dao.verifyIdentity(7L, "ana.corpuz"));
        verify(insertVerification).setObject(3, "Verified");
        verify(updateStatus).setObject(1, "Verified");
    }

    @Test
    void verifyIdentity_recordsFailure_whenUsernameDoesNotMatch()
            throws SQLException {
        when(usernameResult.next()).thenReturn(false);

        assertFalse(dao.verifyIdentity(7L, "someone.else"));
        verify(insertVerification).setObject(3, "Failed");
        verify(updateStatus, never()).executeUpdate();
    }

    // CA 5: the password changes only after verification

    @Test
    void updatePassword_updatesAndCompletesRequest_whenVerified()
            throws SQLException {
        when(updatePassword.executeUpdate()).thenReturn(1);

        assertTrue(dao.updatePassword(7L, "NewPass123"));
        verify(updatePassword).setObject(1, "NewPass123");
        verify(updatePassword).setObject(3, "Verified");
        verify(updateStatus).setObject(1, "Completed");
    }

    @Test
    void updatePassword_changesNothing_whenNotVerified()
            throws SQLException {
        when(updatePassword.executeUpdate()).thenReturn(0);

        assertFalse(dao.updatePassword(7L, "NewPass123"));
        verify(updateStatus, never()).executeUpdate();
    }
}
