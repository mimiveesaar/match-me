package tech.kood.match_me.seeding;

import org.springframework.stereotype.Component;
import tech.kood.match_me.common.domain.api.UserIdDTO;
import tech.kood.match_me.user_management.common.domain.api.EmailDTO;
import tech.kood.match_me.user_management.features.user.domain.HashedPasswordDTO;
import tech.kood.match_me.user_management.features.user.domain.UserDTO;
import tech.kood.match_me.user_management.features.user.domain.UserStateDTO;

@Component
public class SeedUserGenerator {
    private static final int USER_COUNT = 100;

    private static class FakeUserData {
        final String id;
        final String email;
        final String passwordHash;
        final String salt;
        final int state;

        FakeUserData(int index) {
            this.id = String.format("%08d-%04d-%04d-%04d-%012d", index, index, index, index, index);
            this.email = String.format("user%d@example.com", index + 1);
            this.passwordHash = "hash" + (index + 1);
            this.salt = "salt" + (index + 1);
            this.state = (index % 2 == 0) ? 1 : 0;
        }
    }

    public UserDTO[] generate() {
        UserDTO[] users = new UserDTO[USER_COUNT];
        for (int i = 0; i < USER_COUNT; i++) {
            FakeUserData data = new FakeUserData(i);
            users[i] = new UserDTO(
                UserIdDTO.of(data.id),
                EmailDTO.of(data.email),
                new HashedPasswordDTO(data.passwordHash, data.salt),
                new UserStateDTO(data.state),
                java.time.Instant.now(),
                java.time.Instant.now()
            );
        }
        return users;
    }
}
