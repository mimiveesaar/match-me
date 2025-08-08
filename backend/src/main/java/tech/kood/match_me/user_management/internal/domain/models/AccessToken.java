package tech.kood.match_me.user_management.internal.domain.models;

import java.util.Objects;
import jakarta.validation.constraints.NotBlank;
import tech.kood.match_me.user_management.internal.common.validation.DomainObjectInputValidator;

/**
 * Represents an access token (JWT) and its associated user ID.
 * <p>
 * This class is immutable and ensures that both the JWT and userId are non-null and non-blank.
 * </p>
 *
 * <p>
 * Example usage:
 * 
 * <pre>
 * AccessToken token = AccessToken.of("jwtValue", "userIdValue");
 * </pre>
 * </p>
 */
public final class AccessToken {

    @NotBlank
    public final String jwt;

    @NotBlank
    public final String userId;

    private AccessToken(String jwt, String userId) {
        this.jwt = jwt;
        this.userId = userId;
    }

    /**
     * Static factory method to create an AccessToken instance with validation.
     *
     * @param jwt the JWT string (must not be null or blank)
     * @param userId the user ID (must not be null or blank)
     * @return a new AccessToken instance
     * @throws IllegalArgumentException if jwt or userId are invalid
     */
    public static AccessToken of(String jwt, String userId) {
        var accessToken = new AccessToken(jwt, userId);
        var violations = DomainObjectInputValidator.instance.validate(accessToken);
        if (!violations.isEmpty()) {
            throw new IllegalArgumentException("Invalid AccessToken: " + violations);
        }
        return accessToken;
    }

    public AccessToken withJwt(String newJwt) {
        return AccessToken.of(newJwt, this.userId);
    }

    public AccessToken withUserId(String newUserId) {
        return AccessToken.of(this.jwt, newUserId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof AccessToken))
            return false;
        AccessToken that = (AccessToken) o;
        return jwt.equals(that.jwt) && userId.equals(that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jwt, userId);
    }

    @Override
    public String toString() {
        return "AccessToken{" + "jwt='" + jwt + '\'' + ", userId='" + userId + '\'' + '}';
    }
}
