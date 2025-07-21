package tech.kood.match_me.matching;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MatchController {

    private final UserRepository userRepository;

    public MatchController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/matches")
    public List<User> getMatches(@RequestBody MatchFilter filter) {
        List<User> users = userRepository.findAll();

        return users.stream()
                .filter(u -> u.getAge() >= filter.getMinAge() && u.getAge() <= filter.getMaxAge())
                .filter(u -> filter.getBodyform().isEmpty() || filter.getBodyform().contains(u.getBodyform()))
                .filter(u -> filter.getInterests().isEmpty() || !disjoint(u.getInterests(), filter.getInterests()))
                .filter(u -> filter.getLookingFor().isEmpty() || !disjoint(u.getLookingFor(), filter.getLookingFor()))
                // Distance filtering requires calculation; implement if you want here
                .collect(Collectors.toList());
    }

    private boolean disjoint(List<String> list1, List<String> list2) {
        return list1.stream().noneMatch(list2::contains);
    }
}