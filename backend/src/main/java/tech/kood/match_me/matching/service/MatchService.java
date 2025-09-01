package tech.kood.match_me.matching.service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import tech.kood.match_me.matching.dto.MatchFilterDto;
import tech.kood.match_me.matching.dto.MatchResultsDto;
import tech.kood.match_me.matching.model.ConnectionRequest;
import tech.kood.match_me.matching.model.User;
import tech.kood.match_me.matching.model.UserRejection;
import tech.kood.match_me.matching.repository.ConnectionRequestRepository;
import tech.kood.match_me.matching.repository.MatchUserRepository;
import tech.kood.match_me.matching.repository.UserRejectionRepository;

@Service
public class MatchService {

    private final MatchUserRepository userRepository;
    private final MatchScoringService scoringService;
    private final UserRejectionRepository rejectionRepository;
    private final ConnectionRequestRepository connectionRequestRepository;

    public MatchService(MatchUserRepository userRepository,
                        MatchScoringService scoringService,
                        UserRejectionRepository rejectionRepository,
                        ConnectionRequestRepository connectionRequestRepository) {
        this.userRepository = userRepository;
        this.scoringService = scoringService;
        this.rejectionRepository = rejectionRepository;
        this.connectionRequestRepository = connectionRequestRepository;
    }

    public List<MatchResultsDto> getMatches(MatchFilterDto filter, User currentUser) {
        // Step 1: Fetch candidates from repository
        List<User> candidates = userRepository.findByFilter(filter);

        // Step 2: Fetch all users rejected by the current user
        Set<UUID> rejectedIds = rejectionRepository.findAllByRejecterId(currentUser.getId())
                                                    .stream()
                                                    .map(UserRejection::getRejectedId)
                                                    .collect(Collectors.toSet());

        // Step 2.5: Fetch all users who have already been sent a connection request by the current user
        Set<UUID> connectionRequestedIds = connectionRequestRepository.findAllByRequesterId(currentUser.getId())
                                                                     .stream()
                                                                     .map(ConnectionRequest::getRequestedId)
                                                                     .collect(Collectors.toSet());

        // Step 3: Filter out rejected users & users that have already been sent a connection request and the current user themselves
        List<User> filteredCandidates = candidates.stream()
                                                  .filter(candidate -> !rejectedIds.contains(candidate.getId()))
                                                  .filter(candidate -> !connectionRequestedIds.contains(candidate.getId()))
                                                  .filter(candidate -> !candidate.getId().equals(currentUser.getId()))
                                                  .collect(Collectors.toList());

        // Step 4: Score & tag each candidate
        return filteredCandidates.stream()
                                 .map(candidate -> {
                                     double score = scoringService.calculateScore(currentUser, candidate);
                                     boolean supermatch = scoringService.isSupermatch(score);
                                     return new MatchResultsDto(candidate, score, supermatch);
                                 })
                                 .sorted((a, b) -> Double.compare(b.getScore(), a.getScore())) // highest score first
                                 .collect(Collectors.toList());
    }
}

