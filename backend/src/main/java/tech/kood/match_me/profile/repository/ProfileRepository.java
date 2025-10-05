package tech.kood.match_me.profile.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import tech.kood.match_me.profile.model.Profile;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, UUID> {

    @Query("SELECT DISTINCT p FROM Profile p " + "LEFT JOIN FETCH p.homeplanet "
            + "LEFT JOIN FETCH p.bodyform " + "LEFT JOIN FETCH p.lookingFor "
            + "LEFT JOIN FETCH p.interests " + "WHERE p.id = :id")
    Profile findByIdWithRelations(UUID id);

}
