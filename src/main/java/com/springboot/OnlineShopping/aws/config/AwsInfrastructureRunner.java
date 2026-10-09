package com.springboot.OnlineShopping.aws.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.eventbridge.EventBridgeClient;
import software.amazon.awssdk.services.eventbridge.model.PutRuleRequest;
import software.amazon.awssdk.services.eventbridge.model.PutTargetsRequest;
import software.amazon.awssdk.services.eventbridge.model.Target;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.CreateQueueRequest;

@Component
@Slf4j
@RequiredArgsConstructor
public class AwsInfrastructureRunner implements CommandLineRunner {

    private final SqsClient sqsClient;
    private final EventBridgeClient eventBridgeClient;

    @Override
    public void run(String... args) {
        try {
            // 1. Create a Queue named "travel-itinerary-queue"
            sqsClient.createQueue(CreateQueueRequest.builder().queueName("travel-itinerary-queue").build());
            String queueArn = "arn:aws:sqs:us-east-1:000000000000:travel-itinerary-queue";

            // 2. Create an EventBridge Rule
            String ruleName = "CatchTripCreatedEvents";
            eventBridgeClient.putRule(PutRuleRequest.builder()
                    .name(ruleName)
                    .eventPattern("{\"source\":[\"com.travelplanner.trip\"]}")
                    .build());

            // 3. Link the Rule to our new SQS Queue
            eventBridgeClient.putTargets(PutTargetsRequest.builder()
                    .rule(ruleName)
                    .targets(Target.builder().id("sqs-target-1").arn(queueArn).build())
                    .build());

            log.info("🎉 LocalStack Automation Success: EventBridge Rule routed to SQS Queue!");
        } catch (Exception e) {
            log.error("Failed to automate local AWS setup: {}", e.getMessage());
        }
    }
}
