package com.springboot.OnlineShopping.aws.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.eventbridge.EventBridgeClient;

import java.net.URI;

@Configuration
public class EventBridgeConfig {

    @Bean
    public EventBridgeClient eventBridgeClient() {
        return EventBridgeClient.builder()
                // Force the client to talk directly to your running LocalStack container
                .endpointOverride(URI.create("http://localhost:4566"))
                .region(Region.US_EAST_1)
                // LocalStack requires dummy credentials so the SDK doesn't throw a validation error
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create("dummy-access-key", "dummy-secret-key")
                ))
                .build();
    }
}
