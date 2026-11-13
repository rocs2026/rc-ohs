package com.rocs.rc.ohs.data.dao.logout;

import com.rocs.rc.ohs.app.model.usersession.UserSession;

/**
 * Defines the data access operations for logging out
 * a clinic staff member.
 */

public interface LogoutDao {
    /**
     * Retrieves the active session of the given staff member.
     *
     * @param staffId The id of the clinic staff.
     * @return The active {@link UserSession}, or null if there is none.
     */

    UserSession findActiveSession(int staffId);

    /**
     * Ends the session by setting its logout time and status.
     *
     * @param sessionId The id of the session to end.
     * @return True if the session was ended, false if nothing matched.
     */

    boolean endSession(int sessionId);
}