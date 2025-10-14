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

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional(transactionManager = "matchingTransactionManager")
public class IncomingProfileEventTest extends MatchingTestBase {

    private ProfileViewDTO createProfileDTO (UUID id,
                                             String username,
                                             String Name,
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

        ProfileViewDTO dto = new ProfileViewDTO();
        dto.setId(id);
        dto.setUsername(username);
        dto.setName(Name);
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
                "Test User",
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

        var profileDTO2 = new ProfileViewDTO();
        profileDTO.setId(UUID.randomUUID());
        profileDTO.setUsername("testuser");
        profileDTO.setName("Test User");
        profileDTO.setAge(30);
        profileDTO.setHomeplanet("Earth");
        profileDTO.setHomeplanetId(2);
        profileDTO.setBodyform("Humanoid");
        profileDTO.setBodyformId(3);
        profileDTO.setLookingFor("Friendship");
        profileDTO.setLookingForId(4);
        profileDTO.setBio("This is a test bio.");
        profileDTO.setInterests(new HashSet<>(Arrays.asList("Reading", "Gaming")));
        profileDTO.setInterestIds(Arrays.asList(10, 20));
        profileDTO.setProfilePic("/images/testuser.png");

        matchesSeeder.seedMatches();

        var newProfileEvent = new ProfileChangedEvent(profileDTO);
        eventPublisher.publishEvent(newProfileEvent);
        var queryResult = matchUserRepository.findById(profileDTO.getId());

        var result = matchUserRepository.findAll();
        assertTrue(queryResult.isPresent());


        var ProfileChangedEvent = new ProfileChangedEvent(profileDTO2);
        eventPublisher.publishEvent(ProfileChangedEvent);
        var queryResult2 = matchUserRepository.findById(profileDTO2.getId());

        var result2 = matchUserRepository.findAll();
        assertEquals("testuser", queryResult2.get().getUsername());

    }
}