package tech.kood.match_me.chatspace.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import tech.kood.match_me.chatspace.model.User;

public interface ChatUserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);
}
