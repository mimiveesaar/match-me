package tech.kood.match_me.profile.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tech.kood.match_me.profile.dto.ProfileFilter;
import tech.kood.match_me.profile.model.User;
import tech.kood.match_me.profile.service.ProfileService;

@RestController
@RequestMapping("/api")
public class ProfileController {

    private final ProfileService matchService;

    public ProfileController(ProfileService matchService) {
        this.matchService = matchService;
    }

    @PostMapping("/profile")
    public List<User> getMatches(@RequestBody ProfileFilter filter) {
        System.out.println("got filter: " + filter);
        return matchService.getMatches(filter);
    }
}