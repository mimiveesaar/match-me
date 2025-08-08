package tech.kood.match_me.user_management.internal.domain.features.getUser.events;

import tech.kood.match_me.user_management.internal.domain.features.getUser.requests.GetUserByUsernameQuery;
import tech.kood.match_me.user_management.internal.domain.features.getUser.results.GetUserByUsernameResults;

public record GetUserByUsernameEvent(GetUserByUsernameQuery request,
        GetUserByUsernameResults result) {
}
