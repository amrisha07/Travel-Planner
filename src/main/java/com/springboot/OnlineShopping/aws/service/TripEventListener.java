package com.springboot.OnlineShopping.aws.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@EnableScheduling // Enables background scheduling capabilities
public class TripEventListener {

    private final SqsClient sqsClient;
    private static final String QUEUE_URL = "http://localhost:4566/000000000000/travel-itinerary-queue";

    // Checks the SQS queue every 3 seconds for new messages
    @Scheduled(fixedDelay = 3000)
    public void listenToTripEvents() {
        try {
            ReceiveMessageRequest receiveRequest = ReceiveMessageRequest.builder()
                    .queueUrl(QUEUE_URL)
                    .maxNumberOfMessages(5)
                    .waitTimeSeconds(1) // Long-polling
                    .build();

            List<Message> messages = sqsClient.receiveMessage(receiveRequest).messages();

            for (Message message : messages) {
                log.info("📥 INTERCEPTED EVENT FROM SQS QUEUE! Raw Content:\n{}", message.body());

                // Delete the message from the queue after processing so it doesn't process again
                sqsClient.deleteMessage(builder -> builder.queueUrl(QUEUE_URL).receiptHandle(message.receiptHandle()));
            }
        } catch (Exception _) {
            // Silence noise during early container boots
        }
    }
}