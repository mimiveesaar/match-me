package tech.kood.match_me.user_management.internal.domain.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotBlank;
import tech.kood.match_me.user_management.internal.common.validation.DomainObjectInputValidator;
import tech.kood.match_me.user_management.internal.features.user.domain.model.userId.UserId;

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
    private final String jwt;

    @NotBlank
    private final UserId userId;

    @JsonProperty("jwt")
    @Nonnull
    public String getJwt() {
        return jwt;
    }

    @JsonProperty("userId")
    @Nonnull
    public UserId getUserId() {
        return userId;
    }

    private AccessToken(String jwt, UserId userId) {
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

    @JsonCreator
    public static AccessToken of(String jwt, UserId userId) {
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

    public AccessToken withUserId(UserId newUserId) {
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
