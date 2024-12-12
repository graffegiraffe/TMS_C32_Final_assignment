package by.financialstatements.model.log;

import by.financialstatements.model.login_service.service.PropsHandler;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
/**
 * The CustomLogger class is responsible for logging messages of different levels (INFO, WARNING, ERROR) to files.
 * It provides convenient methods for writing logs with the current timestamp, message, and, optionally, the exception stack.
 * Logging is divided into files depending on the level: information and warning messages are saved to one file,
 * and error messages to another.
 */
public class CustomLogger {
    private static final String INFO_LOG_FILE = PropsHandler.getPropertyFromConfig("INFO_LOG_FILE");
    private static final String ERROR_LOG_FILE = PropsHandler.getPropertyFromConfig("ERROR_LOG_FILE");
    /**
     * The log method writes a log to a file.
     * It formats the message with a timestamp, log level, and optionally an exception trail.
     *
     * @param level Log level (INFO, WARNING, ERROR).
     * @param message Message to write to the log.
     * @param throwable Exception whose stack is also written (may be null).
     * @param logFile File to write the log to.
     */
    public static void log(String level, String message, Throwable throwable, String logFile) {
        // Format timestamp and message
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String logMessage = String.format("%s [%s] %s", timestamp, level.toUpperCase(), message);
        // If there is a throwable, include its stack in the message
        if (throwable != null) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            throwable.printStackTrace(pw);
            logMessage += "\n" + sw.toString();
        }
        // Write message to file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true))) {
            writer.write(logMessage + System.lineSeparator());
        } catch (IOException e) {
            System.err.println("Error writing to file " + logFile + ": " + e.getMessage());
        }
    }
    /**
     * Logging of information messages (INFO).
     * @param message Message to log.
     */
    public static void info(String message) {
        log("INFO", message, null, INFO_LOG_FILE);
    }
    /**
     * Logging of warning messages (WARNING).
     * @param message Message to log.
     */
    public static void warning(String message) {
        log("WARNING", message, null, INFO_LOG_FILE);
    }
    /**
     * Log error messages (ERROR).
     * @param message Message to log.
     */
    public static void error(String message) {
        log("ERROR", message, null, ERROR_LOG_FILE);
    }
    /**
     * Log error messages (ERROR) with exception included.
     * @param message Message to log.
     * @param throwable Exception to be included in the log.
     */
    public static void error(String message, Throwable throwable) {
        log("ERROR", message, throwable, ERROR_LOG_FILE);
    }
}