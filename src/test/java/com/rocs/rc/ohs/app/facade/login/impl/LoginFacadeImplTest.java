package com.rocs.rc.ohs.app.facade.login.impl;

import com.rocs.rc.ohs.app.model.login.Login;
import com.rocs.rc.ohs.data.dao.login.LoginDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/**
 * Tests the {@link LoginFacadeImpl}.
 */
class LoginFacadeImplTest {

    private static final String EMAIL = "john.doe@example.com";
    private static final String USERNAME = "staff";
    private static final String NEW_PASSWORD = "NewPass123!";
    private static final String PREFIX = "Cannot reset the password. ";

    @Mock
    private LoginDao loginDao;

    private LoginFacadeImpl loginFacade;

    /**
     * Creates the facade with a mocked DAO before each test.
     */
    @BeforeEach
    void setUp() {
        loginFacade = new LoginFacadeImpl(loginDao);
    }

    /**
     * Login returns the account found by the DAO,
     * or null when the DAO finds nothing.
     */
    @Test
    void loginReturnsAccountFromDao() {
        Login account = org.mockito.Mockito.mock(Login.class);
        when(loginDao.login(USERNAME)).thenReturn(account);
        when(loginDao.login("unknown")).thenReturn(null);

        assertSame(account, loginFacade.login(USERNAME, "anyPassword"));
        assertNull(loginFacade.login("unknown", "anyPassword"));
    }

    /**
     * Forgot password passes the details to the DAO
     * and returns whatever the DAO reports.
     */
    @Test
    void forgotPasswordReturnsDaoResult() {
        when(loginDao.updatePassword(EMAIL, USERNAME,
                NEW_PASSWORD))
                .thenReturn(true);
        assertTrue(loginFacade.forgotPassword(EMAIL, USERNAME,
                NEW_PASSWORD));

        when(loginDao.updatePassword(EMAIL, "other",
                NEW_PASSWORD))
                .thenReturn(false);
        assertFalse(loginFacade.forgotPassword(EMAIL, "other",
                NEW_PASSWORD));
    }

    /**
     * Forgot password rejects every missing value (null, empty, blank)
     * with the matching message and never reaches the DAO.
     */
    @Test
    void forgotPasswordRejectsMissingValues() {
        String[] missing = {null, "", "   "};

        for (String value : missing) {
            assertRejected(value, USERNAME, NEW_PASSWORD,
                    "Email is required.");
            assertRejected(EMAIL, value, NEW_PASSWORD,
                    "Username is required.");
            assertRejected(EMAIL, USERNAME, value,
                    "New password is required.");
        }

        verify(loginDao, never())
                .updatePassword(
                        org.mockito.ArgumentMatchers.anyString(),
                        org.mockito.ArgumentMatchers.anyString(),
                        org.mockito.ArgumentMatchers.anyString());
        verifyNoInteractions(loginDao);
    }

    /**
     * Checks that the given values are rejected with the expected message
     * and that the original exception is kept as the cause.
     */
    private void assertRejected(String email, String username,
                                String password, String message) {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> loginFacade.forgotPassword(email, username, password));

        assertEquals(PREFIX + message, ex.getMessage());
        assertInstanceOf(IllegalArgumentException.class, ex.getCause());
    }
}
