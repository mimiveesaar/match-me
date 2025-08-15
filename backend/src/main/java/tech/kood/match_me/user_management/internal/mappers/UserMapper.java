package tech.kood.match_me.user_management.internal.mappers;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import tech.kood.match_me.user_management.api.DTOs.UserDTO;
import tech.kood.match_me.user_management.internal.database.entities.UserEntity;
import tech.kood.match_me.user_management.internal.features.user.domain.model.hashedPassword.HashedPassword;
import tech.kood.match_me.user_management.internal.features.user.domain.model.User;
import tech.kood.match_me.user_management.internal.features.user.domain.model.userId.UserId;

@Component
@Primary
public final class UserMapper {

    public UserDTO toUserDTO(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        return new UserDTO(user.getId().toString(), user.getUsername(), user.getEmail());
    }

    public UserDTO toUserDTO(UserEntity userEntity) {
        if (userEntity == null) {
            throw new IllegalArgumentException("UserEntity cannot be null");
        }

        return toUserDTO(toUser(userEntity));
    }

    public User toUser(UserEntity userEntity) {
        if (userEntity == null) {
            throw new IllegalArgumentException("UserEntity cannot be null");
        }

        return User.of(UserId.of(userEntity.getId()), userEntity.getUsername(),
                userEntity.getEmail(),
                HashedPassword.of(userEntity.getPasswordHash(), userEntity.getPasswordSalt()),
                userEntity.getCreatedAt(), userEntity.getUpdatedAt());
    }
}
