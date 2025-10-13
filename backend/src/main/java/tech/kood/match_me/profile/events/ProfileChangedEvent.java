package tech.kood.match_me.profile.events;

import tech.kood.match_me.profile.dto.ProfileViewDTO;

public class ProfileChangedEvent{
    private final ProfileViewDTO profile;

    public ProfileChangedEvent(ProfileViewDTO profile) {
        this.profile = profile;
    }

    public ProfileViewDTO getProfile() {
        return profile;
    }
}
