package tech.kood.match_me.user_management.internal.domain.features.login;

import java.io.Serializable;
import jakarta.annotation.Nullable;

public record LoginRequest(String requestId, String email, String password,
                @Nullable String tracingId) implements Serializable {
}
