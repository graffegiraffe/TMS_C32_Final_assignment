package by.financialstatements.model.login_service.session;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.stream.Collectors;
/**
 * The ApplicationSession class represents a user session in an application.
 * It generates an access token accessToken and sets the session expiration time.
 * A session is considered valid if the token was successfully generated, its expiration time was set,
 * and the expiration time has not yet expired.
 */
public class ApplicationSession {
    private String accessToken;
    private Date expDate;
    /**
     * The ApplicationSession constructor creates a session based on the input parameter.
     * If the message indicates that the session is valid ("Valid session"), the token and expiration
     * time are initialized.
     *
     * @param message Message indicating the session status ("Valid session").
     */
    public ApplicationSession(String message) {
        if (message.equals("Valid session")) {
            setAccessToken();
            setExpDate();
        }
    }
    /**
     * The isSessionValid method checks if the session is valid.
     * The session is considered valid if:
     * - The token exists;
     * - The expiration time is set;
     * - The token is 16 characters long;
     * - The current date and time are less than the expiration time.
     *
     * @return true if the session is valid; false otherwise.
     */
    public boolean isSessionValid() {
        return this.accessToken != null
                && this.expDate != null
                && this.accessToken.length() == 16
                && this.expDate.after(new Date());
    }
    /**
     * The setAccessToken method generates a random token of 16 characters.
     * The characters used are lowercase alphabetic letters and numbers.
     */
    private void setAccessToken() {
        String symbols = "abcdefghijklmnopqrstuvwxyz0123456789";
        this.accessToken = new Random()
                .ints(16, 0, symbols.length())
                .mapToObj(symbols::charAt)
                .map(Object::toString)
                .collect(Collectors.joining());

    }
    /**
     * The setExpDate method sets the token expiration date.
     * The session will be active for 1 minute from the moment it is created.
     */
    private void setExpDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, 1);
        this.expDate = calendar.getTime();
    }
}
