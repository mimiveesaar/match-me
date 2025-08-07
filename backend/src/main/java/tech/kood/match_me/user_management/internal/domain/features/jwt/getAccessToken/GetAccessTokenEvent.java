package tech.kood.match_me.user_management.internal.domain.features.jwt.getAccessToken;

public record GetAccessTokenEvent(
        GetAccessTokenRequest request,
        GetAccessTokenResults results) {
}
