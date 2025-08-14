package tech.kood.match_me.user_management.internal.features.getUser.events;

import tech.kood.match_me.user_management.internal.features.getUser.requests.GetUserByEmailQuery;
import tech.kood.match_me.user_management.internal.features.getUser.results.GetUserByEmailQueryResults;

public record GetUserByEmailEvent(GetUserByEmailQuery request, GetUserByEmailQueryResults result) {
}
