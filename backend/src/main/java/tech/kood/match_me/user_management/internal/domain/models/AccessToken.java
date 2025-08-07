package tech.kood.match_me.user_management.internal.domain.models;

public record AccessToken(String jwt, String userId) {
    public AccessToken {
        if (jwt == null || jwt.isBlank()) {
            throw new IllegalArgumentException("Access token cannot be null or blank");
        }
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("User ID cannot be null or blank");
        }
    }

    public AccessToken withJwt(String newJwt) {
        return new AccessToken(newJwt, this.userId);
    }

    public AccessToken withUserId(String newUserId) {
        return new AccessToken(this.jwt, newUserId);
    }
}
