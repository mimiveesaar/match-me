package tech.kood.match_me.chatspace.repository;


import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import tech.kood.match_me.chatspace.model.Message;



public interface MessageRepository extends JpaRepository<Message, UUID> {

    List<Message> findByConversationIdOrderByTimestampAsc(UUID conversationId);

}