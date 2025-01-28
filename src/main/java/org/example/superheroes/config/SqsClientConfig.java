package org.example.superheroes.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import java.net.URI;

@Configuration
public class SqsClientConfig {

    private final SqsConfig sqsConfig;

    public SqsClientConfig(SqsConfig sqsConfig) {
        this.sqsConfig = sqsConfig;
    }

    @Bean
    public SqsClient sqsClient() {
        return SqsClient.builder()
                .region(Region.of(sqsConfig.getRegion())) // Use configured region
                .endpointOverride(URI.create("http://localhost:4566")) // LocalStack endpoint
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsSessionCredentials.create(
                                sqsConfig.getAccessKey(),
                                sqsConfig.getSecretKey(),
                                sqsConfig.getSessionToken()
                        )
                ))
                .build();
    }
}
