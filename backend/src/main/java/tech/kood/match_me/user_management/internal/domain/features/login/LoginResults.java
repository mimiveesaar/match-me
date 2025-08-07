package tech.kood.match_me.user_management.internal.domain.features.login;

import jakarta.annotation.Nullable;
import tech.kood.match_me.user_management.internal.domain.models.RefreshToken;
import tech.kood.match_me.user_management.internal.domain.models.User;

public sealed interface LoginResults permits LoginResults.Success, LoginResults.InvalidCredentials,
        LoginResults.InvalidRequest, LoginResults.SystemError {

    record Success(RefreshToken refreshToken, User user, @Nullable String tracingId)
            implements LoginResults {
    }


    record InvalidCredentials(String username, String password, @Nullable String tracingId)
            implements LoginResults {
        public InvalidCredentials(String username, String password) {
            this(username, password, null);
        }
    }


    record InvalidRequest(String message, @Nullable String tracingId) implements LoginResults {
        public InvalidRequest() {
            this("Invalid login request.", null);
        }
    }

    record SystemError(String message, @Nullable String tracingId) implements LoginResults {
        public SystemError() {
            this("An unexpected error occurred during login.", null);
        }
    }
}
