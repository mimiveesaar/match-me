package tech.kood.match_me.user_management.internal.features.user.mapper;

import org.jmolecules.architecture.layered.ApplicationLayer;
import org.springframework.stereotype.Component;
import tech.kood.match_me.user_management.api.features.user.domain.dto.EmailDTO;
import tech.kood.match_me.user_management.api.features.user.domain.dto.HashedPasswordDTO;
import tech.kood.match_me.user_management.api.features.user.domain.dto.UserDTO;
import tech.kood.match_me.user_management.api.features.user.domain.dto.UserIdDTO;
import tech.kood.match_me.user_management.common.exceptions.CheckedConstraintViolationException;
import tech.kood.match_me.user_management.internal.features.user.domain.model.User;
import tech.kood.match_me.user_management.internal.features.user.domain.model.UserFactory;
import tech.kood.match_me.user_management.internal.features.user.domain.model.email.EmailFactory;
import tech.kood.match_me.user_management.internal.features.user.domain.model.hashedPassword.HashedPasswordFactory;
import tech.kood.match_me.user_management.internal.features.user.domain.model.userId.UserIdFactory;
import tech.kood.match_me.user_management.internal.features.user.persistance.userEntity.UserEntity;
import tech.kood.match_me.user_management.internal.features.user.persistance.userEntity.UserEntityFactory;

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

    public User toUser(UserEntity user) throws CheckedConstraintViolationException {
        var userId = userIdFactory.make(user.getId());
        var email = emailFactory.make(user.getEmail());
        var hashedPassword = hashedPasswordFactory.make(user.getPasswordHash(), user.getPasswordSalt());

        return userFactory.make(userId, email, hashedPassword, user.getCreatedAt(), user.getUpdatedAt());
    }

    public User toUser(UserDTO userDTO) throws CheckedConstraintViolationException {
        var userId = userIdFactory.make(userDTO.id().getValue());
        var email = emailFactory.make(userDTO.email().getValue());
        var hashedPassword = hashedPasswordFactory.make(userDTO.hashedPassword().hash(), userDTO.hashedPassword().salt());

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
