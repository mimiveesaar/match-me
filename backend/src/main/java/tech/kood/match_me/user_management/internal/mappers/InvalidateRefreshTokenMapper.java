package tech.kood.match_me.user_management.internal.mappers;

import org.springframework.stereotype.Component;
import tech.kood.match_me.user_management.api.DTOs.InvalidateRefreshTokenResultsDTO;
import tech.kood.match_me.user_management.internal.domain.features.refreshToken.invalidateToken.InvalidateRefreshTokenResults;

@Component
public final class InvalidateRefreshTokenMapper {

        public InvalidateRefreshTokenResultsDTO toDTO(InvalidateRefreshTokenResults results) {
                return switch (results) {
                        case InvalidateRefreshTokenResults.Success success -> new InvalidateRefreshTokenResultsDTO.Success(
                                        success.getTracingId());

                        case InvalidateRefreshTokenResults.TokenNotFound tokenNotFound -> new InvalidateRefreshTokenResultsDTO.TokenNotFound(
                                        tokenNotFound.getRefreshToken(),
                                        tokenNotFound.getTracingId());

                        case InvalidateRefreshTokenResults.InvalidRequest invalidRequest -> new InvalidateRefreshTokenResultsDTO.InvalidRequest(
                                        invalidRequest.getMessage(), invalidRequest.getTracingId());

                        case InvalidateRefreshTokenResults.SystemError systemError -> new InvalidateRefreshTokenResultsDTO.SystemError(
                                        systemError.getMessage(), systemError.getTracingId());
                };
        }
}
