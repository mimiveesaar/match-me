package tech.kood.match_me.profile.repository;

import java.util.List;

import tech.kood.match_me.profile.dto.ProfileFilter;
import tech.kood.match_me.profile.model.User;

public interface ProfileUserRepositoryCustom {
    List<User> findByFilter(ProfileFilter filter);
}