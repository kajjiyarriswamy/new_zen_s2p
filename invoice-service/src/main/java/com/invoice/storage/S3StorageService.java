package com.invoice.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;

import java.io.IOException;

@Component
public class S3StorageService {

    private final AmazonS3 amazonS3;
    private final String bucketName;

    public S3StorageService(AmazonS3 amazonS3,
                            @Value("${aws.s3.bucket-name}") String bucketName) {
        this.amazonS3 = amazonS3;
        this.bucketName = bucketName;
    }

    public String uploadFile(MultipartFile file, String key) throws IOException {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        amazonS3.putObject(bucketName, key, file.getInputStream(), metadata);
        return amazonS3.getUrl(bucketName, key).toString();
    }
}
