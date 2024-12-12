package by.financialstatements.model.statistics;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import by.financialstatements.model.log.CustomLogger;
import by.financialstatements.model.login_service.service.PropsHandler;
/**
 * The StatisticsWriter class is responsible for writing the calculated statistics to a file.
 * Uses configuration properties to determine the path to the file.
 */
public class StatisticsWriter {
    private static final String STATISTICS_FILE = PropsHandler.getPropertyFromConfig("STATISTICS_FILE");
    /**
     * The writeStatisticsToFile method writes statistics to a text file.
     *
     * @param statistics A statistics map with keys ("invoices", "orders", "checks")
     * and their corresponding values.
     */
    public void writeStatisticsToFile(Map<String, Double> statistics) {
        try {
            String stats = String.format(
                    "Statistics:%n" +
                            "  - Total turnover for all invoices: %.2f%n" +
                            "  - Total turnover for all orders: %.2f%n" +
                            "  - Total turnover for all checks: %.2f%n",
                    statistics.get("invoices"),
                    statistics.get("orders"),
                    statistics.get("checks")
            );
            // Write a line to a file
            Files.write(Paths.get(STATISTICS_FILE), stats.getBytes());
            CustomLogger.info("Statistics successfully written to file " + STATISTICS_FILE);
        } catch (IOException e) {
            CustomLogger.error("Error writing statistics file: " + e.getMessage());
        }
    }
}