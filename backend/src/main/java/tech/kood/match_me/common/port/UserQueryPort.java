package tech.kood.match_me.common.port;

import tech.kood.match_me.common.dto.UserDTO;

import java.util.Optional;
import java.util.UUID;

public interface UserQueryPort {
    Optional<UserDTO> getUserById(UUID id);
}
