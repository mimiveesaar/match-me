package tech.kood.match_me.profile.service;

import java.util.List;

import org.springframework.stereotype.Service;

import tech.kood.match_me.profile.dto.ProfileFilter;
import tech.kood.match_me.profile.model.User;
import tech.kood.match_me.profile.repository.ProfileUserRepository;


@Service
public class ProfileService {

    private final ProfileUserRepository userRepository;

    public ProfileService(ProfileUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getMatches(ProfileFilter filter) {
        return userRepository.findByFilter(filter);
    }
}
