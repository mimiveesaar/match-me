package tech.kood.match_me.matching.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import tech.kood.match_me.matching.dto.CurrentUserDto;
import tech.kood.match_me.matching.dto.MatchFilterDto;
import tech.kood.match_me.matching.dto.MatchResponseDto;
import tech.kood.match_me.matching.dto.MatchResultsDto;
import tech.kood.match_me.matching.model.User;
import tech.kood.match_me.matching.repository.MatchUserRepository;
import tech.kood.match_me.matching.service.MatchService;

@RestController
@RequestMapping("/api")
public class MatchController {

    private final MatchService matchService;
    private final MatchUserRepository userRepository; // to load current user
    private final ObjectMapper objectMapper = new ObjectMapper();

    public MatchController(MatchService matchService,
            MatchUserRepository userRepository) {
        this.matchService = matchService;
        this.userRepository = userRepository;
    }

    @PostMapping("/matches/{userId}")
    public MatchResponseDto getMatches(
            @PathVariable UUID userId,
            @RequestBody String rawBody) {   // accept raw body first

        // Log the raw JSON
        System.out.println("RAW JSON BODY: " + rawBody);

        try {
            MatchFilterDto filter = objectMapper.readValue(rawBody, MatchFilterDto.class);

            User currentUser = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            List<MatchResultsDto> matches = matchService.getMatches(filter, currentUser);

            return new MatchResponseDto(new CurrentUserDto(currentUser), matches);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse JSON", e);
        }
    }
}
