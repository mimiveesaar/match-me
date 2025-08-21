package tech.kood.match_me.profile.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import tech.kood.match_me.profile.model.LookingFor;

public interface LookingForRepository extends JpaRepository<LookingFor, Long> {}
