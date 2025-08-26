package tech.kood.match_me.matching.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import tech.kood.match_me.matching.dto.MatchFilter;
import tech.kood.match_me.matching.dto.MatchResultDto;
import tech.kood.match_me.matching.model.User;
import tech.kood.match_me.matching.repository.MatchUserRepository;

@Service
public class MatchService {

    private final MatchUserRepository userRepository;
    private final MatchScoringService scoringService;

    public MatchService(MatchUserRepository userRepository,
            MatchScoringService scoringService) {
        this.userRepository = userRepository;
        this.scoringService = scoringService;
    }

    public List<MatchResultDto> getMatches(MatchFilter filter, User currentUser) {
        // Step 1: Fetch candidates from repository
        List<User> candidates = userRepository.findByFilter(filter);

        // Step 2: Score & tag each candidate
        return candidates.stream()
                .map(candidate -> {
                    double score = scoringService.calculateScore(currentUser, candidate);
                    boolean supermatch = scoringService.isSupermatch(score);
                    return new MatchResultDto(candidate, score, supermatch);
                })
                .collect(Collectors.toList());
    }
}

