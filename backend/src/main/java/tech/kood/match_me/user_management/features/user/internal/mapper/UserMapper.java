package tech.kood.match_me.user_management.features.user.internal.mapper;

import jakarta.validation.ConstraintViolationException;
import org.jmolecules.architecture.layered.ApplicationLayer;
import org.springframework.stereotype.Component;
import tech.kood.match_me.user_management.features.user.domain.api.EmailDTO;
import tech.kood.match_me.user_management.features.user.domain.api.HashedPasswordDTO;
import tech.kood.match_me.user_management.features.user.domain.api.UserDTO;
import tech.kood.match_me.user_management.features.user.domain.api.UserIdDTO;
import tech.kood.match_me.user_management.common.exceptions.CheckedConstraintViolationException;
import tech.kood.match_me.user_management.features.user.domain.internal.model.user.User;
import tech.kood.match_me.user_management.features.user.domain.internal.model.user.UserFactory;
import tech.kood.match_me.user_management.features.user.domain.internal.model.email.Email;
import tech.kood.match_me.user_management.features.user.domain.internal.model.hashedPassword.HashedPassword;
import tech.kood.match_me.user_management.features.user.domain.internal.model.user.UserId;
import tech.kood.match_me.user_management.features.user.internal.persistance.userEntity.UserEntity;
import tech.kood.match_me.user_management.features.user.internal.persistance.userEntity.UserEntityFactory;

@Component
@ApplicationLayer
public final class UserMapper {

    private final UserFactory userFactory;
    private final UserEntityFactory userEntityFactory;
    private final UserIdFactory userIdFactory;
    private final HashedPasswordFactory hashedPasswordFactory;
    private final EmailFactory emailFactory;


    public UserMapper(UserFactory userFactory, UserEntityFactory userEntityFactory, UserIdFactory userIdFactory, HashedPasswordFactory hashedPasswordFactory, EmailFactory emailFactory) {
        this.userFactory = userFactory;
        this.userEntityFactory = userEntityFactory;
        this.userIdFactory = userIdFactory;
        this.hashedPasswordFactory = hashedPasswordFactory;
        this.emailFactory = emailFactory;
    }

    public User toUser(UserEntity userEntity) throws CheckedConstraintViolationException {

        UserId userId = null;
        Email email = null;
        HashedPassword hashedPassword = null;

        try {
            userId = userIdFactory.make(userEntity.getId());
            email = emailFactory.make(userEntity.getEmail());
            hashedPassword = hashedPasswordFactory.make(userEntity.getPasswordHash(), userEntity.getPasswordSalt());
        } catch (ConstraintViolationException ignored) {
            // We don't care about the validation result, we want to create the user anyway.
            // Validation will take place at the aggregate level.
        }

        return userFactory.make(userId, email, hashedPassword, userEntity.getCreatedAt(), userEntity.getUpdatedAt());
    }

    public User toUser(UserDTO userDTO) throws CheckedConstraintViolationException {

        UserId userId = null;
        Email email = null;
        HashedPassword hashedPassword = null;

        try {
            userId = userIdFactory.make(userDTO.id().getValue());
            email = emailFactory.make(userDTO.email().getValue());
            hashedPassword = hashedPasswordFactory.make(userDTO.hashedPassword().hash(), userDTO.hashedPassword().salt());
        } catch (ConstraintViolationException ignored) {
            // We don't care about the validation result, we want to create the user anyway.
            // Validation will take place at the aggregate level.
        }

        return userFactory.make(userId, email, hashedPassword, userDTO.createdAt(), userDTO.updatedAt());
    }

    public UserEntity toEntity(User user) throws CheckedConstraintViolationException {
        var userId = user.getId().getValue();
        var email = user.getEmail().toString();
        var hashedPassword = user.getHashedPassword().getHash();
        var passwordSalt = user.getHashedPassword().getSalt();

        return userEntityFactory.make(userId, email, hashedPassword, passwordSalt, user.getCreatedAt(), user.getUpdatedAt());
    }

    public UserDTO toDTO(User user) {
        var userIdDTO = new UserIdDTO(user.getId().getValue());
        var hashedPasswordDTO = new HashedPasswordDTO(user.getHashedPassword().getHash(), user.getHashedPassword().getSalt());
        var emailDTO = new EmailDTO(user.getEmail().toString());

        return new UserDTO(
                userIdDTO,
                emailDTO,
                hashedPasswordDTO,
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

}
