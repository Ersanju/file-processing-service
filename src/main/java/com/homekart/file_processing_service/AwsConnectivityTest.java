package com.homekart.file_processing_service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.sns.SnsClient;

@Configuration
@RequiredArgsConstructor
public class AwsConnectivityTest {

    private final S3Client s3Client;
    private final DynamoDbClient dynamoDbClient;
    private final SnsClient snsClient;

    @Bean
    CommandLineRunner testAwsConnection() {

        return args -> {

            System.out.println("\n===== S3 Buckets =====");

            s3Client.listBuckets()
                    .buckets()
                    .forEach(bucket -> System.out.println(bucket.name()));

            System.out.println("\n===== DynamoDB Tables =====");

            dynamoDbClient.listTables()
                    .tableNames()
                    .forEach(System.out::println);

            System.out.println("\n===== SNS Topics =====");

            snsClient.listTopics()
                    .topics()
                    .forEach(topic -> System.out.println(topic.topicArn()));

            System.out.println("\n===== AWS Connection Successful =====");
        };
    }
}