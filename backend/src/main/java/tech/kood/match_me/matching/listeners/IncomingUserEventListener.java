package tech.kood.match_me.matching.listeners;

import org.springframework.context.event.EventListener;
import tech.kood.match_me.matching.model.*;
import tech.kood.match_me.matching.repository.MatchUserRepository;
import tech.kood.match_me.profile.events.ProfileChangedEvent;
import tech.kood.match_me.profile.model.Profile;

import java.util.HashSet;
import java.util.UUID;

public class IncomingUserEventListener {

    private final MatchUserRepository matchUserRepository;

    public IncomingUserEventListener(MatchUserRepository matchUserRepository) {
        this.matchUserRepository = matchUserRepository;
    }

    @EventListener
    public void handleProfileChanged(ProfileChangedEvent event) {

        var profile = event.getProfile();

        var bodyformEntity = new Bodyform(profile.getBodyform().getId());
        var homePlanetEntity = new Homeplanet(1, "Earth", 12.1, 13.1 );
        var lookingForEntity = new LookingFor(1, "...");
        var interestsEntity = new Interest( "kkk");
        var interestsSetEntity = new HashSet<Interest>();
        var userEntity = new User(UUID.randomUUID(),"Mimi", 27, homePlanetEntity, bodyformEntity, "mmm", lookingForEntity, interestsSetEntity, "");

        matchUserRepository.save(user);
    }


}
