package tech.kood.match_me.user_management.internal.mappers;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import tech.kood.match_me.user_management.api.DTOs.UserDTO;
import tech.kood.match_me.user_management.internal.database.entities.UserEntity;
import tech.kood.match_me.user_management.models.HashedPassword;
import tech.kood.match_me.user_management.models.User;

@Component
@Primary
public final class UserMapper {

    public UserDTO toUserDTO(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        return new UserDTO(
                user.id().toString(),
                user.username(),
                user.email());
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

        return new User(
                userEntity.id(),
                userEntity.username(),
                userEntity.email(),
                new HashedPassword(userEntity.passwordHash(), userEntity.passwordSalt()),
                userEntity.createdAt(),
                userEntity.updatedAt());
    }
}
