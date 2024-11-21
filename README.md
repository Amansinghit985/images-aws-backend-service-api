# Project
-> This springboot framework and apis created in the framework uploads/retrieve/filter/delete images in AWS S3 and images metadata in AWS DynamoDB (NoSQL)

### Steps
1. Clone the project https://github.com/Amansinghit985/images-aws-backend-service-api.git/
2. Project is running in localhost:8080
3. The APIs created in the framework uploads/retrieve/filter/delete images in AWS S3 and images metadata in AWS DynamoDB (NoSQL).
4. Create IAM account, AWS S3 bucket and AWS DynamoDB table in AWS and enter the values of bucketName, accessKeyId, secretAccessKey, tableName in application.properties file and update the value of table in ImageMetaData class.
5. Springboot app can be executed using mvn spring-boot:run or from IDE
6. Various APIs have been created
   POST - /api/images/upload - to upload images and storing it's metadata
   GET - /api/images/{id} - get image and it's metadata by id
   GET - /api/images - get list of all images and it's metadata
   DELETE - /api/images/{id} - Delete image and it's metadata by id
   DELETE - /api/images - Delete all images and it's metada
   GET - /api/images/filter - Filter images and it's metada by various fields e.x. fileName, fileFormat, uploadDate, fileType
   DELETE - /api/images/filter - Filter images and it's metada by various fields e.x. fileName, fileFormat, uploadDate, fileType and then delete all those images and it's metadata
7. Postman collection has also been uploaded where various APIs can be executed and verified   
