package tech.kood.match_me.user_management.models;

import java.util.UUID;

public record User(UUID id, String username, String password, String email) {}