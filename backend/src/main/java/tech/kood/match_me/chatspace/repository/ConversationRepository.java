package tech.kood.match_me.chatspace.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import tech.kood.match_me.chatspace.model.Conversation;


public interface ConversationRepository extends JpaRepository<Conversation, UUID> {
}
