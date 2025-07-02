package tech.kood.match_me.profile;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    public List<Profile> getAllProfiles() {
        // For demo: static list
        return List.of(
            new Profile(1L, "kati", "kati@example.com"),
            new Profile(2L, "markus", "markus@example.com")
        );
    }
}
