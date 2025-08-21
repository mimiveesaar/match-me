package tech.kood.match_me.profile.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import tech.kood.match_me.profile.model.Profile;

public interface ProfileRepository extends JpaRepository<Profile, UUID> {
}