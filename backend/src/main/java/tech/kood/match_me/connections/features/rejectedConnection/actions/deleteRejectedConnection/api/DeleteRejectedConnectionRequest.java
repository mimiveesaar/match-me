package tech.kood.match_me.connections.features.rejectedConnection.actions.deleteRejectedConnection.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.cqrs.Command;
import tech.kood.match_me.connections.common.api.ConnectionIdDTO;

@Command
public record DeleteRejectedConnectionRequest(@NotNull @Valid @JsonProperty("connection_id") ConnectionIdDTO connectionIdDTO) {

    public DeleteRejectedConnectionRequest withConnectionId(ConnectionIdDTO connectionIdDTO) {
        return new DeleteRejectedConnectionRequest(connectionIdDTO);
    }
}
