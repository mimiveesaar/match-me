package tech.kood.match_me.profile.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.kood.match_me.profile.model.Profile;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, UUID> {
    boolean existsByUsername(String username);
    
    Optional<Profile> findByUserId(UUID userId);
    
    @Query("SELECT p FROM Profile p " +
           "LEFT JOIN FETCH p.homeplanet " +
           "LEFT JOIN FETCH p.bodyform " +
           "LEFT JOIN FETCH p.lookingFor " +
           "LEFT JOIN FETCH p.interests " +
           "WHERE p.id = :id")
    Profile findByIdWithRelations(@Param("id") UUID id);
    
    @Query("SELECT p FROM Profile p " +
           "LEFT JOIN FETCH p.homeplanet " +
           "LEFT JOIN FETCH p.bodyform " +
           "LEFT JOIN FETCH p.lookingFor " +
           "LEFT JOIN FETCH p.interests " +
           "WHERE p.userId = :userId")
    Optional<Profile> findByUserIdWithRelations(@Param("userId") UUID userId);
}
