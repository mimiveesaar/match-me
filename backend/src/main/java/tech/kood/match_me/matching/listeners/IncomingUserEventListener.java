package tech.kood.match_me.matching.listeners;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import tech.kood.match_me.matching.model.*;
import tech.kood.match_me.matching.repository.*;
import tech.kood.match_me.profile.events.ProfileChangedEvent;

import java.util.stream.Collectors;

@Component
public class IncomingUserEventListener {

    private final MatchUserRepository matchUserRepository;
    private final PlanetsRepository planetsRepository;
    private final InterestsRepository interestsRepository;
    private final LookingForRepository lookingForRepository;

    private final BodyformRepository bodyformRepository;

    public IncomingUserEventListener(MatchUserRepository matchUserRepository,
            PlanetsRepository planetsRepository, InterestsRepository interestsRepository,
            LookingForRepository lookingForRepository, BodyformRepository bodyformRepository) {
        this.matchUserRepository = matchUserRepository;
        this.planetsRepository = planetsRepository;
        this.interestsRepository = interestsRepository;
        this.lookingForRepository = lookingForRepository;
        this.bodyformRepository = bodyformRepository;
    }

    @EventListener
    public void handleProfileChanged(ProfileChangedEvent event) {

        var profile = event.getProfile();

        var bodyformEntity = bodyformRepository.getReferenceById(profile.getBodyformId());
        var homePlanetEntity = planetsRepository.getReferenceById(profile.getHomeplanetId());
        var lookingForEntity = lookingForRepository.getReferenceById(profile.getLookingForId());
        var interestEntities = profile.getInterestIds().stream()
                .map(interestsRepository::getReferenceById).collect(Collectors.toSet());
        var userEntity = new UserEntity(profile.getId(), profile.getName(), profile.getAge(),
                homePlanetEntity, bodyformEntity, profile.getBio(), lookingForEntity,
                interestEntities, profile.getProfilePic());

        matchUserRepository.save(userEntity);
    }
}
