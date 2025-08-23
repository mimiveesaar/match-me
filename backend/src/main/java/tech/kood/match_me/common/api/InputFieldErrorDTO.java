package tech.kood.match_me.common.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public record InputFieldErrorDTO (
        @JsonProperty("field") String field,
        @JsonProperty("rejected_value") Object rejectedValue,
        @JsonProperty("message") String message) {
}