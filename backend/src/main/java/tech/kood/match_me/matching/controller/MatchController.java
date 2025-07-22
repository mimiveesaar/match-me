package tech.kood.match_me.matching.controller;

import org.springframework.web.bind.annotation.*;
import tech.kood.match_me.matching.model.User;
import tech.kood.match_me.matching.dto.MatchFilter;
import tech.kood.match_me.matching.service.MatchService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MatchController {

    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @PostMapping("/matches")
    public List<User> getMatches(@RequestBody MatchFilter filter) {
        return matchService.getMatches(filter);
    }
}