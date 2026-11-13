package com.rocs.rc.ohs.data.utils;

/**
 * Holds the SQL statements used by the data access objects.
 */
public final class QueryConstants {

    /**
     * Prevents instantiation of this class.
     */
    private QueryConstants() {
    }

    /**
     * Updates the Medical record of a patient (med conditions,
     * allergies).
     */
    public static final String UPDATE_MEDICAL_RECORD_STATEMENT =
            "UPDATE medical_record "
                    + "SET medical_conditions = ?, allergies = ? "
                    + "WHERE id =?";

    /**
     * Selects the login account that has the given username.
     */
    public static final String SELECT_LOGIN_BY_USERNAME =
            "select * from login where username = ?";

    /**
     * Updates the last login date of a login account.
     */
    public static final String UPDATE_LAST_LOGIN_DATE =
            "update login set last_login_date = ? where id = ?";

    /**
     * Updates the password of the login account that matches the
     * given username and the email of its personnel.
     */
    public static final String UPDATE_PASSWORD_BY_EMAIL_AND_USERNAME =
            "update login set password = ? "
                    + "where username = ? "
                    + "and person_id in (select pe.id from person pe "
                    + "join personnel p on p.person_id = pe.id "
                    + "where lower(pe.email) = lower(?))";

}
