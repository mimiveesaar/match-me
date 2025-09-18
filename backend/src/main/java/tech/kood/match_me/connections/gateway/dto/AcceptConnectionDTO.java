package tech.kood.match_me.connections.gateway.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.connections.common.api.ConnectionIdDTO;

public record AcceptConnectionDTO(@NotNull @Valid @JsonProperty("connection_id") ConnectionIdDTO connectionIdDTO) {
}
