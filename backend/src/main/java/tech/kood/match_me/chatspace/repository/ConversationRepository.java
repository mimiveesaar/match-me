package tech.kood.match_me.chatspace.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import tech.kood.match_me.chatspace.model.Conversation;
import tech.kood.match_me.chatspace.model.User;

public interface ConversationRepository extends JpaRepository<Conversation, UUID> {

    @Query("SELECT c FROM Conversation c LEFT JOIN FETCH c.participants LEFT JOIN FETCH c.messages WHERE c.id = :id")
    Optional<Conversation> findByIdWithParticipantsAndMessages(@Param("id") UUID id);

    @Query("SELECT c FROM Conversation c JOIN c.participants p1 JOIN c.participants p2 WHERE p1 = :user1 AND p2 = :user2")
    Optional<Conversation> findByParticipants(@Param("user1") User user1, @Param("user2") User user2);

     @Query("""
        SELECT c 
        FROM Conversation c
        JOIN c.participants p1
        JOIN c.participants p2
        LEFT JOIN FETCH c.participants
        LEFT JOIN FETCH c.messages
        WHERE p1 = :user1 AND p2 = :user2
    """)
    Optional<Conversation> findByParticipantsWithData(@Param("user1") User user1, @Param("user2") User user2);
}
