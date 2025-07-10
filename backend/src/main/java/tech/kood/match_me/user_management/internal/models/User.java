package tech.kood.match_me.user_management.internal.models;

import java.util.UUID;

import lombok.Builder;

@Builder
public record User(UUID id, String username, String password, String email) {}