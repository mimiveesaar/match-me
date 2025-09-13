package tech.kood.match_me.user_management.features.user.internal.persistance.userEntity;


import java.time.Instant;
import java.util.UUID;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.layered.InfrastructureLayer;


@InfrastructureLayer
public final class UserEntity {

    @NotNull
    private final UUID id;

    @NotBlank
    @Email
    private final String email;

    @NotBlank
    private final String passwordHash;

    @NotBlank
    private final String passwordSalt;

    @NotNull
    private final Instant createdAt;

    @NotNull
    private final Instant updatedAt;


    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getPasswordSalt() {
        return passwordSalt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    UserEntity(
            UUID id,
            String email,
            String passwordHash,
            String passwordSalt,
            Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.passwordSalt = passwordSalt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UserEntity withId(UUID id) {
        return new UserEntity(id, this.email, this.passwordHash, this.passwordSalt,
                this.createdAt, this.updatedAt);
    }

    public UserEntity withEmail(String email) {
        return new UserEntity(this.id, email, this.passwordHash, this.passwordSalt,
                this.createdAt, this.updatedAt);
    }

    public UserEntity withUsername(String username) {
        return new UserEntity(this.id, this.email, username, this.passwordHash,
                this.createdAt, this.updatedAt);
    }

    public UserEntity withPasswordHash(String passwordHash) {
        return new UserEntity(this.id, this.email, passwordHash, this.passwordSalt,
                this.createdAt, this.updatedAt);
    }

    public UserEntity withPasswordSalt(String passwordSalt) {
        return new UserEntity(this.id, this.email, this.passwordHash, passwordSalt,
                this.createdAt, this.updatedAt);
    }

    public UserEntity withCreatedAt(Instant createdAt) {
        return new UserEntity(this.id, this.email, this.passwordHash,
                this.passwordSalt, createdAt, this.updatedAt);
    }

    public UserEntity withUpdatedAt(Instant updatedAt) {
        return new UserEntity(this.id, this.email, this.passwordHash,
                this.passwordSalt, this.createdAt, updatedAt);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserEntity that = (UserEntity) o;
        return this.hashCode() == that.hashCode();
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "UserEntity{" + "id=" + id + ", email='" + email + '\''
                + '\'' + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + '}';
    }
}
