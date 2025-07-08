package tech.kood.match_me.profile.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tech.kood.match_me.profile.database.repositories.UserProfileRepository;
import tech.kood.match_me.profile.entity.UserProfile;

@RestController
@RequestMapping("/profiles")
public class ProfileController {

    private final UserProfileRepository repository;

    public ProfileController(UserProfileRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<UserProfile> create(@RequestBody UserProfile profile) {
        if (repository.existsById(profile.getUsername())) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repository.save(profile));
    }

    @PatchMapping("/{username}")
    public ResponseEntity<UserProfile> update(@PathVariable String username, @RequestBody UserProfile updates) {
        Optional<UserProfile> optional = repository.findById(username);
        if (optional.isEmpty()) return ResponseEntity.notFound().build();

        UserProfile existing = optional.get();

        // Only update non-null fields
        if (updates.getMail() != null) existing.setMail(updates.getMail());
        if (updates.getAge() != null) existing.setAge(updates.getAge());
        if (updates.getBio() != null) existing.setBio(updates.getBio());
        if (updates.getPassword() != null) existing.setPassword(updates.getPassword());
        if (updates.getBodyform() != null) existing.setBodyform(updates.getBodyform());
        if (updates.getPlanet() != null) existing.setPlanet(updates.getPlanet());
        if (updates.getLookingFor() != null) existing.setLookingFor(updates.getLookingFor());
        if (updates.getInterests() != null) existing.setInterests(updates.getInterests());
        if (updates.getProfilePic() != null) existing.setProfilePic(updates.getProfilePic());

        return ResponseEntity.ok(repository.save(existing));
    }
}
