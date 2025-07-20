package tech.kood.match_me.user_management.internal.features.getUser.events;

import java.util.Optional;

import tech.kood.match_me.user_management.internal.features.getUser.requests.GetUserByUsernameRequest;
import tech.kood.match_me.user_management.internal.features.getUser.results.GetUserByUsernameResults;


public record GetUserByUsernameEvent(
    GetUserByUsernameRequest request,
    GetUserByUsernameResults result
) {}