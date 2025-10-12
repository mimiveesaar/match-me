package tech.kood.match_me.profile.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import tech.kood.match_me.profile.dto.ProfileDTO;
import tech.kood.match_me.profile.dto.ProfileViewDTO;
import tech.kood.match_me.profile.model.Profile;
import tech.kood.match_me.profile.service.ProfileService;

import java.util.UUID;

@RestController
@RequestMapping("/api/profiles")
@CrossOrigin(origins = {"http://localhost:3000"})
public class ProfileController {

    private final ProfileService service;

    public ProfileController(ProfileService service) {
        this.service = service;
    }

    @PostMapping("/me")
    public ResponseEntity<?> saveMyProfile(@RequestBody ProfileDTO dto) {
        System.out.println("=== POST /api/profiles/me called ===");
        System.out.println("Received DTO: " + dto);

        try {
            ProfileViewDTO savedProfileDTO = service.saveOrUpdateProfile(dto);
            System.out.println("Profile saved successfully");
            return ResponseEntity.ok(savedProfileDTO);
        } catch (RuntimeException e) {
            System.err.println("Error saving profile: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error saving profile: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Internal server error: " + e.getMessage());
        }
    }

    @GetMapping("/me")
    public ResponseEntity<ProfileViewDTO> getMyProfile() {
        ProfileViewDTO profileDTO = service.getMyProfileDTO();
        return ResponseEntity.ok(profileDTO);
    }

    @PostMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadProfileImage(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Please select a file to upload");
            }

            if (file.getContentType() == null || !file.getContentType().startsWith("image/")) {
                return ResponseEntity.badRequest().body("Only image files are allowed");
            }

            // Validate file size (5MB max)
            if (file.getSize() > 5 * 1024 * 1024) {
                return ResponseEntity.badRequest().body("File size must be less than 5MB");
            }

            ProfileViewDTO updatedProfile = service.uploadProfileImage(file);
            return ResponseEntity.ok(updatedProfile);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Failed to upload image: " + e.getMessage());
        }
    }

    @GetMapping("/me/image")
    public ResponseEntity<Resource> getProfileImage() {
        try {
            Resource imageResource = service.getProfileImage();
            return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(imageResource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/sync")
    public ResponseEntity<ProfileViewDTO> syncUser(
            @RequestParam UUID userId,
            @RequestParam String username) {
        Profile profile = service.getOrCreateProfile(userId, username);
        return ResponseEntity.ok(service.toViewDTO(profile));
    }
}