package com.rocs.rc.ohs.data.dao.login.impl;

import com.rocs.rc.ohs.data.connection.ConnectionHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests the {@link LoginDaoImpl} class.
 */
class LoginDaoImplTest {

    /** Static connection helper used in tests. */
    private MockedStatic<ConnectionHelper> connectionHelper;

    /** Prepared statement for updating the password. */
    private PreparedStatement updatePassword;

    /** DAO instance being tested. */
    private LoginDaoImpl dao;

    @BeforeEach
    void setUp() throws SQLException {
        Connection connection = mock(Connection.class);
        updatePassword = mock(PreparedStatement.class);

        when(connection.prepareStatement(
                startsWith("update login set password")))
                .thenReturn(updatePassword);

        connectionHelper = mockStatic(ConnectionHelper.class);
        connectionHelper.when(ConnectionHelper::getConnection)
                .thenReturn(connection);

        dao = new LoginDaoImpl();
    }

    @AfterEach
    void tearDown() {
        connectionHelper.close();
    }

    //AC4:empty Staff ID or email.
    @Test
    void updatePasswordReturnsFalseWithoutConnectingWhenBlank() {
        assertFalse(dao.updatePassword(null,
                "ana.corpuz", "NewPass123"));
        assertFalse(dao.updatePassword("   ", "ana.corpuz",
                "NewPass123"));

        connectionHelper.verify(ConnectionHelper::getConnection, never());
    }

    //Identity verified (details match), so the password is updated.
    @Test
    void updatePasswordReturnsTrueWhenDetailsMatch() throws SQLException {
        when(updatePassword.executeUpdate()).thenReturn(1);

        assertTrue(dao.updatePassword("EMP-001", "ana.corpuz",
                "NewPass123"));
        verify(updatePassword).setString(1, "NewPass123");
        verify(updatePassword).setString(2, "ana.corpuz");
    }

    //Identity not verified, so nothing is updated.
    @Test
    void updatePasswordReturnsFalseWhenDetailsDoNotMatch()
            throws SQLException {
        when(updatePassword.executeUpdate()).thenReturn(0);

        assertFalse(dao.updatePassword(
                "EMP-001", "someone.else",
                "NewPass123"));
    }
}

