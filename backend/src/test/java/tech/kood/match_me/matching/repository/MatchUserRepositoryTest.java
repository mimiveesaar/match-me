package tech.kood.match_me.matching.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import tech.kood.match_me.matching.common.MatchingTestBase;
import tech.kood.match_me.matching.model.*;

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

        var bodyformEntity = new BodyformEntity(1, "gelatinous");
        var homePlanetEntity = new HomeplanetEntity(1, "Earth", 12.1f, 13.1f );
        var lookingForEntity = new LookingForEntity(1, "...");
        var interestsEntity = new InterestEntity( "kkk");
        var interestsSetEntity = new HashSet<InterestEntity>();
        var userEntity = new UserEntity(UUID.randomUUID(),"Mimi", 27, homePlanetEntity, bodyformEntity, "mmm", lookingForEntity, interestsSetEntity, "");

        matchUserRepository.save(userEntity);

        var queryResult = matchUserRepository.findByHomeplanetId(homePlanetEntity.getId());

        assertEquals( 1, queryResult.size());
    }

}
