package tech.kood.match_me.matching.service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import tech.kood.match_me.matching.dto.MatchFilter;
import tech.kood.match_me.matching.dto.MatchResultDto;
import tech.kood.match_me.matching.model.User;
import tech.kood.match_me.matching.model.UserRejection;
import tech.kood.match_me.matching.repository.MatchUserRepository;
import tech.kood.match_me.matching.repository.UserRejectionRepository;

@Service
public class MatchService {

    private final MatchUserRepository userRepository;
    private final MatchScoringService scoringService;
    private final UserRejectionRepository rejectionRepository;

    public MatchService(MatchUserRepository userRepository,
                        MatchScoringService scoringService,
                        UserRejectionRepository rejectionRepository) {
        this.userRepository = userRepository;
        this.scoringService = scoringService;
        this.rejectionRepository = rejectionRepository;
    }

    public List<MatchResultDto> getMatches(MatchFilter filter, User currentUser) {
        // Step 1: Fetch candidates from repository
        List<User> candidates = userRepository.findByFilter(filter);

        // Step 2: Fetch all users rejected by the current user
        Set<UUID> rejectedIds = rejectionRepository.findAllByRejecterId(currentUser.getId())
                                                    .stream()
                                                    .map(UserRejection::getRejectedId)
                                                    .collect(Collectors.toSet());

        // Step 3: Filter out rejected users
        List<User> filteredCandidates = candidates.stream()
                                                  .filter(candidate -> !rejectedIds.contains(candidate.getId()))
                                                  .collect(Collectors.toList());

        // Step 4: Score & tag each candidate
        return filteredCandidates.stream()
                                 .map(candidate -> {
                                     double score = scoringService.calculateScore(currentUser, candidate);
                                     boolean supermatch = scoringService.isSupermatch(score);
                                     return new MatchResultDto(candidate, score, supermatch);
                                 })
                                 .collect(Collectors.toList());
    }
}

