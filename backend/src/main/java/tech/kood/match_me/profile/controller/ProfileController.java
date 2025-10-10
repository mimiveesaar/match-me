package tech.kood.match_me.profile.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.UUID;
import org.springframework.core.io.Resource;
import tech.kood.match_me.profile.dto.ProfileDTO;
import tech.kood.match_me.profile.dto.ProfileViewDTO;
import tech.kood.match_me.profile.model.Profile;
import tech.kood.match_me.profile.service.ProfileService;

@RestController
@RequestMapping("/api/profiles")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3002", "http://localhost:3003"})
public class ProfileController {

    private final ProfileService service;

    public ProfileController(ProfileService service) {
        this.service = service;
    }

    @PostMapping("/me")
    public ResponseEntity<ProfileViewDTO> saveMyProfile(@RequestBody ProfileDTO dto) {
        System.out.println("=== POST /api/profiles/me called ===");
        System.out.println("Received DTO: " + dto);

        try {
            ProfileViewDTO savedProfileDTO = service.saveOrUpdateProfile(dto);
            System.out.println("Profile saved successfully");
            return ResponseEntity.ok(savedProfileDTO);
        } catch (Exception e) {
            System.out.println("Error saving profile: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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

            if (!file.getContentType().startsWith("image/")) {
                return ResponseEntity.badRequest().body("Only image files are allowed");
            }

            String imagePath = service.saveProfileImage(file);
            ProfileViewDTO updatedProfile = service.updateProfileImage(imagePath);

            return ResponseEntity.ok(updatedProfile);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload image: " + e.getMessage());
        }
    }

    @GetMapping("/me/image")
    public ResponseEntity<Resource> getProfileImage() {
        try {
            Resource imageResource = service.getProfileImage();
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG) // could be dynamically
                                                                         // detected
                    .body(imageResource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }



    
// endpoint that external modules can call:
//      The other module can POST:
//      POST /api/profiles/sync?userId=<uuid>&username=JohnDoe

    @PostMapping("/sync")
    public ResponseEntity<ProfileViewDTO> syncUser(@RequestParam UUID userId,
            @RequestParam String username) {
        Profile profile = service.getOrCreateProfile(userId, username);
        return ResponseEntity.ok(service.toViewDTO(profile));
    }
}
