package com.images.springboot.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@DynamoDBTable(tableName = "Image-Metadata")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageMetaData {

	@DynamoDBHashKey(attributeName = "id")
	private String id;

	@DynamoDBAttribute(attributeName = "fileName")
	private String fileName;

	@DynamoDBAttribute(attributeName = "fileFormat")
	private String fileFormat;

	@DynamoDBAttribute(attributeName = "uploadDate")
	private String uploadDate;

	@DynamoDBAttribute(attributeName = "uploadTime")
	private String uploadTime;

	@DynamoDBAttribute(attributeName = "fileType")
	private String fileType;

	@DynamoDBAttribute(attributeName = "objectUrl")
	private String objectUrl;

}
