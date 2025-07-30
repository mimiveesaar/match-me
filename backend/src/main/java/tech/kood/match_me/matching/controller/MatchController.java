package tech.kood.match_me.matching.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tech.kood.match_me.matching.dto.MatchFilter;
import tech.kood.match_me.matching.model.User;
import tech.kood.match_me.matching.service.MatchService;

@RestController
@RequestMapping("/api")
public class MatchController {

    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @PostMapping("/matches")
    public List<User> getMatches(@RequestBody MatchFilter filter) {
        System.out.println("got filter: " + filter);
        return matchService.getMatches(filter);
    }
}
