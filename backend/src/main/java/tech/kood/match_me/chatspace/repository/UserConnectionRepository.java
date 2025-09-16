package tech.kood.match_me.chatspace.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import tech.kood.match_me.chatspace.model.UserConnection;

@Repository
public interface UserConnectionRepository extends JpaRepository<UserConnection, UUID> {

    @Query("SELECT uc FROM UserConnection uc JOIN FETCH uc.connectedUser JOIN FETCH uc.user "
            + "WHERE uc.user.id = :userId OR uc.connectedUser.id = :userId")
    List<UserConnection> findByUserIdWithConnectedUser(UUID userId);

}
