package tech.kood.match_me.profile.events;

import java.util.UUID;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import tech.kood.match_me.profile.service.ProfileService;
import tech.kood.match_me.user_management.features.user.actions.RegisterUser;

@Component
public class UserRegisteredListener {

    private final ProfileService profileService;

    public UserRegisteredListener(ProfileService profileService) {
        this.profileService = profileService;
    }

    @EventListener
    public void handleUserRegistered(RegisterUser.UserRegistered event) {

        UUID id = event.userId().value();

        profileService.createProfileForUser(id);
        System.out.println("📢 New user registered with ID: " + id);

    }

}
