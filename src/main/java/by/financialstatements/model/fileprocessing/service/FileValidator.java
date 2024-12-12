package by.financialstatements.model.fileprocessing.service;

import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.io.IOException;

import by.financialstatements.model.exception.FileMovementException;
import by.financialstatements.model.log.CustomLogger;
import by.financialstatements.model.login_service.service.PropsHandler;
/**
 * The FileValidator class is designed to check files for validity.
 * It determines whether a file is valid based on its name, extension, and the content of certain keywords.
 * If the file is invalid, it is moved to the invalid folder.
 */
public class FileValidator {

    private static final String INVALID_PATH = PropsHandler.getPropertyFromConfig("INVALID_PATH");
    /**
     * The isValid method checks if a file is valid based on the following criteria:
     * - The file must have the `.txt` extension.
     * - The file name must contain `2024` in the title.
     * - The file name must contain one of the keywords: `invoice`, `order`, `electric_bill`.
     * If the file does not meet these conditions, it is moved to the directory for invalid files.
     *
     * @param file Path to the file to check.
     * @return true if the file is valid, otherwise false.
     * @throws FileMovementException Thrown if an error occurs while moving an invalid file.
     */
    public boolean isValid(Path file) {
        // Get file name in lowercase
        String fileName = file.getFileName().toString().toLowerCase();
        CustomLogger.info("Checking the file" + fileName + " for compliance with validity criteria");
        // Check if the file name is valid
        boolean isValid = (fileName.endsWith(".txt") && fileName.contains("2024"))
                && (fileName.contains("invoice") || fileName.contains("order") || fileName.contains("electric_bill"));
        if (!isValid) {
            try {
                // Move file to invalid files folder
                Files.move(file, Path.of(INVALID_PATH, file.getFileName().toString()), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new FileMovementException("Error moving invalid file: " + file.getFileName(), e);
            }
            CustomLogger.warning("Unknown or invalid file type for " + fileName);
        }
        return isValid;
    }
}