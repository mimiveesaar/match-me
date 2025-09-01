package tech.kood.match_me.matching.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tech.kood.match_me.matching.dto.MatchFilterDto;
import tech.kood.match_me.matching.dto.MatchResultsDto;
import tech.kood.match_me.matching.model.User;
import tech.kood.match_me.matching.repository.MatchUserRepository;
import tech.kood.match_me.matching.service.MatchService;

@RestController
@RequestMapping("/api")
public class RecommendationController {

    private final MatchService matchService;
    private final MatchUserRepository matchUserRepository; 

    public RecommendationController(MatchService matchService,
                                    MatchUserRepository matchUserRepository) {
        this.matchService = matchService;
        this.matchUserRepository = matchUserRepository;
    }

    @PostMapping("/recommendations/{userId}")
    public List<UUID> getRecommendations(@PathVariable UUID userId,
                                         @RequestBody MatchFilterDto filter) {

        // Fetch the logged-in user using your existing repository
        User currentUser = matchUserRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Return only the IDs
        return matchService.getMatches(filter, currentUser)
                           .stream()
                           .map(MatchResultsDto::getId)
                           .map(UUID::fromString)
                           .toList();
    }
}