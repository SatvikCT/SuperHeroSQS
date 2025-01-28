package org.example.superheroes.services;

import org.example.superheroes.config.SqsConfig;
import org.example.superheroes.models.Superhero;
import org.example.superheroes.repositories.SuperheroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse;

import java.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;


@Service
public class SuperheroQueueConsumer {
    @Autowired
    private SqsClient sqsClient;

    @Autowired
    private SqsConfig sqsConfig;

    @Autowired
    private SuperheroRepository superheroRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // Continuous polling of the queue
    public void consumeQueue() {
        while (true) {
            try {
                // Poll messages from the queue
                ReceiveMessageResponse response = sqsClient.receiveMessage(ReceiveMessageRequest.builder()
                        .queueUrl(sqsConfig.getQueueUrl())
                        .maxNumberOfMessages(10)
                        .build());

                List<String> messages = response.messages().stream()
                        .map(message -> {
                            // Process each message
                            handleMessage(message.body());

                            // Delete the message from the queue after processing
                            sqsClient.deleteMessage(DeleteMessageRequest.builder()
                                    .queueUrl(sqsConfig.getQueueUrl())
                                    .receiptHandle(message.receiptHandle())
                                    .build());

                            return message.body();
                        }).toList();

                // Sleep if no messages are received
                if (messages.isEmpty()) {
                    Thread.sleep(5000); // Sleep for 5 seconds before polling again
                }
            } catch (Exception e) {
                System.err.println("Error consuming queue: " + e.getMessage());
            }
        }
    }

    // Handle individual messages
//    private void handleMessage(String superheroName) {
//        // Check if the superhero exists in the database
//        Superhero superhero = superheroRepository.findByName(superheroName);
//
//        if (superhero == null) {
//            // Superhero not found
//            System.out.println("Superhero with name '" + superheroName + "' not found in the database.");
//        } else {
//            // Superhero found, mark as true
//            superhero.setMarked(true);
//            superheroRepository.save(superhero);
//            System.out.println("Superhero '" + superheroName + "' marked as true in the database.");
//        }
//    }

    private void handleMessage(String messageBody) {
        try {
            // Parse the message body into a map
            Map<String, Object> updateRequest = objectMapper.readValue(messageBody, Map.class);

            // Extract the superhero name
            String superheroName = (String) updateRequest.get("name");

            // Find the superhero in the database
            Superhero superhero = superheroRepository.findByName(superheroName);

            if (superhero == null) {
                System.out.println("Superhero with name '" + superheroName + "' not found in the database.");
            } else {
                // Update the superhero's properties dynamically
                updateRequest.forEach((key, value) -> {
                    switch (key) {
                        case "power":
                            superhero.setPower((String) value);
                            break;
                        case "age":
                            superhero.setAge((Integer) value);
                            break;
                        case "gender":
                            superhero.setGender((String) value);
                            break;
                        case "universe":
                            superhero.setUniverse((String) value);
                            break;
                        case "marked":
                            superhero.setMarked((Boolean) value);
                            break;
                        default:
                            // Ignore unrecognized properties
                            break;
                    }
                });

                // Save the updated superhero to the database
                superheroRepository.save(superhero);
                System.out.println("Superhero '" + superheroName + "' updated with changes: " + updateRequest);
            }
        } catch (Exception e) {
            System.err.println("Error processing message: " + e.getMessage());
        }
    }
}
