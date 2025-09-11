package tech.kood.match_me.profile.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            ProfileViewDTO savedProfileDTO = service.saveOrUpdateProfile(dto); // Now returns ProfileViewDTO
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
}