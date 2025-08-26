package tech.kood.match_me.profile.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import tech.kood.match_me.profile.model.Interest;

public interface InterestRepository extends JpaRepository<Interest, Long> {}

