package tech.kood.match_me.matching.listeners;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import tech.kood.match_me.matching.model.*;
import tech.kood.match_me.matching.repository.MatchUserRepository;
import tech.kood.match_me.matching.repository.PlanetsRepository;
import tech.kood.match_me.profile.events.ProfileChangedEvent;


import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class IncomingUserEventListener {

    private final MatchUserRepository matchUserRepository;
    private final PlanetsRepository planetsRepository;

    public IncomingUserEventListener(MatchUserRepository matchUserRepository, PlanetsRepository planetsRepository) {
        this.matchUserRepository = matchUserRepository;
        this.planetsRepository = planetsRepository;
    }

    @EventListener
    public void handleProfileChanged(ProfileChangedEvent event) {

        var profile = event.getProfile();

        var bodyformEntity = new BodyformEntity(profile.getBodyformId());
        var homePlanetEntity = new HomeplanetEntity(profile.getHomeplanetId());
        var lookingForEntity = new LookingForEntity(profile.getLookingForId());
        var interests = profile.getInterestIds().stream().map(id -> new InterestEntity(id)).collect(Collectors.toSet());
        var userEntity = new UserEntity(UUID.randomUUID(),"Mimi", 27, homePlanetEntity, bodyformEntity, "mmm", lookingForEntity, interests, profile.getProfilePic());

        matchUserRepository.save(userEntity);
    }
}
