package com.user.controller;


import com.user.Entity.UserDetailsEntity;
import com.user.Repository.UserMappingRepository;
import com.user.dto.ApiResponse;
import com.user.dto.OrgMap;
import com.user.dto.RoleMap;
import com.user.dto.UserDetails;
import com.user.dto.UserVendorMap;
import com.user.kafka.KafkaProducer;
import com.user.service.UserMappingService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/user/mapping")
public class UserMappingController {
	

   private static final Logger Logger=LoggerFactory.getLogger(UserMappingController.class);
   
   private final UserMappingService userMappingService;
   private final KafkaProducer kafkaProducer;
   private final UserMappingRepository userMappingRepository;
   private final com.user.storage.S3StorageService s3StorageService;
   
   public UserMappingController(UserMappingService userMappingService, KafkaProducer kafkaProducer, UserMappingRepository userMappingRepository, com.user.storage.S3StorageService s3StorageService) {
	   
	   this.userMappingService = userMappingService;
	   this.kafkaProducer = kafkaProducer;
	   this.userMappingRepository = userMappingRepository;
	   this.s3StorageService = s3StorageService;
   }

    @GetMapping("/org/{userId}")
    public ResponseEntity<ApiResponse<List<OrgMap>>> getOrgMappings(@PathVariable String userId) {
        ApiResponse<List<OrgMap>> resp = userMappingService.getOrgMappings(userId);
        return ResponseEntity.status(resp.getStatusCode()).body(resp);
    }

    @GetMapping("/role/{userId}")
    public ResponseEntity<ApiResponse<List<RoleMap>>> getRoleMappings(@PathVariable String userId) {
        ApiResponse<List<RoleMap>> resp = userMappingService.getRoleMappings(userId);
        return ResponseEntity.status(resp.getStatusCode()).body(resp);
    }

    @GetMapping("/vendor/{userId}")
    public ResponseEntity<ApiResponse<List<UserVendorMap>>> getVendorMappings(@PathVariable String userId) {
        ApiResponse<List<UserVendorMap>> resp = userMappingService.getVendorMappings(userId);
        return ResponseEntity.status(resp.getStatusCode()).body(resp);
    }

    @PostMapping("/org/{userId}/{orgId}")
    public ResponseEntity<ApiResponse<OrgMap>> linkUserToOrg(@PathVariable String userId,
                                                             @PathVariable String orgId) {
        ApiResponse<OrgMap> resp = userMappingService.linkUserToOrg(userId, orgId);
        return ResponseEntity.status(resp.getStatusCode()).body(resp);
    }

    @PostMapping("/role/{userId}/{roleId}")
    public ResponseEntity<ApiResponse<RoleMap>> linkUserToRole(@PathVariable String userId,
                                                               @PathVariable String roleId) {
        ApiResponse<RoleMap> resp = userMappingService.linkUserToRole(userId, roleId);
        return ResponseEntity.status(resp.getStatusCode()).body(resp);
    }

    @PostMapping("/vendor/{userId}/{vendorId}")
    public ResponseEntity<ApiResponse<UserVendorMap>> linkUserToVendor(@PathVariable String userId,
                                                                       @PathVariable String vendorId) {
        ApiResponse<UserVendorMap> resp = userMappingService.linkUserToVendor(userId, vendorId);
        return ResponseEntity.status(resp.getStatusCode()).body(resp);
    }
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<UserDetailsEntity>> createUser(
            @RequestBody UserDetailsEntity user) {

        try {

            UserDetailsEntity savedUser = userMappingRepository.save(user);

            return ResponseEntity.ok(
                    ApiResponse.success(savedUser,
                            "User created successfully"));

        } catch (Exception e) {

            Logger.error("Failed to create user", e);

            return ResponseEntity.status(500)
                    .body(ApiResponse.internalError(
                            "Failed to create user: " + e.getMessage()));
        }
    }
    @PostMapping("/{userId}/upload-document")
    public ResponseEntity<ApiResponse<UserDetailsEntity>> uploadUserDocument(
            @PathVariable String userId,
            @RequestParam("file") MultipartFile file) {

        Logger.info("Uploading document for user {}", userId);

        if (userId == null || userId.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.badRequest("User id is required"));
        }

        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.badRequest("Document file is required"));
        }

        Optional<UserDetailsEntity> opt =
                userMappingRepository.findByUserId(userId);

        if (!opt.isPresent()) {
            return ResponseEntity.status(404)
                    .body(ApiResponse.notFound("User not found"));
        }

        try {

            String objectKey = String.format(
                    "user/%s/%s_%s",
                    userId,
                    UUID.randomUUID(),
                    file.getOriginalFilename());

            String documentPath =
                    s3StorageService.uploadFile(file, objectKey);

            UserDetailsEntity entity = opt.get();

            entity.setDocumentPath(documentPath);
            entity.setLastUpdatedTime(LocalDateTime.now().toString());

            UserDetailsEntity savedEntity =
                    userMappingRepository.save(entity);

            return ResponseEntity.ok(
                    ApiResponse.success(
                            savedEntity,
                            "User document uploaded successfully"));

        } catch (Exception e) {

            e.printStackTrace();

            Logger.error("Failed to upload user document", e);

            return ResponseEntity.status(500)
                    .body(ApiResponse.internalError(
                            "Failed to upload user document: "
                                    + e.getMessage()));
        }
    }
}
