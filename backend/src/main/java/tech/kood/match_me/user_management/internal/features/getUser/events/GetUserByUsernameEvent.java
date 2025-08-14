package tech.kood.match_me.user_management.internal.features.getUser.events;

import tech.kood.match_me.user_management.internal.features.getUser.requests.GetUserByUsernameQuery;
import tech.kood.match_me.user_management.internal.features.getUser.results.GetUserByUsernameQueryResults;

public record GetUserByUsernameEvent(GetUserByUsernameQuery request,
        GetUserByUsernameQueryResults result) {
}
