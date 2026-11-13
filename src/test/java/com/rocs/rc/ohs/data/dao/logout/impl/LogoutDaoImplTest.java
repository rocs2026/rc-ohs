package com.rocs.rc.ohs.data.dao.logout.impl;

import com.rocs.rc.ohs.app.model.usersession.UserSession;
import com.rocs.rc.ohs.data.connection.ConnectionHelper;
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
import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link LogoutDaoImpl}.
 */
class LogoutDaoImplTest {

    /** The DAO under test. */
    private final LogoutDaoImpl dao = new LogoutDaoImpl();

    // Creates a mock prepared statement for the given SQL query.
    private PreparedStatement mockStatement(Connection connection, String sql)
            throws SQLException {
        PreparedStatement statement = mock(PreparedStatement.class);
        when(connection.prepareStatement(startsWith(sql)))
                .thenReturn(statement);
        return statement;
    }

    // Makes ConnectionHelper return the connection used in the test.
    private MockedStatic<ConnectionHelper> mockConnectionHelper(
            Connection connection) {
        MockedStatic<ConnectionHelper> helper =
                mockStatic(ConnectionHelper.class);
        helper.when(ConnectionHelper::getConnection)
                .thenReturn(connection);
        return helper;
    }

    // Checks if the active session is returned when it exists.
    @Test
    void findActiveSessionReturnsSessionWhenFound() throws SQLException {
        Connection connection = mock(Connection.class);
        PreparedStatement statement =
                mockStatement(connection, "select session_id");
        ResultSet resultSet = mock(ResultSet.class);

        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("session_id")).thenReturn(10);
        when(resultSet.getString("status")).thenReturn("ACTIVE");

        try (MockedStatic<ConnectionHelper> helper =
                     mockConnectionHelper(connection)) {
            UserSession session = dao.findActiveSession(1);

            assertEquals(10, session.getSessionId());
            assertEquals("ACTIVE", session.getStatus());
        }
    }

    // Checks if null is returned when there is no active session.
    @Test
    void findActiveSessionReturnsNullWhenNotFound() throws SQLException {
        Connection connection = mock(Connection.class);
        PreparedStatement statement =
                mockStatement(connection, "select session_id");
        ResultSet resultSet = mock(ResultSet.class);

        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        try (MockedStatic<ConnectionHelper> helper =
                     mockConnectionHelper(connection)) {
            assertNull(dao.findActiveSession(1));
        }
    }

    // Checks if the session is successfully ended.
    @Test
    void endSessionReturnsTrueWhenSessionEnded() throws SQLException {
        Connection connection = mock(Connection.class);
        PreparedStatement statement =
                mockStatement(connection, "update user_session set");

        when(statement.executeUpdate()).thenReturn(1);

        try (MockedStatic<ConnectionHelper> helper =
                     mockConnectionHelper(connection)) {
            assertTrue(dao.endSession(10));
        }
    }

    // Checks if false is returned when no session is updated.
    @Test
    void endSessionReturnsFalseWhenNothingMatches() throws SQLException {
        Connection connection = mock(Connection.class);
        PreparedStatement statement =
                mockStatement(connection, "update user_session set");

        when(statement.executeUpdate()).thenReturn(0);

        try (MockedStatic<ConnectionHelper> helper =
                     mockConnectionHelper(connection)) {
            assertFalse(dao.endSession(999));
        }
    }
}
