package com.rocs.rc.ohs.data.dao.person;

import com.rocs.rc.ohs.app.model.person.Person;

import java.util.List;

/**
 * Defines the data access operations for persons.
 */
public interface PersonDao {

    /**
     * Retrieves the person that matches the given ID.
     *
     * @param id The ID of the person to look up.
     * @return The {@link Person}, or null if it is not found.
     */
    Person findById(String id);

    /**
     * Retrieves the persons whose name contains the given text. The
     * search is not case sensitive.
     *
     * @param name The full or partial name to look up.
     * @return The list of matching {@link Person}, or an empty list if
     *         there is no match.
     */
    List<Person> findByName(String name);
}