package by.financialstatements.model.login_service.service;

import by.financialstatements.model.exception.LoginFailedException;
import by.financialstatements.model.log.CustomLogger;
import by.financialstatements.model.login_service.encryptor.LoginEncryptor;
import by.financialstatements.model.login_service.session.ApplicationSession;
import by.financialstatements.model.login_service.storage.MockStorage;
import by.financialstatements.model.login_service.tfa.TFALaunch;
/**
 * The AuthService class represents the user authentication service.
 * Its main task is to verify the credentials entered by the user (login and password),
 * use two-factor authentication (TFA) to increase the security level,
 * and return a session object (ApplicationSession) depending on the success or failure of authentication.
 */
public class AuthService {
    /**
     * The auth method performs the authentication process taking into account the following steps:
     * - Retrieves encrypted credentials (login and password) from the mock storage (MockStorage).
     * - Decrypts them using LoginEncryptor.
     * - Compares the user-entered data with the data retrieved from the database.
     * - If the credentials match, performs two-factor authentication (TFA).
     * - If TFA is successful, returns an object with a valid session, otherwise terminates the process with an error.
     * - If the credentials are incorrect or there is a TFA error, a LoginFailedException is thrown,
     * and an object with an invalid session is returned.
     *
     * @param login The user-entered login.
     * @param password The user-entered password.
     * @return An ApplicationSession object, which can be a "valid" or "invalid" session.
     */
    public static ApplicationSession auth(String login, String password) {
        // Simulated database as storage
        MockStorage storageLikeDB = new MockStorage();
        String loginFromDB = storageLikeDB.getLogin();
        String passwordFromDB = storageLikeDB.getPassword();
        // Decrypt data from storage
        String decryptedLogin = LoginEncryptor.decrypt(loginFromDB);
        String decryptedPassword = LoginEncryptor.decrypt(passwordFromDB);

        try {
            // Checking the entered login and password
            if (login.equals(decryptedLogin) &&
                    password.equals(decryptedPassword)) {
                CustomLogger.info("Login and password are entered correctly");
                // Two-factor authentication (TFA)
                if (TFALaunch.launchTFA()) {
                    CustomLogger.info("Session started successfully");
                    return new ApplicationSession("Valid session");
                }
                else {
                    throw new LoginFailedException("Error starting session: invalid authentication code", null);
                }
            } else {
                throw new LoginFailedException("Error starting session: invalid credentials", null);
            }
        } catch (LoginFailedException e) {
            CustomLogger.error(e.getMessage(), e);
            return new ApplicationSession("Invalid session");
        }
    }
}
