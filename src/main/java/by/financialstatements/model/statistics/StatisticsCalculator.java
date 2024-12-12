package by.financialstatements.model.statistics;

import by.financialstatements.model.log.CustomLogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * The StatisticsCalculator class is used to calculate financial statistics,
 * such as invoice, order, and receipt data.
 * It parses a list of files, extracts the required data, and stores the results.
 */
public class StatisticsCalculator {
    private final Map<String, Double> statistics;
    /**
     * StatisticsCalculator constructor. Initializes statistics to default values ​​(0.0).
     */
    public StatisticsCalculator() {
        this.statistics = new HashMap<>();
        statistics.put("invoices", 0.0);
        statistics.put("orders", 0.0);
        statistics.put("checks", 0.0);
    }
    /**
     * The calculateStatistics method calculates statistics based on the provided files.
     *
     * Execution steps:
     * - Reads the last line of each file.
     * - Determines the file type (invoice, order, receipt).
     * - Extracts data and adds it to the appropriate statistics category.
     * - Logs read errors or invalid data.
     *
     * @param validFiles List of files that contain valid data.
     */
    public void calculateStatistics(List<Path> validFiles) {
        for (Path file : validFiles) {
            try (BufferedReader reader = Files.newBufferedReader(file)) {
                String currentLine;
                String lastLine = null;
                // Read the last line of the file
                while ((currentLine = reader.readLine()) != null) {
                    lastLine = currentLine;
                }
                CustomLogger.info("Last line of the file " + file.getFileName() + ": " + lastLine);
                if (lastLine != null && !lastLine.isEmpty()) {
                    String fileName = file.getFileName().toString();
                    double amount;
                    // Determining the file type and processing data
                    switch (getFileType(fileName)) {
                        case "invoice":
                            amount = parseAmount(lastLine);
                            statistics.put("invoices", statistics.get("invoices") + amount);
                            CustomLogger.info("Added to invoices: " + amount);
                            break;
                        case "order":
                            amount = parseOrderAmount(lastLine);
                            statistics.put("orders", statistics.get("orders") + amount);
                            CustomLogger.info("Added to orders: " + amount);
                            break;
                        case "check":
                            amount = parseAmount(lastLine);
                            statistics.put("checks", statistics.get("checks") + amount);
                            CustomLogger.info("Added to checks: " + amount);
                            break;
                        default:
                            CustomLogger.error("Unknown file type for " + fileName);
                    }
                }
            } catch (IOException e) {
                CustomLogger.error("Error reading file: " + file.getFileName() + " " + e.getMessage());
            }
        }
    }
    /**
     * The getStatistics method returns the calculated statistics.
     *
     * @return Map with statistics (categories and their values).
     */
    public Map<String, Double> getStatistics() {
        return statistics;
    }
    /**
     * The parseAmount method extracts a numeric value (amount) from a string.
     *
     * @param line The last line from the file.
     * @return The extracted value as a double, or 0.0 if the format is invalid.
     */
    private double parseAmount(String line) {
        try {
            String number = line.replaceAll("[^0-9,\\.]", "");
            double parsedAmount = Double.parseDouble(number.replace(',', '.'));
            CustomLogger.info("Extracted amount: " + parsedAmount);
            return parsedAmount;
        } catch (NumberFormatException e) {
            CustomLogger.error("Invalid number format: " + line);
            return 0.0;
        }
    }
    /**
     * The parseOrderAmount method extracts the value for orders (the "Order Total" field).
     *
     * @param line The last line from the file.
     * @return The extracted value as a double, or 0.0 if the format is invalid.
     */
    private double parseOrderAmount(String line) {
        try {
            String pattern = "(?<=Order Total\\s)(\\d{1,3}(?:,\\d{3})*\\.\\d+)";
            Pattern regex = Pattern.compile(pattern);
            Matcher matcher = regex.matcher(line);
            if (matcher.find()) {
                String number = matcher.group(1).replace(",", "");
                double parsedAmount = Double.parseDouble(number);
                CustomLogger.info("Extracted amount for order: " + parsedAmount);
                return parsedAmount;
            } else {
                throw new NumberFormatException("Number not found in line: " + line);
            }
        } catch (NumberFormatException e) {
            CustomLogger.error("Invalid number format: " + line);
            return 0.0;
        }
    }
    /**
     * The getFileType method determines the file type based on its name.
     *
     * File types:
     * - "invoice" for invoices.
     * - "order" for orders.
     * - "check" for checks.
     * - "unknown" for any other format.
     *
     * @param fileName The file name.
     * @return The file type as a string.
     */
    private String getFileType(String fileName) {
        String lowerCaseFileName = fileName.toLowerCase();
        if (lowerCaseFileName.contains("invoice")) {
            return "invoice";
        } else if (lowerCaseFileName.contains("order")) {
            return "order";
        } else if (lowerCaseFileName.contains("electric_bill")) {
            return "check";
        }
        return "unknown";
    }
}