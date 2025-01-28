package org.example.superheroes.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class QueueConsumerRunner {

    @Autowired
    private SuperheroQueueConsumer queueConsumer;

    @PostConstruct
    public void startConsumer() {
        new Thread(() -> queueConsumer.consumeQueue()).start(); // Run in a separate thread
    }
}
