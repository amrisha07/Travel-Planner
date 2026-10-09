package com.springboot.OnlineShopping.aws.service;

import com.springboot.OnlineShopping.aws.model.TripCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.eventbridge.EventBridgeClient;
import software.amazon.awssdk.services.eventbridge.model.PutEventsRequest;
import software.amazon.awssdk.services.eventbridge.model.PutEventsRequestEntry;
import software.amazon.awssdk.services.eventbridge.model.PutEventsResponse;
import tools.jackson.databind.ObjectMapper;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventBridgePublisher {

    private final EventBridgeClient eventBridgeClient;
    private final ObjectMapper objectMapper; // Spring automatically provides this bean

    public void publishTripCreatedEvent(TripCreatedEvent event) {
        try {
            // Convert the Java object to a clean JSON string
            String eventDetailJson = objectMapper.writeValueAsString(event);

            // Construct the single event payload entry
            PutEventsRequestEntry entry = PutEventsRequestEntry.builder()
                    .eventBusName("default")                // Using the standard default event bus
                    .source("com.travelplanner.trip")      // Identifies which application sent the event
                    .detailType("TripCreated")             // The type of action that occurred
                    .detail(eventDetailJson)               // The JSON payload containing trip info
                    .build();

            // Wrap the entry into an execution request
            PutEventsRequest request = PutEventsRequest.builder()
                    .entries(entry)
                    .build();

            // Send the event down the pipeline to LocalStack
            PutEventsResponse response = eventBridgeClient.putEvents(request);

            // Log the execution status
            log.info("Successfully sent event to EventBridge! Failed entries count: {}", response.failedEntryCount());

        } catch (Exception e) {
            log.error("Failed to publish event to Amazon EventBridge", e);
        }
    }
}
