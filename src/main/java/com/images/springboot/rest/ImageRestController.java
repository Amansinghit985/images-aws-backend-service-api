package com.images.springboot.rest;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.images.springboot.model.ImageMetaData;
import com.images.springboot.service.ImageService;

@RestController
@RequestMapping("/api/images")
public class ImageRestController {

	@Autowired
	private ImageService imageService;

	@PostMapping("/upload")
	public ResponseEntity<String> uploadImageAndSaveMetaData(@RequestParam("file") MultipartFile file,
			@RequestParam("metadata") String metadata) {
		String imageId = imageService.uploadImageAndSaveMetaDataService(file, metadata);
		return ResponseEntity.ok("Image uploaded successfully with ID: " + imageId);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ImageMetaData> getMetadata(@PathVariable String id) {
		return ResponseEntity.ok(imageService.getMetadataService(id));
	}

	@GetMapping
	public ResponseEntity<List<ImageMetaData>> getAllMetadata() {
		List<ImageMetaData> imageMetaDataList = imageService.getAllMetadataService();
		return ResponseEntity.ok(imageMetaDataList);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteImage(@PathVariable String id) {
		imageService.deleteImageAndMetaDataService(id);
		return ResponseEntity.noContent().build(); 
	}

	@DeleteMapping
	public ResponseEntity<Void> deleteAllImages() {
		imageService.deleteAllImagesAndMetaDataService();
		return ResponseEntity.noContent().build(); 
	}

	@GetMapping("/filter")
	public ResponseEntity<List<ImageMetaData>> filterMetadata(@RequestParam Map<String, String> filters) {
		List<ImageMetaData> filteredMetadata = imageService.filterMetadataService(filters);
		return ResponseEntity.ok(filteredMetadata);
	}

	@DeleteMapping("/filter")
	public ResponseEntity<Void> filterAndDeleteMetadata(@RequestParam Map<String, String> filters) {
		imageService.filterDeleteMetadataAndImageService(filters);
		return ResponseEntity.noContent().build();
	}

}
