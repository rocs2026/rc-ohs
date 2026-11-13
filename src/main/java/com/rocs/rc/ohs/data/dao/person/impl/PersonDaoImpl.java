
package com.rocs.rc.ohs.data.dao.person.impl;

import com.rocs.rc.ohs.app.model.enums.gender.Gender;
import com.rocs.rc.ohs.app.model.person.Person;
import com.rocs.rc.ohs.data.connection.ConnectionHelper;
import com.rocs.rc.ohs.data.dao.person.PersonDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles database operations for person records.
 */
public class PersonDaoImpl implements PersonDao {

    /**
     * Logger used to record database errors.
     */
    private static final Logger LOGGER =
            Logger.getLogger(PersonDaoImpl.class.getName());

    /**
     * SQL query used to find a person by ID.
     */
    private static final String SELECT_BY_ID_SQL =
            "SELECT id, first_name, middle_name, last_name, email, "
                    + "date_of_birth, gender, address, contact_number "
                    + "FROM person WHERE id = ?";

    /**
     * SQL query used to search persons by their names.
     */
    private static final String SELECT_BY_NAME_SQL =
            "SELECT id, first_name, middle_name, last_name, email, "
                    + "date_of_birth, gender, address, contact_number "
                    + "FROM person "
                    + "WHERE LOWER(first_name || ' ' || last_name) LIKE ? "
                    + "OR LOWER(first_name || ' ' || middle_name || ' ' "
                    + "|| last_name) LIKE ?";

    /**
     * Finds a person using their ID.
     *
     * @param id the person's ID
     * @return the matching person, or null if not found
     */
    @Override
    public Person findById(String id) {
        try (Connection connection = ConnectionHelper.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(SELECT_BY_ID_SQL)) {

            statement.setString(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapPerson(resultSet);
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE,
                    "Unable to retrieve person with ID: {0}", id);
            LOGGER.log(Level.SEVERE, "Database error details.", e);
        }

        return null;
    }

    /**
     * Searches for persons using their name.
     *
     * @param name the name to search
     * @return a list of matching persons
     */
    @Override
    public List<Person> findByName(String name) {
        List<Person> matchingPersons = new ArrayList<>();

        if (name == null || name.trim().isEmpty()) {
            return matchingPersons;
        }

        String searchValue = createSearchPattern(name);

        try (Connection connection = ConnectionHelper.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(SELECT_BY_NAME_SQL)) {

            applyNameSearch(statement, searchValue);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    matchingPersons.add(mapPerson(resultSet));
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE,
                    "Unable to search for persons.", e);
        }

        return matchingPersons;
    }

    /**
     * Prepares the name for a partial, case-insensitive search.
     *
     * @param name the name entered by the user
     * @return the formatted search value
     */
    private String createSearchPattern(String name) {
        return "%" + name.trim().toLowerCase(Locale.ROOT) + "%";
    }

    /**
     * Sets the search value for both name conditions.
     *
     * @param statement the prepared SQL statement
     * @param searchValue the formatted name to search
     * @throws SQLException if a parameter cannot be set
     */
    private void applyNameSearch(
            PreparedStatement statement, String searchValue)
            throws SQLException {

        statement.setString(1, searchValue);
        statement.setString(2, searchValue);
    }

    /**
     * Converts a database row into a Person object.
     *
     * @param resultSet the current database result
     * @return the mapped person
     * @throws SQLException if a column cannot be read
     */
    private Person mapPerson(ResultSet resultSet) throws SQLException {
        Person person = new Person();

        person.setId(resultSet.getString("id"));
        person.setFirstName(resultSet.getString("first_name"));
        person.setMiddleName(resultSet.getString("middle_name"));
        person.setLastName(resultSet.getString("last_name"));
        person.setEmail(resultSet.getString("email"));

        Timestamp birthDate = resultSet.getTimestamp("date_of_birth");

        if (birthDate != null) {
            person.setDateOfBirth(birthDate.toLocalDateTime());
        }

        person.setGender(findGender(resultSet.getString("gender")));
        person.setAddress(resultSet.getString("address"));
        person.setContactNumber(resultSet.getString("contact_number"));

        return person;
    }

    /**
     * Finds the Gender enum that matches the database value.
     *
     * @param databaseValue the gender value from the database
     * @return the matching Gender, or null if no match exists
     */
    private Gender findGender(String databaseValue) {
        if (databaseValue == null) {
            return null;
        }

        for (Gender gender : Gender.values()) {
            if (databaseValue.equals(gender.getValue())) {
                return gender;
            }
        }

        return null;
    }
}
