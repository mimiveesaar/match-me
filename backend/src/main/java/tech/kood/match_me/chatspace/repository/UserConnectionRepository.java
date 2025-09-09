package tech.kood.match_me.chatspace.repository;


import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import tech.kood.match_me.chatspace.model.UserConnection;

public interface UserConnectionRepository extends JpaRepository<UserConnection, UUID> {
    List<UserConnection> findByUserId(UUID userId);
}
