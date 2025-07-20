package tech.kood.match_me.user_management.internal.features.getUser.events;

import tech.kood.match_me.user_management.internal.features.getUser.requests.GetUserByEmailRequest;
import tech.kood.match_me.user_management.internal.features.getUser.results.GetUserByEmailResults;


public record GetUserByEmailEvent(
    GetUserByEmailRequest request,
    GetUserByEmailResults result
) {}