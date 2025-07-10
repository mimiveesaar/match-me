package tech.kood.match_me.user_management.internal.common;

import java.util.Optional;

public interface UserManagementResult {
    // Marker interface for user management results

    default Optional<String> tracingId() {
        return Optional.empty();
    }
}