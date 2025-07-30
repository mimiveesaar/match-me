package tech.kood.match_me.matching.repository;

import java.util.List;

import tech.kood.match_me.matching.dto.MatchFilter;
import tech.kood.match_me.matching.model.User;

public interface MatchUserRepositoryCustom {
    List<User> findByFilter(MatchFilter filter);
}