package tech.kood.match_me.user_management.internal.features.login;

import java.util.Optional;
import java.util.UUID;

public record LoginRequest(
                UUID requestId,
                String email,
                String password,
                Optional<String> tracingId) {
}