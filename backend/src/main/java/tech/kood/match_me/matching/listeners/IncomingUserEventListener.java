package tech.kood.match_me.matching.listeners;

import org.springframework.context.event.EventListener;
import tech.kood.match_me.matching.model.User;
import tech.kood.match_me.matching.repository.MatchUserRepository;
import tech.kood.match_me.profile.events.ProfileChangedEvent;

public class IncomingUserEventListener {

    private final MatchUserRepository matchUserRepository;

    public IncomingUserEventListener(MatchUserRepository matchUserRepository) {
        this.matchUserRepository = matchUserRepository;
    }

    @EventListener
    public void handleProfileChanged(ProfileChangedEvent event) {
        User user = event.getUser();
        matchUserRepository.save(user);
    }


}
