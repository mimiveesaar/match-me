package tech.kood.match_me.matching.adapter;

import org.springframework.stereotype.Component;
import tech.kood.match_me.matching.port.UserQueryPort;
import tech.kood.match_me.matching.dto.*;
import tech.kood.match_me.matching.repository.MatchUserRepository;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Component
public class UserQueryAdapter implements UserQueryPort {

    private final MatchUserRepository matchUserRepository;

    public UserQueryAdapter(MatchUserRepository matchUserRepository) {
        this.matchUserRepository = matchUserRepository;
    }

    @Override
    public Optional<UserDTO> getUserById(UUID id) {
        return matchUserRepository.findById(id).map(user -> new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getAge(),
                user.getBio(),
                user.getProfilepicSrc(),
                user.getHomeplanet() != null ?
                        new HomeplanetDTO(user.getHomeplanet().getId(), user.getHomeplanet().getName()) : null,
                user.getBodyform() != null ?
                        new BodyformDTO(user.getBodyform().getId(), user.getBodyform().getName()) : null,
                user.getLookingFor() != null ?
                        new LookingForDTO(user.getLookingFor().getId(), user.getLookingFor().getName()) : null,
                user.getInterests().stream()
                        .map(i -> new InterestDTO(i.getId(), i.getName()))
                        .collect(Collectors.toSet())
        ));
    }
}

