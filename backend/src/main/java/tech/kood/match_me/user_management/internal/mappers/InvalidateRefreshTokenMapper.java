package tech.kood.match_me.user_management.internal.mappers;

import org.springframework.stereotype.Component;
import tech.kood.match_me.user_management.api.DTOs.InvalidateRefreshTokenResultsDTO;
import tech.kood.match_me.user_management.internal.features.refreshToken.invalidateToken.InvalidateRefreshTokenResults;

@Component
public final class InvalidateRefreshTokenMapper {

    public InvalidateRefreshTokenResultsDTO toDTO(InvalidateRefreshTokenResults results) {
        return switch (results) {
            case InvalidateRefreshTokenResults.Success success -> new InvalidateRefreshTokenResultsDTO.Success(
                    success.tracingId());

            case InvalidateRefreshTokenResults.TokenNotFound tokenNotFound -> new InvalidateRefreshTokenResultsDTO.TokenNotFound(
                    tokenNotFound.token(), tokenNotFound.tracingId());

            case InvalidateRefreshTokenResults.InvalidRequest invalidRequest -> new InvalidateRefreshTokenResultsDTO.InvalidRequest(
                    invalidRequest.message(), invalidRequest.tracingId());

            case InvalidateRefreshTokenResults.SystemError systemError -> new InvalidateRefreshTokenResultsDTO.SystemError(
                    systemError.message(), systemError.tracingId());
        };
    }
}
