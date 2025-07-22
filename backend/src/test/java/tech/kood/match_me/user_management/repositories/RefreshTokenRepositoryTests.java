package tech.kood.match_me.user_management.repositories;

import java.time.Instant;
import java.util.UUID;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import tech.kood.match_me.user_management.common.UserManagementTestBase;
import tech.kood.match_me.user_management.internal.database.repostitories.RefreshTokenRepository;
import tech.kood.match_me.user_management.internal.database.repostitories.UserRepository;
import tech.kood.match_me.user_management.internal.features.refreshToken.RefreshTokenFactory;
import tech.kood.match_me.user_management.internal.mappers.RefreshTokenMapper;
import tech.kood.match_me.user_management.mocks.UserEntityMocker;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS) 
@Transactional
public class RefreshTokenRepositoryTests extends UserManagementTestBase {

    @Autowired
    RefreshTokenRepository refreshTokenRepository;


    @Autowired
    @Qualifier("userManagementFlyway") Flyway userManagementFlyway;

    @Autowired
    RefreshTokenFactory refreshTokenFactory;

    @Autowired
    RefreshTokenMapper refreshTokenMapper;

    @Autowired
    UserRepository userRepository;

    @BeforeAll
    void migrate() {
        var result = userManagementFlyway.migrate();
    }

    @Test
    void testSaveRefreshToken() {

        var mockUser = UserEntityMocker.createValidUserEntity();
        userRepository.saveUser(mockUser);

        var refreshToken = refreshTokenFactory.create(mockUser.id());
        var entity = refreshTokenMapper.toEntity(refreshToken);
        refreshTokenRepository.save(entity);
    }

}