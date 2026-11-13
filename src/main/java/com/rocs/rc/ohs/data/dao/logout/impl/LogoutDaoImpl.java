package com.rocs.rc.ohs.data.dao.logout.impl;

import com.rocs.rc.ohs.app.model.usersession.UserSession;
import com.rocs.rc.ohs.data.dao.logout.LogoutDao;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;


public class LogoutDaoImpl implements LogoutDao {
    private static final String ACTIVE = "ACTIVE";
    private static final String ENDED = "ENDED";

    private static final String FIND_ACTIVE =
            "SELECT session_id, staff_id, login_time, logout_time, status "
                    + "FROM user_session WHERE staff_id = ? AND status = ? "
                    + "ORDER BY login_time DESC LIMIT 1";

    private static final String END_SESSION =
            "UPDATE user_session SET logout_time = ?, status = ? "
                    + "WHERE session_id = ? AND status = ?";

    private final DataSource dataSource;

    public LogoutDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public UserSession findActiveSession(int staffId) {
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(FIND_ACTIVE)) {
            ps.setInt(1, staffId);
            ps.setString(2, ACTIVE);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find active session", e);
        }
    }

    @Override
    public boolean endSession(int sessionId) {
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(END_SESSION)) {
            ps.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            ps.setString(2, ENDED);
            ps.setInt(3, sessionId);
            ps.setString(4, ACTIVE);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to end session", e);
        }
    }

    private UserSession mapRow(ResultSet rs) throws SQLException {
        UserSession session = new UserSession();
        session.setSessionId(rs.getInt("session_id"));
        session.setStaffId(rs.getInt("staff_id"));
        session.setLoginTime(rs.getTimestamp("login_time"));
        session.setLogoutTime(rs.getTimestamp("logout_time"));
        session.setStatus(rs.getString("status"));
        return session;
    }
}