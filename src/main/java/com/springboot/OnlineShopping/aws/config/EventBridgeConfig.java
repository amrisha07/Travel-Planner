package com.springboot.OnlineShopping.aws.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.eventbridge.EventBridgeClient;
import software.amazon.awssdk.services.sqs.SqsClient;

import java.net.URI;

@Configuration
public class EventBridgeConfig {

    private static final String ENDPOINT = "http://localhost:4566";
    private static final Region REGION = Region.US_EAST_1;
    private static final StaticCredentialsProvider CREDENTIALS = StaticCredentialsProvider.create(
            AwsBasicCredentials.create("dummy-access-key", "dummy-secret-key")
    );


    @Bean
    public EventBridgeClient eventBridgeClient() {
        return EventBridgeClient.builder()
                .endpointOverride(URI.create(ENDPOINT))
                .region(REGION)
                .credentialsProvider(CREDENTIALS)
                .build();
    }

    @Bean
    public SqsClient sqsClient() {
        return SqsClient.builder()
                .endpointOverride(URI.create(ENDPOINT))
                .region(REGION)
                .credentialsProvider(CREDENTIALS)
                .build();
    }
}
