package tech.kood.match_me.profile.events;

import tech.kood.match_me.profile.dto.ProfileDTO;

public class ProfileChangedEvent{
    private final ProfileDTO profile;

    public ProfileChangedEvent(ProfileDTO profile) {
        this.profile = profile;
    }

    public ProfileDTO getProfile() {
        return profile;
    }
}
