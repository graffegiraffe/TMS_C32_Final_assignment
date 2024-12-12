package by.financialstatements;

import by.financialstatements.model.fileprocessing.service.FileProcessor;
import by.financialstatements.model.login_service.service.AuthService;
import by.financialstatements.model.login_service.session.ApplicationSession;

import java.util.Scanner;
/**
 * The Main class is the entry point to the application.
 *
 * Program structure:
 * - A request for user credentials (login and password) is made.
 * - Authorization is checked via the authorization service.
 * - If authorization is successful, the option to specify the path to the data folder is provided.
 * - Data processing is started via `FileProcessor`.
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // Debug code for encrypting credentials
        /* String resultLogin = LoginEncryptor.encrypt("kate");
        System.out.println(resultLogin);
        String resultPassword = LoginEncryptor.encrypt("2511");
        System.out.println(resultPassword);*/
        System.out.print("Enter login: ");
        String login = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        // User authentication via AuthService
        ApplicationSession session = AuthService.auth(login, password);
        // Check if the session is valid
        if (session.isSessionValid()) {
            System.out.print("Enter the path to the data folder: ");
            String dataPath = scanner.nextLine();
            FileProcessor processor = new FileProcessor(dataPath);
            processor.process(session);
        }
        scanner.close();
    }
}