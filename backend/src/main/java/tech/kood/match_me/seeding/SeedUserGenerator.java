package tech.kood.match_me.seeding;

import org.springframework.stereotype.Component;
import tech.kood.match_me.common.domain.api.UserIdDTO;
import tech.kood.match_me.user_management.common.domain.api.EmailDTO;
import tech.kood.match_me.user_management.features.user.domain.HashedPasswordDTO;
import tech.kood.match_me.user_management.features.user.domain.UserDTO;
import tech.kood.match_me.user_management.features.user.domain.UserStateDTO;

@Component
public class SeedUserGenerator {

    //TODO: make hashed passwords realistic.
    public UserDTO[] generate() {
        return new UserDTO[] {
            new UserDTO(
                UserIdDTO.of("11111111-1111-1111-1111-111111111111"),
                EmailDTO.of("user1@example.com"),
                new HashedPasswordDTO("hash1", "salt1"),
                new UserStateDTO(1),
                java.time.Instant.now(),
                java.time.Instant.now()
            ),
            new UserDTO(
                UserIdDTO.of("22222222-2222-2222-2222-222222222222"),
                EmailDTO.of("user2@example.com"),
                new HashedPasswordDTO("hash2", "salt2"),
                new UserStateDTO(1),
                java.time.Instant.now(),
                java.time.Instant.now()
            ),
            new UserDTO(
                UserIdDTO.of("33333333-3333-3333-3333-333333333333"),
                EmailDTO.of("user3@example.com"),
                new HashedPasswordDTO("hash3", "salt3"),
                new UserStateDTO(0),
                java.time.Instant.now(),
                java.time.Instant.now()
            ),
            new UserDTO(
                UserIdDTO.of("44444444-4444-4444-4444-444444444444"),
                EmailDTO.of("user4@example.com"),
                new HashedPasswordDTO("hash4", "salt4"),
                new UserStateDTO(0),
                java.time.Instant.now(),
                java.time.Instant.now()
            ),
            new UserDTO(
                UserIdDTO.of("55555555-5555-5555-5555-555555555555"),
                EmailDTO.of("user5@example.com"),
                new HashedPasswordDTO("hash5", "salt5"),
                new UserStateDTO(0),
                java.time.Instant.now(),
                java.time.Instant.now()
            )
        };
    }
}
