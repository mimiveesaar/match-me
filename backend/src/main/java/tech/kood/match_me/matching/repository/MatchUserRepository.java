package tech.kood.match_me.matching.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tech.kood.match_me.matching.model.UserEntity;

@Repository
public interface MatchUserRepository extends JpaRepository<UserEntity, UUID>, MatchUserRepositoryCustom {

    List<UserEntity> findByHomeplanetId(Integer homeplanetId);
    List<UserEntity> findByLookingForId(Integer lookingForId);
    List<UserEntity> findByBodyformId(Integer bodyformId);
    List<UserEntity> findByHomeplanetIdAndLookingForIdAndBodyformId(Integer homeplanetId, Integer lookingForId, Integer bodyformId);
}