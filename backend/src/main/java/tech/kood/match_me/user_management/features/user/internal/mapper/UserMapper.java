package tech.kood.match_me.user_management.features.user.internal.mapper;

import org.jmolecules.architecture.layered.ApplicationLayer;
import org.springframework.stereotype.Component;
import tech.kood.match_me.user_management.features.user.domain.api.EmailDTO;
import tech.kood.match_me.user_management.features.user.domain.api.HashedPasswordDTO;
import tech.kood.match_me.user_management.features.user.domain.api.UserDTO;
import tech.kood.match_me.user_management.features.user.domain.api.UserIdDTO;
import tech.kood.match_me.user_management.common.exceptions.CheckedConstraintViolationException;
import tech.kood.match_me.user_management.features.user.domain.internal.model.email.EmailFactory;
import tech.kood.match_me.user_management.features.user.domain.internal.model.hashedPassword.HashedPasswordFactory;
import tech.kood.match_me.user_management.features.user.domain.internal.model.user.User;
import tech.kood.match_me.user_management.features.user.domain.internal.model.user.UserFactory;
import tech.kood.match_me.user_management.features.user.domain.internal.model.user.UserIdFactory;
import tech.kood.match_me.user_management.features.user.internal.persistance.userEntity.UserEntity;
import tech.kood.match_me.user_management.features.user.internal.persistance.userEntity.UserEntityFactory;

@Component
@ApplicationLayer
public final class UserMapper {

    private final UserFactory userFactory;
    private final UserEntityFactory userEntityFactory;
    private final HashedPasswordFactory hashedPasswordFactory;
    private final EmailFactory emailFactory;
    private final UserIdFactory userIdFactory;


    public UserMapper(UserFactory userFactory, UserEntityFactory userEntityFactory, HashedPasswordFactory hashedPasswordFactory, EmailFactory emailFactory, UserIdFactory userIdFactory) {
        this.userFactory = userFactory;
        this.userEntityFactory = userEntityFactory;
        this.hashedPasswordFactory = hashedPasswordFactory;
        this.emailFactory = emailFactory;
        this.userIdFactory = userIdFactory;
    }

    public User toUser(UserEntity userEntity) throws CheckedConstraintViolationException {

        var userId = userIdFactory.create(userEntity.getId());
        var email = emailFactory.create(userEntity.getEmail());
        var hashedPassword = hashedPasswordFactory.create(userEntity.getPasswordHash(), userEntity.getPasswordSalt());

        return userFactory.create(userId, email, hashedPassword, userEntity.getCreatedAt(), userEntity.getUpdatedAt());
    }

    public User toUser(UserDTO userDTO) throws CheckedConstraintViolationException {

        var userId = userIdFactory.create(userDTO.id().value());
        var email = emailFactory.create(userDTO.email().value());
        var hashedPassword = hashedPasswordFactory.create(userDTO.hashedPassword().hash(), userDTO.hashedPassword().salt());

        return userFactory.create(userId, email, hashedPassword, userDTO.createdAt(), userDTO.updatedAt());
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

    public UserDTO toDTO(UserEntity userEntity) throws CheckedConstraintViolationException {
        var user = toUser(userEntity);
        return toDTO(user);
    }
}