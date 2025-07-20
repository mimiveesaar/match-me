
package tech.kood.match_me.user_management.internal.features.getUser.events;


import tech.kood.match_me.user_management.internal.features.getUser.requests.GetUserByIdRequest;
import tech.kood.match_me.user_management.internal.features.getUser.results.GetUserByIdResults;

public record GetUserByIdEvent(GetUserByIdRequest request, GetUserByIdResults result) {}