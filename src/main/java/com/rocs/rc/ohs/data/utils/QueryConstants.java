package com.rocs.rc.ohs.data.utils;

/**
 * Holds the SQL statements used by the data access objects.
 */
public final class QueryConstants {

    public static final String SELECT_LOGIN_BY_USERNAME =
            "select * from login where username = ?";

    public static final String UPDATE_LAST_LOGIN_DATE =
            "update login set last_login_date = ? where id = ?";

    public static final String UPDATE_PASSWORD_BY_EMAIL_AND_USERNAME =
            "update login set password = ? "
                    + "where username = ? "
                    + "and person_id in (select pe.id from person pe "
                    + "join personnel p on p.person_id = pe.id "
                    + "where lower(pe.email) = lower(?))";

}
