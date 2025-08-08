package tech.kood.match_me.user_management.internal.domain.models;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.user_management.internal.common.validation.DomainObjectInputValidator;

/**
 * Represents a user in the system.
 * <ul>
 * <li>{@code id} - Unique identifier for the user (must not be null).</li>
 * <li>{@code username} - The user's username (must not be null or blank).</li>
 * <li>{@code email} - The user's email address (must not be null or blank).</li>
 * <li>{@code password} - The user's hashed password (must not be null).</li>
 * <li>{@code createdAt} - Timestamp when the user was created (must not be null).</li>
 * <li>{@code updatedAt} - Timestamp when the user was last updated (must not be null).</li>
 * </ul>
 * <p>
 * Use the static factory method {@link #of(UUID, String, String, HashedPassword, Instant, Instant)}
 * to create instances, which performs validation. If validation fails, a
 * {@link ConstraintViolationException} is thrown.
 * </p>
 * <p>
 * Provides "with" methods to create modified copies of the user with updated fields.
 * </p>
 */
public final class User implements Serializable {

    @NotNull
    private final UserId id;

    @NotBlank
    private final String username;

    @Email
    private final String email;

    @NotNull
    private final HashedPassword password;

    @NotNull
    private final Instant createdAt;

    @NotNull
    private final Instant updatedAt;

    @Nonnull
    @JsonProperty("id")
    public UserId getId() {
        return id;
    }

    @Nonnull
    @JsonProperty("username")
    public String getUsername() {
        return username;
    }

    @Nonnull
    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    @Nonnull
    @JsonProperty("password")
    public HashedPassword getPassword() {
        return password;
    }

    @Nonnull
    @JsonProperty("createdAt")
    public Instant getCreatedAt() {
        return createdAt;
    }

    @Nonnull
    @JsonProperty("updatedAt")
    public Instant getUpdatedAt() {
        return updatedAt;
    }

    private User(@NotNull UserId id, @NotBlank String username, @NotBlank String email,
            @NotNull HashedPassword password, @NotNull Instant createdAt,
            @NotNull Instant updatedAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @JsonCreator
    public static User of(@NotNull UserId id, @NotBlank String username, @NotBlank String email,
            @NotNull HashedPassword password, @NotNull Instant createdAt,
            @NotNull Instant updatedAt) {
        var user = new User(id, username, email, password, createdAt, updatedAt);
        var violations = DomainObjectInputValidator.instance.validate(user);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException("Invalid User: " + violations, violations);
        }
        return user;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof User))
            return false;
        User other = (User) obj;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.getValue().hashCode();
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", username='" + username + '\'' + ", email='" + email + '\''
                + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + '}';
    }

    public User withId(UserId newId) {
        return User.of(newId, username, email, password, createdAt, updatedAt);
    }

    public User withUsername(String newUsername) {
        return User.of(id, newUsername, email, password, createdAt, updatedAt);
    }

    public User withEmail(String newEmail) {
        return User.of(id, username, newEmail, password, createdAt, updatedAt);
    }

    public User withPassword(HashedPassword newPassword) {
        return User.of(id, username, email, newPassword, createdAt, updatedAt);
    }

    public User withCreatedAt(Instant newCreatedAt) {
        return User.of(id, username, email, password, newCreatedAt, updatedAt);
    }

    public User withUpdatedAt(Instant newUpdatedAt) {
        return User.of(id, username, email, password, createdAt, newUpdatedAt);
    }


}
