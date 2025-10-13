package tech.kood.match_me.matching.event;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;
import tech.kood.match_me.matching.common.MatchingTestBase;
import tech.kood.match_me.matching.model.*;
import tech.kood.match_me.matching.repository.MatchUserRepository;
import tech.kood.match_me.matching.seeder.MatchesSeeder;
import tech.kood.match_me.profile.dto.ProfileViewDTO;
import tech.kood.match_me.profile.events.ProfileChangedEvent;

import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
public class IncomingProfileEventTest extends MatchingTestBase {

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private MatchUserRepository matchUserRepository;

    @Autowired
    private MatchesSeeder matchesSeeder;

    @Test
    void testListenAndSaveNewUser() {
        var profileDTO = new ProfileViewDTO();
        profileDTO.setId(UUID.randomUUID());
        profileDTO.setUsername("testuser");
        profileDTO.setName("Test User");
        profileDTO.setAge(30);
        profileDTO.setHomeplanet("Earth");
        profileDTO.setHomeplanetId(1);
        profileDTO.setBodyform("Humanoid");
        profileDTO.setBodyformId(2);
        profileDTO.setLookingFor("Friendship");
        profileDTO.setLookingForId(3);
        profileDTO.setBio("This is a test bio.");
        profileDTO.setInterests(new HashSet<>(Arrays.asList("Reading", "Gaming")));
        profileDTO.setInterestIds(Arrays.asList(10, 20));
        profileDTO.setProfilePic("/images/testuser.png");

        matchesSeeder.seedMatches();

        var newProfileEvent = new ProfileChangedEvent(profileDTO);

        eventPublisher.publishEvent(newProfileEvent);

        var queryResult = matchUserRepository.findById(profileDTO.getId());
        assertTrue(queryResult.isPresent());

    }
}
