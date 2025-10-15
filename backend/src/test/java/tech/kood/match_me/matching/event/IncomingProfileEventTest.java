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
import tech.kood.match_me.profile.dto.ProfileDTO;
import tech.kood.match_me.profile.events.ProfileChangedEvent;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional(transactionManager = "matchingTransactionManager")
public class IncomingProfileEventTest extends MatchingTestBase {

    private ProfileDTO createProfileDTO (UUID id,
                                             String username,
                                             Integer age,
                                             String homeplanet,
                                             Integer homeplanetId,
                                             String bodyform,
                                             Integer bodyformId,
                                             String lookingFor,
                                             Integer lookingForId,
                                             String bio,
                                             Set<String> interests,
                                             List<Integer> interestIds,
                                             String profilePicSrc
                                             ) {

        ProfileDTO dto = new ProfileDTO();
        dto.setId(id);
        dto.setUsername(username);
        dto.setAge(age);
        dto.setHomeplanet(homeplanet);
        dto.setHomeplanetId(homeplanetId);
        dto.setBodyform(bodyform);
        dto.setBodyformId(bodyformId);
        dto.setLookingFor(lookingFor);
        dto.setLookingForId(lookingForId);
        dto.setBio(bio);
        dto.setInterests(interests);
        dto.setInterestIds(interestIds);
        dto.setProfilePic(profilePicSrc);

        return dto;
    }


    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private MatchUserRepository matchUserRepository;

    @Autowired
    private MatchesSeeder matchesSeeder;

    @Test
    void testListenAndSaveNewUser() {

        var profile1 = createProfileDTO(
                UUID.randomUUID(),
                "testuser",
                30,
                "Earth",
                1,
                "Humanoid",
                2,
                "Friendship",
                3,
                "This is a test bio",
                Set.of("Reading", "Gaming"),
                List.of(10, 20),
                "/images/testuser.png"
        );

        matchesSeeder.seedMatches();

        var newProfileEvent = new ProfileChangedEvent(profile1);

        eventPublisher.publishEvent(newProfileEvent);

        var queryResult = matchUserRepository.findById(profile1.getId());
        var results = matchUserRepository.findAll();
        assertTrue(queryResult.isPresent());
    }

    @Test
    void testListenAndModifyUser() {

        var initialProfile = createProfileDTO(
                UUID.randomUUID(),
                "testuser",
                30,
                "Earth",
                1,
                "Humanoid",
                2,
                "Friendship",
                3,
                "This is a test bio",
                Set.of("Reading", "Gaming"),
                List.of(10, 20),
                "/images/testuser.png"
        );

        var updatedProfile = createProfileDTO(
                UUID.randomUUID(),
                "testuser",
                30,
                "Mars",
                2,
                "Humanoid",
                3,
                "Friendship",
                3,
                "This is a test bio",
                Set.of("Reading", "Gaming"),
                List.of(10, 20),
                "/images/testuser.png"
        );

        matchUserRepository.deleteAll();
        matchesSeeder.seedMatches();

        var newProfileEvent = new ProfileChangedEvent(initialProfile);
        eventPublisher.publishEvent(newProfileEvent);
        var queryResult = matchUserRepository.findById(initialProfile.getId());
        var result = matchUserRepository.findAll();
        assertTrue(queryResult.isPresent());


        var ProfileChangedEvent = new ProfileChangedEvent(updatedProfile);
        eventPublisher.publishEvent(ProfileChangedEvent);
        var queryResult2 = matchUserRepository.findById(updatedProfile.getId());
        var result2 = matchUserRepository.findAll();
        assertEquals("testuser", queryResult2.get().getUsername());

        assertNotEquals(queryResult.get().getHomeplanet(), queryResult2.get().getHomeplanet());

    }
}