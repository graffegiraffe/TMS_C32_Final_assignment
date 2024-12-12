package by.financialstatements.model.login_service.tfa;

import by.financialstatements.model.exception.LoginFailedException;
import by.financialstatements.model.log.CustomLogger;
import com.google.zxing.WriterException;

import java.io.IOException;
import java.util.Scanner;
/**
 * The TFALaunch class is responsible for starting the two-factor authentication (2FA) process.
 * It generates a QR code, verifies the entered code, and returns the authentication result.
 */
public class TFALaunch {
    /**
     * The launchTFA method starts the two-factor authentication check.
     *
     * Execution steps:
     * - Generates a secret key, email address, and company name.
     * - Creates a URL for Google Authenticator and generates a QR code based on it.
     * - Waits for the user to enter a code.
     * - Compares the entered code with the calculated one-time code.
     * - Returns true if the code is correct, or false if the code is incorrect.
     *
     * @return true if two-factor authentication is successful; false if an error occurs.
     */
    public static boolean launchTFA() {
        // Data for 2FA
        String secretKey = "QDWSM3OYBPGTEVSPB5FKVDM3CSNCWHVK";
        String email = "ilya_kate@gmail.com";
        String companyName = "Kate's and Ilya's corp.";
        // Generate URL for Google Authenticator
        String barCodeUrl = TFAUtils.getGoogleAuthenticatorBarCode(secretKey, email, companyName);

        try {
            // Generate QR code
            TFAUtils.createQRCode(barCodeUrl, "src/main/resources/QRCode.png", 400, 400);
        } catch (WriterException | IOException e) {
            CustomLogger.error("Error creating QR code: " + e.getMessage(), e);
            throw new RuntimeException("Error creating QR code", e);
        }
        // Waiting for the user to enter the code
        System.out.print("Please enter 2fA code here: ");
        Scanner scanner = new Scanner(System.in);
        String code = scanner.nextLine();
        // Checking the entered code for correctness
        if (code.equals(TFAUtils.getTOTPCode(secretKey))) {
            CustomLogger.info("Two-factor authentication completed successfully");
            return true;
        } else {
            LoginFailedException loginFailedException = new LoginFailedException("Error: Invalid authentication code", new Throwable("Invalid code"));
            CustomLogger.error(loginFailedException.getMessage(), loginFailedException);
            return false;
        }
    }
}