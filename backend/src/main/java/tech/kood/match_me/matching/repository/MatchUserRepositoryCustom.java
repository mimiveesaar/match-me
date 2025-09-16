package tech.kood.match_me.matching.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import tech.kood.match_me.matching.dto.MatchFilterDto;
import tech.kood.match_me.matching.model.User;

@Repository
public interface MatchUserRepositoryCustom {
    List<User> findByFilter(MatchFilterDto filter);
}