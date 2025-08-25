package tech.kood.match_me.profile.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tech.kood.match_me.profile.dto.ProfileDTO;
import tech.kood.match_me.profile.dto.ProfileViewDTO;
import tech.kood.match_me.profile.model.Profile;
import tech.kood.match_me.profile.service.ProfileService;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {

    private final ProfileService service;

    public ProfileController(ProfileService service) {
        this.service = service;
    }

@PostMapping("/me")
public ResponseEntity<ProfileViewDTO> saveMyProfile(@RequestBody ProfileDTO dto) {
    return ResponseEntity.ok(toViewDTO(service.saveOrUpdateProfile(dto)));
}

    @GetMapping("/me")
    public ResponseEntity<ProfileViewDTO> getMyProfile() {
        Profile profile = service.getMyProfile(); // no ID needed
        return ResponseEntity.ok(toViewDTO(profile));
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
        dto.setInterests(profile.getInterests().stream().map(i -> i.getName()).collect(java.util.stream.Collectors.toSet()));
        dto.setProfilePic(profile.getProfilePic());
        return dto;
    }
}
