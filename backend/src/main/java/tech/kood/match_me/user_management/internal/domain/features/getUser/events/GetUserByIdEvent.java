package tech.kood.match_me.user_management.internal.domain.features.getUser.events;

import tech.kood.match_me.user_management.internal.domain.features.getUser.requests.GetUserByIdQuery;
import tech.kood.match_me.user_management.internal.domain.features.getUser.results.GetUserByIdQueryResults;

public record GetUserByIdEvent(GetUserByIdQuery request, GetUserByIdQueryResults result) {
}
