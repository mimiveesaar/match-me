package tech.kood.match_me.user_management.features.user;

import org.springframework.stereotype.Component;
import com.github.javafaker.Faker;
import tech.kood.match_me.user_management.common.exceptions.CheckedConstraintViolationException;
import tech.kood.match_me.user_management.features.user.internal.mapper.UserMapper;
import tech.kood.match_me.user_management.features.user.internal.persistance.userEntity.UserEntity;

@Component
public class UserEntityMother {
    public static Faker faker = new Faker();

    private final UserMother userMother;
    private final UserMapper userMapper;

    public UserEntityMother(UserMother userMother, UserMapper userMapper) {
        this.userMother = userMother;
        this.userMapper = userMapper;
    }

    public UserEntity createValidUserEntity() throws CheckedConstraintViolationException {
        var user = userMother.createValidUser();
        return userMapper.toEntity(user);
    }
}
