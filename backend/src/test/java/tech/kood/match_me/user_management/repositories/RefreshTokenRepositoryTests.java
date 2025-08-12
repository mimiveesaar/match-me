package tech.kood.match_me.user_management.repositories;

import java.time.Instant;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import tech.kood.match_me.user_management.common.UserManagementTestBase;
import tech.kood.match_me.user_management.internal.database.repostitories.RefreshTokenRepository;
import tech.kood.match_me.user_management.internal.database.repostitories.UserRepository;
import tech.kood.match_me.user_management.internal.domain.features.refreshToken.RefreshTokenFactory;
import tech.kood.match_me.user_management.internal.mappers.RefreshTokenMapper;
import tech.kood.match_me.user_management.mocks.UserEntityMocker;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class RefreshTokenRepositoryTests extends UserManagementTestBase {

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    @Qualifier("userManagementFlyway")
    Flyway userManagementFlyway;

    @Autowired
    RefreshTokenFactory refreshTokenFactory;

    @Autowired
    RefreshTokenMapper refreshTokenMapper;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserEntityMocker userEntityMocker;


    @Test
    void testSaveRefreshToken() {

        var mockUser = userEntityMocker.createValidUserEntity();
        userRepository.saveUser(mockUser);

        var refreshToken = refreshTokenFactory.create(mockUser.getId());
        var entity = refreshTokenMapper.toEntity(refreshToken);
        refreshTokenRepository.save(entity);
    }

    @Test
    void testValidateToken() {

        var mockUser = userEntityMocker.createValidUserEntity();
        userRepository.saveUser(mockUser);

        var refreshToken = refreshTokenFactory.create(mockUser.getId());
        var entity = refreshTokenMapper.toEntity(refreshToken);
        refreshTokenRepository.save(entity);

        boolean isValid = refreshTokenRepository.validateToken(refreshToken.getToken(),
                mockUser.getId(), Instant.now());
        assert isValid : "The token should be valid";
    }

    @Test
    void testFindToken() {
        var mockUser = userEntityMocker.createValidUserEntity();
        userRepository.saveUser(mockUser);

        var refreshToken = refreshTokenFactory.create(mockUser.getId());
        var entity = refreshTokenMapper.toEntity(refreshToken);
        refreshTokenRepository.save(entity);

        var foundToken = refreshTokenRepository.findToken(refreshToken.getToken());
        assert foundToken.isPresent() : "The token should be found";
    }

    @Test
    void testExpiredTokenInvalid() {
        var mockUser = userEntityMocker.createValidUserEntity();
        userRepository.saveUser(mockUser);

        var refreshToken =
                refreshTokenFactory.create(mockUser.getId(), Instant.now().minusSeconds(3600)); // Token
                                                                                                // expired
                                                                                                // 1
                                                                                                // hour
                                                                                                // ago
        var entity = refreshTokenMapper.toEntity(refreshToken);
        refreshTokenRepository.save(entity);

        boolean isValid = refreshTokenRepository.validateToken(refreshToken.getToken(),
                mockUser.getId(), Instant.now());
        assert !isValid : "The token should be invalid";
    }

    @Test
    void testDeleteExpiredTokens() {
        var mockUser = userEntityMocker.createValidUserEntity();
        userRepository.saveUser(mockUser);

        var refreshToken =
                refreshTokenFactory.create(mockUser.getId(), Instant.now().minusSeconds(3600)); // Token
                                                                                                // expired
                                                                                                // 1
                                                                                                // hour
                                                                                                // ago
        var entity = refreshTokenMapper.toEntity(refreshToken);
        refreshTokenRepository.save(entity);

        refreshTokenRepository.deleteExpiredTokens(Instant.now());

        boolean isValid = refreshTokenRepository.validateToken(refreshToken.getToken(),
                mockUser.getId(), Instant.now());
        assert !isValid : "The token should be invalid";
    }

    @Test
    void testDeleteByToken() {
        var mockUser = userEntityMocker.createValidUserEntity();
        userRepository.saveUser(mockUser);

        var refreshToken = refreshTokenFactory.create(mockUser.getId());
        var entity = refreshTokenMapper.toEntity(refreshToken);
        refreshTokenRepository.save(entity);

        refreshTokenRepository.deleteToken(refreshToken.getToken());

        boolean isValid = refreshTokenRepository.validateToken(refreshToken.getToken(),
                mockUser.getId(), Instant.now());
        assert !isValid : "The token should be deleted and invalid";
    }
}
