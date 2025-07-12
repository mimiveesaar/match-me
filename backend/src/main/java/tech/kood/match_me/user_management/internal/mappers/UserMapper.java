package tech.kood.match_me.user_management.internal.mappers;

import tech.kood.match_me.user_management.DTOs.UserDTO;
import tech.kood.match_me.user_management.internal.entities.UserEntity;
import tech.kood.match_me.user_management.models.HashedPassword;
import tech.kood.match_me.user_management.models.User;

public final class UserMapper {
    
    public static UserDTO toUserDTO(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        
        return new UserDTO(
            user.id(),
            user.username(),
            user.email()
        );
    }

    public static UserDTO toUserDTO(UserEntity userEntity) {
        if (userEntity == null) {
            throw new IllegalArgumentException("UserEntity cannot be null");
        }
     
        return toUserDTO(toUser(userEntity));
    }

    public static User toUser(UserEntity userEntity) {
        if (userEntity == null) {
            throw new IllegalArgumentException("UserEntity cannot be null");
        }
        
        return new User(
            userEntity.id(),
            userEntity.username(),
            userEntity.email(),
            new HashedPassword(userEntity.passwordHash(), userEntity.passwordSalt()),
            userEntity.createdAt(),
            userEntity.updatedAt()
        );
    }
}
