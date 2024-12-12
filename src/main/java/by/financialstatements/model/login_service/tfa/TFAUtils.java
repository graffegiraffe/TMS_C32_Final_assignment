package by.financialstatements.model.login_service.tfa;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import de.taimos.totp.TOTP;
import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.SecureRandom;
/**
 * The TFAUtils class contains utility methods for working with two-factor authentication (2FA),
 * including generating a secret key, a one-time-password (TOTP), a URL for Google Authenticator
 * and creating QR codes.
 */
public class TFAUtils {
    /**
     * The generateSecretKey method generates a random secret key for 2FA.
     *
     * @return The secret key as a Base32 encoded string.
     */
    public static String generateSecretKey() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        Base32 base32 = new Base32();
        return base32.encodeToString(bytes);
    }
    /**
     * The getTOTPCode method generates a temporary one-time password (TOTP) based on the secret key.
     *
     * @param secretKey The secret key in Base32 format.
     * @return The one-time code as a string.
     */
    public static String getTOTPCode(String secretKey) {
        Base32 base32 = new Base32();
        byte[] bytes = base32.decode(secretKey);
        String hexKey = Hex.encodeHexString(bytes);
        return TOTP.getOTP(hexKey);
    }
    /**
     * The getGoogleAuthenticatorBarCode method generates a URL for adding an account
     * to the Google Authenticator app.
     *
     * @param secretKey Secret key for 2FA.
     * @param account Email address or account name.
     * @param issuer Company or app name.
     * @return URL in `otpauth://totp/...` format for generating QR code.
     */
    public static String getGoogleAuthenticatorBarCode(String secretKey, String account, String issuer) {
        try {
            return "otpauth://totp/"
                    + URLEncoder.encode(issuer + ":" + account, "UTF-8").replace("+", "%20")
                    + "?secret=" + URLEncoder.encode(secretKey, "UTF-8").replace("+", "%20")
                    + "&issuer=" + URLEncoder.encode(issuer, "UTF-8").replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }
    /**
     * The createQRCode method generates a QR code based on the given URL and saves it to a file.
     *
     * @param barCodeData The URL for the QR code.
     * @param filePath The path where the file with the QR code will be saved.
     * @param height The height of the QR code image.
     * @param width The width of the QR code image.
     *
     * @throws WriterException If an error occurred while creating the QR code matrix.
     * @throws IOException If an error occurred while writing the file.
     */
    public static void createQRCode(String barCodeData, String filePath, int height, int width)
            throws WriterException, IOException {
        BitMatrix matrix = new MultiFormatWriter().encode(barCodeData, BarcodeFormat.QR_CODE, width, height);
        try (FileOutputStream out = new FileOutputStream(filePath)) {
            MatrixToImageWriter.writeToStream(matrix, "png", out);
        }
    }
}
