package tech.kood.match_me.user_management.common.domain.internal.accessToken;

import java.util.Objects;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.ddd.types.ValueObject;
import tech.kood.match_me.common.domain.internal.userId.UserId;


public class AccessToken implements ValueObject {

    @NotBlank
    private final String jwt;

    @NotNull
    @Valid
    private final UserId userId;

    public String getJwt() {
        return jwt;
    }

    public UserId getUserId() {
        return userId;
    }

     AccessToken(String jwt, UserId userId) {
        this.jwt = jwt;
        this.userId = userId;
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
        return "AccessToken{" +
                "jwt='" + jwt + '\'' +
                ", userId=" + userId +
                '}';
    }
}
