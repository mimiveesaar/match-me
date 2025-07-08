package tech.kood.match_me.profile.database.repositories;

import org.springframework.data.repository.CrudRepository;

import tech.kood.match_me.profile.entity.UserProfile;

public interface UserProfileRepository extends CrudRepository<UserProfile, String> {
}
