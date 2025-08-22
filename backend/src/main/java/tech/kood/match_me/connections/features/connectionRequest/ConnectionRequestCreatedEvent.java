package tech.kood.match_me.connections.features.connectionRequest;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ConnectionRequestCreatedEvent(@NotNull UUID value) {


}
