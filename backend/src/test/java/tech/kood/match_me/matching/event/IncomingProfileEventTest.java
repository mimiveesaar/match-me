package tech.kood.match_me.matching.event;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import tech.kood.match_me.matching.common.MatchingTestBase;
import tech.kood.match_me.matching.model.*;
import tech.kood.match_me.matching.repository.MatchUserRepository;
import tech.kood.match_me.profile.events.ProfileChangedEvent;

import java.util.HashSet;
import java.util.UUID;

@SpringBootTest
@Transactional
public class IncomingProfileEventTest extends MatchingTestBase {

    @Autowired
    private MatchUserRepository matchUserRepository;

    @Test
    void testListenAndSaveNewUser() {

        var bodyformEntity = new Bodyform(1, "gelatinous");
        var homePlanetEntity = new Homeplanet(1, "Earth", 12.1, 13.1 );
        var lookingForEntity = new LookingFor(1, "...");
        var interestsEntity = new Interest( "kkk");
        var interestsSetEntity = new HashSet<Interest>();
        var newUserEntity = new User(UUID.randomUUID(),"Mimi", 27, homePlanetEntity, bodyformEntity, "mmm", lookingForEntity, interestsSetEntity, "");

        var newProfileSent = new ProfileChangedEvent(newUserEntity);

        // Kuidas ma kuulan eventi?
        matchUserRepository.save(newUserEntity);
    }
}
