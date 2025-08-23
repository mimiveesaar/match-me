package tech.kood.match_me.user_management.features.user.actions.login.api;


import tech.kood.match_me.user_management.common.domain.api.UserIdDTO;

public record UserLoggedInEvent(
        UserIdDTO userId) {
}
