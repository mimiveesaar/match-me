package tech.kood.match_me.connections.features.acceptedConnection.actions.createConnection.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.connections.features.acceptedConnection.domain.api.AcceptedConnectionDTO;

public record AcceptedConnectionCreatedEvent(@NotNull @Valid AcceptedConnectionDTO acceptedConnectionDTO) {
}
