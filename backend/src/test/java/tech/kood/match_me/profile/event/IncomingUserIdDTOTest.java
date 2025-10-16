package tech.kood.match_me.profile.event;

import static org.junit.Assert.assertTrue;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;
import tech.kood.match_me.common.domain.api.UserIdDTO;
import tech.kood.match_me.profile.ProfileDataSeeder;
import tech.kood.match_me.profile.common.ProfileTestBase;
import tech.kood.match_me.profile.repository.ProfileRepository;
import tech.kood.match_me.user_management.features.user.actions.RegisterUser;


@SpringBootTest
@Transactional(transactionManager = "profileManagementTransactionManager")
public class IncomingUserIdDTOTest extends ProfileTestBase {

    private UserIdDTO dto = new UserIdDTO(UUID.randomUUID());

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ProfileDataSeeder profileDataSeeder;

    @Test
    void testListenAndSaveNewUser() {

        var newUserEvent = new RegisterUser.UserRegistered(dto);

        eventPublisher.publishEvent(newUserEvent);

        var queryResult = profileRepository.findById(dto.value());
        var results = profileRepository.findAll();
        assertTrue(queryResult.isPresent());
    }
};


