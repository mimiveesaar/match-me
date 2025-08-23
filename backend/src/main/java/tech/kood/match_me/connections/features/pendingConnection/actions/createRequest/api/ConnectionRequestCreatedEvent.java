package tech.kood.match_me.connections.features.pendingConnection.actions.createRequest.api;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ConnectionRequestCreatedEvent(@NotNull UUID value) {


}
