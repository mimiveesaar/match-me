package tech.kood.match_me.profile.events;

import org.springframework.context.ApplicationEvent;
import tech.kood.match_me.profile.model.Profile;

public class ProfileChangedEvent extends ApplicationEvent {
    private final Profile profile;

    public ProfileChangedEvent(Object source, Profile profile) {
        super(source);
        this.profile = profile;
    }

    public Profile getProfile() {
        return profile;
    }
}
