package tech.kood.match_me.connections.features.acceptedConnection.actions.createConnection.api;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.cqrs.Command;
import tech.kood.match_me.common.domain.api.UserIdDTO;

@Command
public record CreateAcceptedConnectionRequest(@NotNull @Valid @JsonProperty("accepted_by_user") UserIdDTO acceptedByUser,
        @NotNull @Valid @JsonProperty("accepted_user") UserIdDTO acceptedUser) implements Serializable {

    public CreateAcceptedConnectionRequest withAcceptedByUser(UserIdDTO acceptedByUser) {
        return new CreateAcceptedConnectionRequest(acceptedByUser, acceptedUser);
    }

    public CreateAcceptedConnectionRequest withAcceptedUser(UserIdDTO acceptedUser) {
        return new CreateAcceptedConnectionRequest(acceptedByUser, acceptedUser);
    }
}
