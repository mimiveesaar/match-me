package tech.kood.match_me.profile.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tech.kood.match_me.profile.dto.ProfileDTO;
import tech.kood.match_me.profile.dto.ProfileViewDTO;
import tech.kood.match_me.profile.model.Profile;
import tech.kood.match_me.profile.service.ProfileService;

@RestController
@RequestMapping("/api/profiles")
@CrossOrigin(origins = {"http://localhost:3002", "http://localhost:3000"}) // Add this line
public class ProfileController {

    private final ProfileService service;

    public ProfileController(ProfileService service) {
        this.service = service;
    }

@PostMapping("/me")
public ResponseEntity<ProfileViewDTO> saveMyProfile(@RequestBody ProfileDTO dto) {
    System.out.println("=== POST /api/profiles/me called ===");
    System.out.println("Received DTO: " + dto);
    
    // Add detailed logging
    if (dto != null) {
        System.out.println("DTO Details:");
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
        Profile savedProfile = service.saveOrUpdateProfile(dto);
        System.out.println("Profile saved with ID: " + savedProfile.getId());
        return ResponseEntity.ok(toViewDTO(savedProfile));
    } catch (Exception e) {
        System.out.println("Error saving profile: " + e.getMessage());
        e.printStackTrace();
        throw e;
    }
}
    @GetMapping("/me")
    public ResponseEntity<ProfileViewDTO> getMyProfile() {
        Profile profile = service.getMyProfile(); // no ID needed
        return ResponseEntity.ok(toViewDTO(profile));
    }

    // Add PUT method for updates
    @PutMapping("/me")
    public ResponseEntity<ProfileViewDTO> updateMyProfile(@RequestBody ProfileDTO dto) {
        System.out.println("=== PUT /api/profiles/me called ===");
        System.out.println("Received DTO: " + dto);

        try {
            Profile updatedProfile = service.saveOrUpdateProfile(dto);
            System.out.println("Profile updated with ID: " + updatedProfile.getId());
            return ResponseEntity.ok(toViewDTO(updatedProfile));
        } catch (Exception e) {
            System.out.println("Error updating profile: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    private ProfileViewDTO toViewDTO(Profile profile) {
        ProfileViewDTO dto = new ProfileViewDTO();
        dto.setId(profile.getId());
        dto.setUsername(profile.getUsername());
        dto.setAge(profile.getAge());
        dto.setHomeplanet(profile.getHomeplanet().getName());
        dto.setBodyform(profile.getBodyform().getName());
        dto.setLookingFor(profile.getLookingFor().getName());
        dto.setBio(profile.getBio());
        dto.setInterests(profile.getInterests().stream().map(i -> i.getName())
                .collect(java.util.stream.Collectors.toSet()));
        dto.setProfilePic(profile.getProfilePic());
        return dto;
    }
}