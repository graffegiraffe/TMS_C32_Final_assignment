package by.financialstatements.model.fileprocessing.service;

import by.financialstatements.model.exception.FileMovementException;
import by.financialstatements.model.log.CustomLogger;
import by.financialstatements.model.login_service.service.AWS;
import by.financialstatements.model.login_service.service.PropsHandler;
import by.financialstatements.model.login_service.session.ApplicationSession;
import by.financialstatements.model.statistics.StatisticsCalculator;
import by.financialstatements.model.statistics.StatisticsWriter;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
/**
 * The FileProcessor class is responsible for processing files from the specified path, validating them, moving them,
 * statistical processing and transferring data to cloud services.
 * It integrates with various modules, such as logging, statistics processing,
 * file validation system, and data transfer to the cloud system (AWS).
 */
public class FileProcessor {
    private static final String VALID_PATH = PropsHandler.getPropertyFromConfig("VALID_PATH");
    private static final String INVALID_PATH = PropsHandler.getPropertyFromConfig("INVALID_PATH");

    private final String dataPath;
    private final StatisticsCalculator statisticsCalculator;
    private final StatisticsWriter statisticsWriter;
    private final FileValidator fileValidator;
    /**
     * Class constructor. Initializes the path to the data, objects for working with statistics and validation.
     *
     * @param dataPath Path to the directory with the data to be processed.
     */
    public FileProcessor(String dataPath) {
        this.dataPath = dataPath;
        this.statisticsCalculator = new StatisticsCalculator();
        this.statisticsWriter = new StatisticsWriter();
        this.fileValidator = new FileValidator();
   }
    /**
     * The process method performs the main logic of file processing:
     * - Checks if the application session exists and is valid.
     * - Walks the file tree, validating and moving files to the appropriate directories (valid/invalid).
     * - Calculates statistics for valid files.
     * - Writes statistics to a file.
     * - Transfers data to the cloud structure (AWS).
     *
     * @param session Current application session.
     */
    public void process(ApplicationSession session) {
        if (session != null) {
            if (session.isSessionValid()) {
                List<Path> validFiles = new ArrayList<>();

                try {
                    Files.walkFileTree(Paths.get(dataPath), new SimpleFileVisitor<Path>() {
                        @Override
                        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                            // Recursively traverse the file tree
                            try {
                                String fileName = file.getFileName().toString();
                                CustomLogger.info("File processing: " + fileName);
                                // File validation and moving
                                if (fileValidator.isValid(file)) {
                                    Files.move(file, Paths.get(VALID_PATH, fileName), StandardCopyOption.REPLACE_EXISTING);
                                    CustomLogger.info("VALID: File " + fileName + " successfully moved");
                                    validFiles.add(Paths.get(VALID_PATH, fileName));
                                } else {
                                    Files.move(file, Paths.get(INVALID_PATH, fileName), StandardCopyOption.REPLACE_EXISTING);
                                    CustomLogger.warning("INVALID: File " + fileName + " moved");
                                }
                            } catch (IOException e) {
                                CustomLogger.error("Error processing file: " + file.getFileName(), e);
                            } catch (FileMovementException e) {
                                CustomLogger.error(e.getMessage(), e);
                                throw e;
                            }
                            return FileVisitResult.CONTINUE;
                        }
                    });
                    // Calculate and record statistics
                    statisticsCalculator.calculateStatistics(validFiles);
                    statisticsWriter.writeStatisticsToFile(statisticsCalculator.getStatistics());
                    // Transferring data to AWS
                    AWS.throwToAmazon();
                } catch (IOException e) {
                    CustomLogger.error("File system traversal error:" + e.getMessage(), e);
                }
            } else {
                CustomLogger.error("Session is not valid", new Throwable("invalid session"));
            }
        } else {
            CustomLogger.error("Session does not exist", new Throwable("session does not exist"));
        }
    }
}