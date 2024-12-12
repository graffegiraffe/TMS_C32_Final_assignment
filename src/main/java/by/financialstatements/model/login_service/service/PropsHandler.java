package by.financialstatements.model.login_service.service;

import by.financialstatements.model.log.CustomLogger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
/**
 * The PropsHandler class is designed to manage application configuration properties.
 * It loads settings from a configuration file and provides methods to access this data.
 */
public class PropsHandler {
    private static final String CONFIG_FILE_PATH = "src/main/resources/config.properties";
    private static Properties properties;
    // Static block for initialization
    static {
        properties = new Properties();
        try (InputStream input = new FileInputStream(CONFIG_FILE_PATH)) {
            // Load parameters from file
            properties.load(input);
        } catch (IOException e) {
            CustomLogger.error("Failed to load properties from " + CONFIG_FILE_PATH, e);
        }
    }
    /**
     * The getPropertyFromConfig method returns the value of a property by its name.
     *
     * @param propName The name of the parameter whose value you want to get.
     * @return The value of the property, or null if the property is not found.
     */
    public static String getPropertyFromConfig(String propName) {
        return properties.getProperty(propName);
    }
}
