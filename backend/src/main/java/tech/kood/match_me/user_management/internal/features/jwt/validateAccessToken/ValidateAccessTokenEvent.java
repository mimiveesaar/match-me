package tech.kood.match_me.user_management.internal.features.jwt.validateAccessToken;

public record ValidateAccessTokenEvent(
        ValidateAccessTokenRequest request,
        ValidateAccessTokenResults results) {

}
