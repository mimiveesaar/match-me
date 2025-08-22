package tech.kood.match_me.user_management.features.refreshToken;

import java.time.Instant;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import tech.kood.match_me.user_management.common.UserManagementTestBase;
import tech.kood.match_me.user_management.common.exceptions.CheckedConstraintViolationException;
import tech.kood.match_me.user_management.features.refreshToken.domain.internal.model.refreshToken.RefreshTokenFactory;
import tech.kood.match_me.user_management.features.refreshToken.domain.internal.model.refreshTokenId.RefreshTokenIdFactory;
import tech.kood.match_me.user_management.features.refreshToken.domain.internal.model.refreshTokenSecret.RefreshTokenSecretFactory;
import tech.kood.match_me.user_management.features.refreshToken.internal.mapper.RefreshTokenMapper;
import tech.kood.match_me.user_management.features.refreshToken.internal.persistance.RefreshTokenRepository;
import tech.kood.match_me.user_management.features.refreshToken.internal.persistance.refreshTokenEntity.RefreshTokenEntity;
import tech.kood.match_me.user_management.features.user.UserMother;
import tech.kood.match_me.user_management.features.user.internal.mapper.UserMapper;
import tech.kood.match_me.user_management.features.user.internal.persistance.UserRepository;
import tech.kood.match_me.user_management.features.user.UserEntityMother;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class RefreshTokenRepositoryTests extends UserManagementTestBase {

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    RefreshTokenFactory refreshTokenFactory;

    @Autowired
    RefreshTokenIdFactory refreshTokenIdFactory;

    @Autowired
    RefreshTokenSecretFactory refreshTokenSecretFactory;

    @Autowired
    RefreshTokenMapper refreshTokenMapper;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserEntityMother userEntityMother;

    @Autowired
    UserMother userMother;

    @Autowired
    UserMapper userMapper;

    private RefreshTokenEntity createAndSaveRefreshToken() throws CheckedConstraintViolationException {
        var mockUser = userMother.createValidUser();
        var mockUserEntity = userMapper.toEntity(mockUser);
        userRepository.saveUser(mockUserEntity);

        var refreshToken = refreshTokenFactory.createNew(mockUser.getId());
        var entity = refreshTokenMapper.toEntity(refreshToken);
        refreshTokenRepository.save(entity);
        return entity;
    }

    @Test
    void testSaveRefreshToken() throws CheckedConstraintViolationException {
        createAndSaveRefreshToken();
    }

    @Test
    void testValidateToken() throws CheckedConstraintViolationException {
        var refreshToken = createAndSaveRefreshToken();

        boolean isValid = refreshTokenRepository.validateToken(refreshToken.getSecret(), refreshToken.getUserId(), Instant.now());
        assert isValid : "The token should be valid";
    }

    @Test
    void testFindToken() throws CheckedConstraintViolationException {

        var token = createAndSaveRefreshToken();

        var foundToken = refreshTokenRepository.findToken(token.getSecret());
        assert foundToken.isPresent() : "The token should be found";
    }

    @Test
    void testExpiredTokenInvalid() throws CheckedConstraintViolationException {

        var mockUser = userMother.createValidUser();
        var mockUserEntity = userMapper.toEntity(mockUser);
        userRepository.saveUser(mockUserEntity);

        var refreshTokenId = refreshTokenIdFactory.newId();
        var refreshToken = refreshTokenFactory.create(refreshTokenId, mockUser.getId(),
                refreshTokenSecretFactory.createNew(), Instant.now().minusSeconds(3600), Instant.now().minusSeconds(1800)); // Token expired );
        var entity = refreshTokenMapper.toEntity(refreshToken);
        refreshTokenRepository.save(entity);

        boolean isValid = refreshTokenRepository.validateToken(refreshToken.getSecret().toString(), mockUser.getId().getValue(), Instant.now());
        assert !isValid : "The token should be invalid";
    }

    @Test
    void testDeleteExpiredTokens() throws CheckedConstraintViolationException {
        var mockUser = userMother.createValidUser();
        var mockUserEntity = userMapper.toEntity(mockUser);
        userRepository.saveUser(mockUserEntity);

        var refreshTokenId = refreshTokenIdFactory.newId();
        var refreshToken = refreshTokenFactory.create(refreshTokenId, mockUser.getId(),
                refreshTokenSecretFactory.createNew(), Instant.now().minusSeconds(3600), Instant.now().minusSeconds(1800)); // Token expired );
        var entity = refreshTokenMapper.toEntity(refreshToken);
        refreshTokenRepository.save(entity);

        refreshTokenRepository.deleteExpiredTokens(Instant.now());

        boolean isValid = refreshTokenRepository.validateToken(refreshToken.getSecret().toString(), mockUser.getId().getValue(), Instant.now());
        assert !isValid : "The token should be invalid";
    }

    @Test
    void testDeleteByToken() throws CheckedConstraintViolationException {
        var refreshToken = createAndSaveRefreshToken();
        refreshTokenRepository.deleteToken(refreshToken.getSecret());
        boolean isValid = refreshTokenRepository.validateToken(refreshToken.getSecret(), refreshToken.getUserId(), Instant.now());
        assert !isValid : "The token should be deleted and invalid";
    }
}
