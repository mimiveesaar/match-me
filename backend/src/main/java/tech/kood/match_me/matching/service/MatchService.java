package tech.kood.match_me.matching.service;

import java.util.List;

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
        return userRepository.findByFilter(filter);
    }
}
