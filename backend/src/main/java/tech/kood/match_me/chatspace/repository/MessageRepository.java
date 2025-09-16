package tech.kood.match_me.chatspace.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tech.kood.match_me.chatspace.model.Message;
import tech.kood.match_me.chatspace.model.MessageStatus;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {

    List<Message> findByConversationIdOrderByTimestampAsc(UUID conversationId);

    List<Message> findByConversationIdAndSenderIdNotAndStatus(
            UUID conversationId,
            UUID senderId,
            MessageStatus status
    );

}
