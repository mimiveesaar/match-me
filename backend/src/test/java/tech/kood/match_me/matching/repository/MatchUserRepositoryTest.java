package tech.kood.match_me.matching.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import tech.kood.match_me.common.constants.Interests;
import tech.kood.match_me.matching.common.MatchingTestBase;
import tech.kood.match_me.matching.model.*;
import tech.kood.match_me.user_management.features.user.internal.persistance.userEntity.UserEntity;

import java.util.HashSet;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class MatchUserRepositoryTest extends MatchingTestBase {

    @Autowired
    private MatchUserRepository matchUserRepository;

    @Test
    void testSaveAndFindById() {

        var bodyformEntity = new Bodyform(1, "gelatinous");
        var homePlanetEntity = new Homeplanet(1, "Earth", 12.1, 13.1 );
        var lookingForEntity = new LookingFor(1, "...");
        var interestsEntity = new Interest( "kkk");
        var interestsSetEntity = new HashSet<Interest>();
        var userEntity = new User(UUID.randomUUID(),"Mimi", 27, homePlanetEntity, bodyformEntity, "mmm", lookingForEntity, interestsSetEntity, "");

        matchUserRepository.save(userEntity);

        var queryResult = matchUserRepository.findByHomeplanetId(homePlanetEntity.getId());

        assertEquals( 1, queryResult.size());
    }

}
