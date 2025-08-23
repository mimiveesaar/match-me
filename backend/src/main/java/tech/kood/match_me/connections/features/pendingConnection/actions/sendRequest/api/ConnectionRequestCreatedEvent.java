package tech.kood.match_me.connections.features.pendingConnection.actions.sendRequest.api;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ConnectionRequestCreatedEvent(@NotNull UUID value) {


}
