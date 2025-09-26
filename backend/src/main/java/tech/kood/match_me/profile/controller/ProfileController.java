package tech.kood.match_me.profile.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import tech.kood.match_me.profile.dto.ProfileDTO;
import tech.kood.match_me.profile.dto.ProfileViewDTO;
import tech.kood.match_me.profile.service.ProfileService;

@RestController
@RequestMapping("/api/profiles")
@CrossOrigin(origins = {"http://localhost:3002", "http://localhost:3000"})
public class ProfileController {

    private final ProfileService service;

    public ProfileController(ProfileService service) {
        this.service = service;
    }

    @PostMapping("/me")
    public ResponseEntity<ProfileViewDTO> saveMyProfile(@RequestBody ProfileDTO dto) {
        System.out.println("=== POST /api/profiles/me called ===");
        System.out.println("Received DTO: " + dto);

        if (dto != null) {

            System.out.println("DTO Details:");
            System.out.println("  Age: " + dto.getAge());
            System.out.println("  Bio: '" + dto.getBio() + "'");
            System.out.println("  HomeplanetId: " + dto.getHomeplanetId());
            System.out.println("  BodyformId: " + dto.getBodyformId());
            System.out.println("  LookingForId: " + dto.getLookingForId());
            System.out.println("  InterestIds: " + dto.getInterestIds());
            System.out.println("  ProfilePic: '" + dto.getProfilePic() + "'");
        } else {
            System.out.println("DTO is NULL!");
        }

        try {
            ProfileViewDTO savedProfileDTO = service.saveOrUpdateProfile(dto); // Now returns
                                                                               // ProfileViewDTO
            System.out.println("Profile saved successfully");
            return ResponseEntity.ok(savedProfileDTO);
        } catch (Exception e) {
            System.out.println("Error saving profile: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @GetMapping("/me")
    public ResponseEntity<ProfileViewDTO> getMyProfile() {
        ProfileViewDTO profileDTO = service.getMyProfileDTO(); // Use the new method
        return ResponseEntity.ok(profileDTO);
    }

    @PostMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadProfileImage(@RequestParam("file") MultipartFile file) {
        try {
            // Validate file
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Please select a file to upload");
            }

            // Check file type
            String contentType = file.getContentType();
            if (!contentType.startsWith("image/")) {
                return ResponseEntity.badRequest().body("Only image files are allowed");
            }

            // Save image and get the file path/URL
            String imagePath = service.saveProfileImage(file);

            // Update profile with image path
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
            Resource imageResource = service.getProfileImage();  // Use 'service'
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG) // or detect content type
                    .body(imageResource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}