package tech.kood.match_me.user_management.internal.mappers;

import org.springframework.stereotype.Component;
import tech.kood.match_me.user_management.api.DTOs.GetAccessTokenResultsDTO;
import tech.kood.match_me.user_management.internal.domain.features.jwt.createAccessToken.CreateAccessTokenResults;

@Component
public final class GetAccessTokenMapper {

        public GetAccessTokenResultsDTO toDTO(CreateAccessTokenResults result) {
                return switch (result) {
                        case CreateAccessTokenResults.Success success -> new GetAccessTokenResultsDTO.Success(
                                        success.getJwt(), success.getTracingId());
                        case CreateAccessTokenResults.InvalidToken invalidToken -> new GetAccessTokenResultsDTO.InvalidToken(
                                        invalidToken.getToken(), invalidToken.getTracingId());
                        case CreateAccessTokenResults.SystemError systemError -> new GetAccessTokenResultsDTO.SystemError(
                                        systemError.getMessage(), systemError.getTracingId());
                };
        }
}
