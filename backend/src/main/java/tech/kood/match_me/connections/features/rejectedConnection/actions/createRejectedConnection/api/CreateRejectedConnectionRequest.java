package tech.kood.match_me.connections.features.rejectedConnection.actions.createRejectedConnection.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.cqrs.Command;
import tech.kood.match_me.common.domain.api.UserIdDTO;
import tech.kood.match_me.connections.features.rejectedConnection.actions.createRejectedConnection.api.validation.ValidCreateRejectedConnection;
import tech.kood.match_me.connections.features.rejectedConnection.domain.api.RejectedConnectionReasonDTO;

import java.io.Serializable;

@Command
@ValidCreateRejectedConnection
public record CreateRejectedConnectionRequest(@NotNull @Valid @JsonProperty("rejected_by_user") UserIdDTO rejectedByUser,
                                              @NotNull @Valid @JsonProperty("rejected_user") UserIdDTO rejectedUser,
                                              @NotNull @JsonProperty("reason") RejectedConnectionReasonDTO reason) implements Serializable {


    public CreateRejectedConnectionRequest withRejectedByUser(UserIdDTO rejectedByUser) {
        return new CreateRejectedConnectionRequest(rejectedByUser, rejectedUser, reason);
    }

    public CreateRejectedConnectionRequest withRejectedUser(UserIdDTO rejectedUser) {
        return new CreateRejectedConnectionRequest(rejectedByUser, rejectedUser, reason);
    }

    public CreateRejectedConnectionRequest withReason(RejectedConnectionReasonDTO reason) {
        return new CreateRejectedConnectionRequest(rejectedByUser, rejectedUser, reason);
    }
}
