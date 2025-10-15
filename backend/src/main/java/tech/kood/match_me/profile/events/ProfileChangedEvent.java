package tech.kood.match_me.profile.events;

import tech.kood.match_me.profile.dto.ProfileDTO_New;

public class ProfileChangedEvent{
    private final ProfileDTO_New profile;

    public ProfileChangedEvent(ProfileDTO_New profile) {
        this.profile = profile;
    }

    public ProfileDTO_New getProfile() {
        return profile;
    }
}
