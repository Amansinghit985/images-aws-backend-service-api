package com.images.springboot.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.images.springboot.model.ImageMetaData;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ImageService {

	private final DynamoDBMapper dynamoDBMapper;
	private final AwsS3Service awsS3Service;

	@Value("${aws.region}")
	private String region;

	@Value("${aws.s3.bucketName}")
	private String bucketName;

	@Autowired
	public ImageService(DynamoDBMapper dynamoDBMapper, AwsS3Service awsS3Service) {
		this.dynamoDBMapper = dynamoDBMapper;
		this.awsS3Service = awsS3Service;
	}

	public ImageMetaData getMetadataService(String id) {
		ImageMetaData imageMetaData = dynamoDBMapper.load(ImageMetaData.class, id);
		if (imageMetaData == null) {
			throw new EntityNotFoundException("Metadata not found");
		}
		return imageMetaData;
	}

	public String uploadImageAndSaveMetaDataService(MultipartFile file, String fileType) {
		String uniqueId = UUID.randomUUID().toString();
		String fileName = file.getOriginalFilename();
		LocalDateTime now = LocalDateTime.now();
		String uploadDate = now.toLocalDate().format(DateTimeFormatter.ISO_LOCAL_DATE);
		String uploadTime = now.toLocalTime().format(DateTimeFormatter.ISO_LOCAL_TIME);
		String fileFormat = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
		String objectUrl = String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, uniqueId);
		;

		ImageMetaData imageMetaData = ImageMetaData.builder().id(uniqueId).fileName(fileName).fileType(fileType)
				.fileFormat(fileFormat).uploadDate(uploadDate).uploadTime(uploadTime).objectUrl(objectUrl).build();

		dynamoDBMapper.save(imageMetaData);

		awsS3Service.uploadToS3(uniqueId, file);

		return uniqueId;
	}

	public List<ImageMetaData> getAllMetadataService() {
		return dynamoDBMapper.scan(ImageMetaData.class, new DynamoDBScanExpression());
	}

	public void deleteImageAndMetaDataService(String id) {
		ImageMetaData imageMetaData = dynamoDBMapper.load(ImageMetaData.class, id);
		if (imageMetaData == null) {
			throw new EntityNotFoundException("Metadata not found");
		}

		awsS3Service.deleteFromS3(id);

		dynamoDBMapper.delete(imageMetaData);
	}

	public void deleteAllImagesAndMetaDataService() {

		List<ImageMetaData> imageMetaDataList = getAllMetadataService();

		deleteListOfMetaDataAndImages(imageMetaDataList);
	}

	public List<ImageMetaData> filterMetadataService(Map<String, String> filters) {

		Map<String, AttributeValue> eav = new HashMap<>();
		StringBuilder filterExpression = new StringBuilder();

		filters.forEach((key, value) -> {
			if (filterExpression.length() > 0) {
				filterExpression.append(" and ");
			}
			filterExpression.append(key).append(" = :").append(key);
			eav.put(":" + key, new AttributeValue().withS(value));
		});

		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
				.withFilterExpression(filterExpression.toString()).withExpressionAttributeValues(eav);

		return dynamoDBMapper.scan(ImageMetaData.class, scanExpression);
	}

	public void filterDeleteMetadataAndImageService(Map<String, String> filters) {
		List<ImageMetaData> imageMetaDataList = filterMetadataService(filters);
		deleteListOfMetaDataAndImages(imageMetaDataList);

	}

	public void deleteListOfMetaDataAndImages(List<ImageMetaData> imageMetaDataList) {

		for (ImageMetaData imageMetaData : imageMetaDataList) {

			String id = imageMetaData.getId();
			awsS3Service.deleteFromS3(id);

			dynamoDBMapper.delete(imageMetaData);
		}
	}

}
