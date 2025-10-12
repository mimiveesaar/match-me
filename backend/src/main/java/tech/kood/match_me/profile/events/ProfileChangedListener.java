package tech.kood.match_me.profile.events;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ProfileChangedListener {

    @EventListener
    public void onProfileChanged(ProfileChangedDTOEvent event) {
        System.out.println("📢 Profile changed for user: " + event.getProfile().getUsername());
    }
}
