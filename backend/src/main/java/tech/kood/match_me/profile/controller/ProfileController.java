package tech.kood.match_me.profile.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.kood.match_me.profile.dto.ProfileDTO;
import tech.kood.match_me.profile.model.Profile;
import tech.kood.match_me.profile.service.ProfileService;
import tech.kood.match_me.user_management.features.user.domain.UserDTO;

import java.util.UUID;

@RestController
@RequestMapping("/api/profiles")
@CrossOrigin(origins = {"http://localhost:3000"})
public class ProfileController {

    private final ProfileService service;

    public ProfileController(ProfileService service) {
        this.service = service;
    }

    /** 🟢 Save or update the logged-in user's profile */
    @PostMapping("/me")
    public ResponseEntity<?> saveMyProfile(@RequestBody ProfileDTO dto) {
        System.out.println("=== POST /api/profiles/me called ===");
        System.out.println("Received DTO: " + dto);

        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (!(principal instanceof UserDTO user)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Unauthorized: user not authenticated");
            }

            UUID userId = user.id().value();
            ProfileDTO savedProfileDTO = service.saveOrUpdateProfile(userId, dto);
            System.out.println("Profile saved successfully for userId=" + userId);

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

    /** 🟢 Get the logged-in user's profile */
    @GetMapping("/me")
    public ResponseEntity<?> getMyProfile() {
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            if (!(principal instanceof UserDTO user)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Unauthorized: user not authenticated");
            }

            UUID userId = user.id().value();
            ProfileDTO profileDTO = service.getProfileByUserId(userId);

            if (profileDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Profile not found for user: " + userId);
            }

            return ResponseEntity.ok(profileDTO);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving profile: " + e.getMessage());
        }
    }

    /** 🟢 Upload a profile image for the current user */
    @PostMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadProfileImage(@RequestParam("file") MultipartFile file) {
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (!(principal instanceof UserDTO user)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Unauthorized: user not authenticated");
            }

            UUID userId = user.id().value();

            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Please select a file to upload");
            }

            if (file.getContentType() == null || !file.getContentType().startsWith("image/")) {
                return ResponseEntity.badRequest().body("Only image files are allowed");
            }

            if (file.getSize() > 5 * 1024 * 1024) {
                return ResponseEntity.badRequest().body("File size must be less than 5MB");
            }

            ProfileDTO updatedProfile = service.uploadProfileImage(userId, file);
            return ResponseEntity.ok(updatedProfile);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload image: " + e.getMessage());
        }
    }

    /** 🟢 Get the logged-in user's profile image */
    @GetMapping("/me/image")
    public ResponseEntity<Resource> getProfileImage() {
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (!(principal instanceof UserDTO user)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            UUID userId = user.id().value();
            Resource imageResource = service.getProfileImage(userId);

            if (imageResource == null) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(imageResource);

        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /** 🟡 Admin or system sync — no auth required */
    @PostMapping("/sync")
    public ResponseEntity<ProfileDTO> syncUser(
            @RequestParam UUID userId,
            @RequestParam String username) {
        Profile profile = service.getOrCreateProfile(userId, username);
        return ResponseEntity.ok(service.toViewDTO(profile));
    }
}
