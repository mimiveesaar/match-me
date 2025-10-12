package tech.kood.match_me.profile.events;

import org.springframework.context.ApplicationEvent;
import tech.kood.match_me.profile.dto.ProfileViewDTO;

public class ProfileChangedDTOEvent extends ApplicationEvent {
    private final ProfileViewDTO profile;

    public ProfileChangedDTOEvent(Object source, ProfileViewDTO profile) {
        super(source);
        this.profile = profile;
    }

    public ProfileViewDTO getProfile() {
        return profile;
    }
}
