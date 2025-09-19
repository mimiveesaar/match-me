package tech.kood.match_me.connections.gateway.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import tech.kood.match_me.connections.common.api.ConnectionIdDTO;

public record RejectConnectionDTO(@Valid @JsonProperty("connection_id") ConnectionIdDTO connectionId) {}
