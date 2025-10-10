package tech.kood.match_me.profile.events;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import tech.kood.match_me.profile.model.Profile;

@Component
public class ProfileChangedListener {

    @EventListener
    public void onProfileChanged(ProfileChangedEvent event) {
        Profile profile = event.getProfile();
        System.out.println("📢 Profile changed for user: " + profile.getUsername());
        // TODO: Call external module, enqueue Kafka message, etc.
    }
}
