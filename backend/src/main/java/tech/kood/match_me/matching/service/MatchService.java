package tech.kood.match_me.matching.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import tech.kood.match_me.matching.dto.MatchFilter;
import tech.kood.match_me.matching.model.User;
import tech.kood.match_me.matching.repository.MatchUserRepository;

@Service
public class MatchService {

    private final MatchUserRepository userRepository;

    public MatchService(MatchUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getMatches(MatchFilter filter) {
        List<User> users = userRepository.findAll();

        return users.stream()
                .filter(user -> user.getAge() >= filter.getMinAge() && user.getAge() <= filter.getMaxAge())
                .filter(user -> filter.getBodyform().isEmpty() || filter.getBodyform().contains(user.getBodyform()))
                .filter(user -> filter.getInterests().isEmpty() || !disjoint(user.getInterests(), filter.getInterests()))
                .filter(user -> filter.getLookingFor().isEmpty() || filter.getLookingFor().contains(user.getLookingFor()))
                .filter(user -> filter.getHomeplanet().isEmpty() || filter.getHomeplanet().contains(user.getHomeplanet()))
                .collect(Collectors.toList());
    }

    private boolean disjoint(List<String> list1, List<String> list2) {
        return list1.stream().noneMatch(list2::contains);
    }
}