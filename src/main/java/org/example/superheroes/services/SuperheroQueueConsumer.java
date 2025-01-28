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

import java.util.List;

@Service
public class SuperheroQueueConsumer {

    @Autowired
    private SqsClient sqsClient;

    @Autowired
    private SqsConfig sqsConfig;

    @Autowired
    private SuperheroRepository superheroRepository;

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
    private void handleMessage(String superheroName) {
        // Check if the superhero exists in the database
        Superhero superhero = superheroRepository.findByName(superheroName);

        if (superhero == null) {
            // Superhero not found
            System.out.println("Superhero with name '" + superheroName + "' not found in the database.");
        } else {
            // Superhero found, mark as true
            superhero.setMarked(true);
            superheroRepository.save(superhero);
            System.out.println("Superhero '" + superheroName + "' marked as true in the database.");
        }
    }
}
