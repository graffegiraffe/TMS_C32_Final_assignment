package by.financialstatements.model.login_service.service;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.File;
import java.nio.file.Path;
/**
 * The AWS class provides functionality for integrating with Amazon S3.
 * It implements a method that sends a statistics file to the specified bucket on Amazon S3 using the provided credentials.
 */
public class AWS {
    /**
     * AWS access key.
     */
    private static final String accessKey = "AAAAA";
    /**
     * AWS secret key.
     */
    private static final String secretKey = "BBBBB";
    /**
     * The name of the S3 bucket where the data will be uploaded.
     */
    private static final String bucketName = "reports-tms";
    /**
     * Region to which the S3 bucket is bound.
     */
    private static final  String regionName = "eu-north-1";
    /**
     * The throwToAmazon method sends a stats file to the specified S3 bucket.
     * It:
     * - Retrieves the stats file, the path to which is specified in the configuration.
     * - Creates an S3 client with the specified credentials and region.
     * - Generates a PutObjectRequest` to upload a file to the bucket.
     * - Performs the file upload and receives a response from Amazon S3.
     */
    public static void throwToAmazon() {
        // File name in S3 bucket
        String key = "statistics_ilya_kate.txt";
        // Statistics file obtained from configuration
        File file = new File(PropsHandler.getPropertyFromConfig("STATISTICS_FILE"));
        // Create AWS credentials
        AwsCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
        // Create S3 client
        S3Client s3Client = S3Client
                .builder()
                .region(Region.of(regionName))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
        // Generate a request to download a file
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
        // Upload file to S3
        PutObjectResponse response = s3Client.putObject(request, Path.of(file.toURI()));
    }
}
