package tech.kood.match_me.user_management.internal.features.user.domain.model;

import java.time.Instant;

import jakarta.annotation.Nonnull;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.ddd.types.AggregateRoot;
import tech.kood.match_me.user_management.internal.features.user.domain.model.email.Email;
import tech.kood.match_me.user_management.internal.features.user.domain.model.hashedPassword.HashedPassword;
import tech.kood.match_me.user_management.internal.features.user.domain.model.userId.UserId;

public class User implements AggregateRoot<User, UserId> {

    @Valid
    @NotNull
    private final UserId id;

    @Valid
    @NotNull
    private final Email email;

    @Valid
    @NotNull
    private final HashedPassword hashedPassword;

    @NotNull
    private final Instant createdAt;

    @NotNull
    private final Instant updatedAt;

    @Nonnull
    public UserId getId() {
        return id;
    }

    @Nonnull
    public Email getEmail() {
        return email;
    }

    @Nonnull
    public HashedPassword getHashedPassword() {
        return hashedPassword;
    }

    @Nonnull
    public Instant getCreatedAt() {
        return createdAt;
    }

    @Nonnull
    public Instant getUpdatedAt() {
        return updatedAt;
    }

    User(UserId id, Email email, HashedPassword password, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.email = email;
        this.hashedPassword = password;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof User that)) return false;
        return hashCode() == that.hashCode();
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + " email='" + email + '\'' + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + '}';
    }
}