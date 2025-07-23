package tech.kood.match_me.user_management.internal.features.login;

import java.util.Optional;

import tech.kood.match_me.user_management.models.RefreshToken;
import tech.kood.match_me.user_management.models.User;

public sealed interface LoginRequestResults
        permits LoginRequestResults.Success,
        LoginRequestResults.InvalidCredentials,
        LoginRequestResults.InvalidRequest,
        LoginRequestResults.SystemError {

    record Success(RefreshToken token, User user, Optional<String> tracingId) implements LoginRequestResults {
    }

    record InvalidCredentials(String username, String password, Optional<String> tracingId)
            implements LoginRequestResults {
        public InvalidCredentials(String username, String password) {
            this(username, password, Optional.empty());
        }
    }

    record InvalidRequest(String message, Optional<String> tracingId) implements LoginRequestResults {
        public InvalidRequest() {
            this("Invalid login request.", Optional.empty());
        }
    }

    record SystemError(String message, Optional<String> tracingId) implements LoginRequestResults {
        public SystemError() {
            this("An unexpected error occurred during login.", Optional.empty());
        }
    }
}