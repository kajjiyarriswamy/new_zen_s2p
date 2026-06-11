package com.budget.config;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class AwsS3ConfigCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        Environment env = context.getEnvironment();
        String accessKey = env.getProperty("aws.s3.access-key");
        String secretKey = env.getProperty("aws.s3.secret-key");
        String region = env.getProperty("aws.s3.region");
        String endpoint = env.getProperty("aws.s3.endpoint");
        return isConfigured(accessKey) && isConfigured(secretKey) && (isConfigured(region) || isConfigured(endpoint));
    }

    private boolean isConfigured(String value) {
        return value != null && !value.trim().isEmpty() && !value.contains("{") && !value.contains("}");
    }
}
