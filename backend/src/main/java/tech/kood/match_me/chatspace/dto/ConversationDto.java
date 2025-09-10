package tech.kood.match_me.chatspace.dto;

import java.util.List;
import java.util.UUID;

public record ConversationDto(
        UUID id,
        List<String> participants,
        List<ChatMessageDTO> messages
) {}