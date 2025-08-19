package tech.kood.match_me.matching.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tech.kood.match_me.matching.dto.MatchFilter;
import tech.kood.match_me.matching.dto.MatchResultDto;
import tech.kood.match_me.matching.model.User;
import tech.kood.match_me.matching.repository.MatchUserRepository;
import tech.kood.match_me.matching.service.MatchService;


@RestController
@RequestMapping("/api")
public class MatchController {

    private final MatchService matchService;
    private final MatchUserRepository userRepository; // to load current user

    public MatchController(MatchService matchService,
                           MatchUserRepository userRepository) {
        this.matchService = matchService;
        this.userRepository = userRepository;
    }

    @PostMapping("/matches/{userId}")
    public List<MatchResultDto> getMatches(@PathVariable UUID userId,
                                           @RequestBody MatchFilter filter) {
        System.out.println("got filter: " + filter);

        // Load current user from DB
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Call service with both filter + current user
        return matchService.getMatches(filter, currentUser);
    }
}


// @RestController
// @RequestMapping("/api")
// public class MatchController {

//     private final MatchService matchService;

//     public MatchController(MatchService matchService) {
//         this.matchService = matchService;
//     }

//     @PostMapping("/matches")
//     public List<User> getMatches(@RequestBody MatchFilter filter) {
//         System.out.println("got filter: " + filter);
//         return matchService.getMatches(filter);
//     }
// }
