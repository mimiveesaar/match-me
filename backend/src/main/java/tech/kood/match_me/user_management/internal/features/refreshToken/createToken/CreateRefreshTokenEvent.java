package tech.kood.match_me.user_management.internal.features.refreshToken.createToken;

public record CreateRefreshTokenEvent(CreateRefreshTokenRequest request, CreateRefreshTokenResults result) {
    /**
     * Event representing the refresh token request.
     *
     * @param request The refresh token request; must not be {@code null}
     * @param result  The result of the refresh token operation; must not be
     *                {@code null}
     * @throws IllegalArgumentException if {@code request} or {@code result} is
     *                                  {@code null}
     */
    public CreateRefreshTokenEvent {
        if (request == null || result == null) {
            throw new IllegalArgumentException("Request and result must not be null");
        }
    }
}
