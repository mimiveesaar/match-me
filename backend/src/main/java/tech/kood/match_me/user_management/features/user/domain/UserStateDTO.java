package tech.kood.match_me.user_management.features.user.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public record UserStateDTO(@JsonValue int code) {
    public UserStateDTO(int code) {
        this.code = code;
    }

    @JsonCreator
    public static UserStateDTO fromCode(int code) {
        return new UserStateDTO(code);
    }
}