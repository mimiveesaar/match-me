package tech.kood.match_me.matching.port;

import tech.kood.match_me.matching.dto.UserDTO;

import java.util.Optional;
import java.util.UUID;

public interface UserQueryPort {
    Optional<UserDTO> getUserById(UUID id);
}
