package com.rocs.rc.ohs.data.dao.forgotpassword.impl;

import com.rocs.rc.ohs.data.connection.ConnectionHelper;
import com.rocs.rc.ohs.data.dao.forgotpassword.ForgotPasswordDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;



public class ForgotPasswordDaoImpl  implements ForgotPasswordDao {

    @Override
    public boolean isRegistered(String staffIdOrEmail) {
        if (staffIdOrEmail == null || staffIdOrEmail.trim().isEmpty()) {
            return false;
        }
        String value = staffIdOrEmail.trim();
        String sql = "select id from login" + "where lower (employee_id) = lower(?) or lower(email) = lower(?)";

        try (Connection connection = ConnectionHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, value);
            statement.setString(2, value);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            System.out.println("Error Checking Account:" + e.getMessage());
            return false;
            }
    }
    @Override
    public boolean updatePassword(String staffIdOrEmail, String username,
                                  String newPassword) {
        if (staffIdOrEmail == null || staffIdOrEmail.trim().isEmpty() ||
                username == null || newPassword == null) {
            return false;
        }
        String value = staffIdOrEmail.trim();
        String sql = "update login set password = ? where username ?"
                + "and(lower(employee_id) = "
                + "lower(?) or lower(email) = lower(?))";

            try (Connection connection = ConnectionHelper.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, newPassword);
                statement.setString(2, username.trim());
                statement.setString(3, value);
                statement.setString(4, value);
                return statement.executeUpdate() > 0;
            } catch (SQLException e) {
                System.out.println("Error updating password: " + e.getMessage());
                return false;
            }
        }
    }
