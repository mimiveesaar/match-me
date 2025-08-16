package tech.kood.match_me.user_management.internal.features.getUser.events;

import tech.kood.match_me.user_management.internal.features.getUser.results.GetUserByIdQueryResults;

public record GetUserByIdEvent(GetUserByIdQuery request, GetUserByIdQueryResults result) {
}
