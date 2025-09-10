package tech.kood.match_me.chatspace.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import tech.kood.match_me.chatspace.model.UserConnection;

public interface UserConnectionRepository extends JpaRepository<UserConnection, UUID> {

     @Query("SELECT uc FROM UserConnection uc JOIN FETCH uc.connectedUser WHERE uc.user.id = :userId")
    List<UserConnection> findByUserIdWithConnectedUser(UUID userId);

}
