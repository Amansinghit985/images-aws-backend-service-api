package com.images.springboot.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;

@Service
public class AwsS3Service {

	private final S3Client s3Client;
	@Value("${aws.s3.bucketName}")
	private String bucketName;

	@Autowired
	public AwsS3Service(S3Client s3Client) {
		this.s3Client = s3Client;
	}

	public void uploadToS3(String fileName, MultipartFile file) {
		try {
			s3Client.putObject(builder -> builder.bucket(bucketName).key(fileName),
					RequestBody.fromBytes(file.getBytes()));
		} catch (IOException e) {
			throw new RuntimeException("Failed to upload image to S3", e);
		}
	}

	public void deleteFromS3(String fileName) {
		DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder().bucket(bucketName).key(fileName)
				.build();

		s3Client.deleteObject(deleteObjectRequest);
	}

}
