package tech.kood.match_me.profile.events;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ProfileEventListener {

    @EventListener
    public void onProfileChanged(ProfileChangedEvent event) {
        System.out.println("📢 Profile changed for user: " + event.getProfile().getUsername());
    }
}