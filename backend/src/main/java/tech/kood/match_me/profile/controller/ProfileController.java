package tech.kood.match_me.profile.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tech.kood.match_me.profile.dto.ProfileDTO;
import tech.kood.match_me.profile.dto.ProfileViewDTO;
import tech.kood.match_me.profile.model.Profile;
import tech.kood.match_me.profile.service.ProfileService;

import java.util.UUID;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {
    private final ProfileService service;

    public ProfileController(ProfileService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ProfileViewDTO> createProfile(@RequestBody ProfileDTO dto) {
        Profile profile = service.createProfile(dto);
        return ResponseEntity.ok(toViewDTO(profile));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfileViewDTO> getProfile(@PathVariable UUID id) {
        return ResponseEntity.ok(toViewDTO(service.getProfile(id)));
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
