package tech.kood.match_me.user_management.internal.features.login;

import java.util.Optional;

import tech.kood.match_me.user_management.models.RefreshToken;
import tech.kood.match_me.user_management.models.User;

public sealed interface LoginResults
        permits LoginResults.Success,
        LoginResults.InvalidCredentials,
        LoginResults.InvalidRequest,
        LoginResults.SystemError {

    record Success(RefreshToken refreshToken, User user, Optional<String> tracingId) implements LoginResults {
    }

    record InvalidCredentials(String username, String password, Optional<String> tracingId)
            implements LoginResults {
        public InvalidCredentials(String username, String password) {
            this(username, password, Optional.empty());
        }
    }

    record InvalidRequest(String message, Optional<String> tracingId) implements LoginResults {
        public InvalidRequest() {
            this("Invalid login request.", Optional.empty());
        }
    }

    record SystemError(String message, Optional<String> tracingId) implements LoginResults {
        public SystemError() {
            this("An unexpected error occurred during login.", Optional.empty());
        }
    }
}