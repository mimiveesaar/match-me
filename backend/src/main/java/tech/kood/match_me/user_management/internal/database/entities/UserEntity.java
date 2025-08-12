

package tech.kood.match_me.user_management.internal.database.entities;



import java.time.Instant;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.user_management.internal.common.validation.DomainObjectInputValidator;


/**
 * Represents a user entity in the system.
 */

public class UserEntity {

    @NotNull
    private final UUID id;

    @NotBlank
    @Email
    private final String email;

    @NotBlank
    private final String username;

    @NotBlank
    private final String passwordHash;

    @NotBlank
    private final String passwordSalt;

    @NotNull
    private final Instant createdAt;

    @NotNull
    private final Instant updatedAt;


    @JsonProperty("id")
    public UUID getId() {
        return id;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    @JsonProperty("username")
    public String getUsername() {
        return username;
    }

    @JsonProperty("passwordHash")
    public String getPasswordHash() {
        return passwordHash;
    }

    @JsonProperty("passwordSalt")
    public String getPasswordSalt() {
        return passwordSalt;
    }

    @JsonProperty("createdAt")
    public Instant getCreatedAt() {
        return createdAt;
    }

    @JsonProperty("updatedAt")
    public Instant getUpdatedAt() {
        return updatedAt;
    }

    private UserEntity(@JsonProperty("id") UUID id, @JsonProperty("email") String email,
            @JsonProperty("username") String username,
            @JsonProperty("passwordHash") String passwordHash,
            @JsonProperty("passwordSalt") String passwordSalt,
            @JsonProperty("createdAt") Instant createdAt,
            @JsonProperty("updatedAt") Instant updatedAt) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.passwordHash = passwordHash;
        this.passwordSalt = passwordSalt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @JsonCreator
    public static UserEntity of(@JsonProperty("id") UUID id, @JsonProperty("email") String email,
            @JsonProperty("username") String username,
            @JsonProperty("passwordHash") String passwordHash,
            @JsonProperty("passwordSalt") String passwordSalt,
            @JsonProperty("createdAt") Instant createdAt,
            @JsonProperty("updatedAt") Instant updatedAt) {
        var entity = new UserEntity(id, email, username, passwordHash, passwordSalt, createdAt,
                updatedAt);

        var violations = DomainObjectInputValidator.instance.validate(entity);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        return entity;
    }

    public UserEntity withId(UUID id) {
        return UserEntity.of(id, this.email, this.username, this.passwordHash, this.passwordSalt,
                this.createdAt, this.updatedAt);
    }

    public UserEntity withEmail(String email) {
        return UserEntity.of(this.id, email, this.username, this.passwordHash, this.passwordSalt,
                this.createdAt, this.updatedAt);
    }

    public UserEntity withUsername(String username) {
        return UserEntity.of(this.id, this.email, username, this.passwordHash, this.passwordSalt,
                this.createdAt, this.updatedAt);
    }

    public UserEntity withPasswordHash(String passwordHash) {
        return UserEntity.of(this.id, this.email, this.username, passwordHash, this.passwordSalt,
                this.createdAt, this.updatedAt);
    }

    public UserEntity withPasswordSalt(String passwordSalt) {
        return UserEntity.of(this.id, this.email, this.username, this.passwordHash, passwordSalt,
                this.createdAt, this.updatedAt);
    }

    public UserEntity withCreatedAt(Instant createdAt) {
        return UserEntity.of(this.id, this.email, this.username, this.passwordHash,
                this.passwordSalt, createdAt, this.updatedAt);
    }

    public UserEntity withUpdatedAt(Instant updatedAt) {
        return UserEntity.of(this.id, this.email, this.username, this.passwordHash,
                this.passwordSalt, this.createdAt, updatedAt);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserEntity that = (UserEntity) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "UserEntity{" + "id=" + id + ", email='" + email + '\'' + ", username='" + username
                + '\'' + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + '}';
    }
}
