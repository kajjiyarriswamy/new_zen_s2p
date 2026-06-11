package com.budget.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmazonS3Config {

    @Value("${aws.s3.region}")
    private String region;

    @Value("${aws.s3.endpoint:}")
    private String endpoint;

    @Value("${aws.s3.access-key}")
    private String accessKey;

    @Value("${aws.s3.secret-key}")
    private String secretKey;

    @Bean
    @Conditional(AwsS3ConfigCondition.class)
    public AmazonS3 amazonS3() {
        BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

        AmazonS3ClientBuilder builder = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials));

        boolean useEndpoint = endpoint != null && !endpoint.trim().isEmpty()
                && !endpoint.contains("{") && !endpoint.contains("}");
        boolean useRegion = region != null && !region.trim().isEmpty()
                && !region.contains("{") && !region.contains("}");

        if (useEndpoint) {
            builder.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, region));
        } else if (useRegion) {
            builder.withRegion(region);
        } else {
            throw new IllegalStateException("AWS S3 configuration is incomplete: either aws.s3.region or aws.s3.endpoint must be set to a real value.");
        }

        return builder.build();
    }
}
