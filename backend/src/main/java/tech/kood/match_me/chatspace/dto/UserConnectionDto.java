package tech.kood.match_me.chatspace.dto;

import java.util.UUID;

// Outgoing Dto
public record UserConnectionDto(
        UUID id,
        String username,
        String status
) {}
