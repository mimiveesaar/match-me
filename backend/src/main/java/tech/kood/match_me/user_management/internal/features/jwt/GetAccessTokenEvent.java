package tech.kood.match_me.user_management.internal.features.jwt;

public record GetAccessTokenEvent(
        GetAccessTokenRequest request,
        GetAccessTokenResults results) {
}
