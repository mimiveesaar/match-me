package tech.kood.match_me.user_management.internal.mappers;

import org.springframework.stereotype.Component;
import tech.kood.match_me.user_management.api.DTOs.GetAccessTokenResultsDTO;
import tech.kood.match_me.user_management.internal.features.jwt.getAccessToken.GetAccessTokenResults;

@Component
public final class GetAccessTokenMapper {

        public GetAccessTokenResultsDTO toDTO(GetAccessTokenResults result) {
                return switch (result) {
                        case GetAccessTokenResults.Success success -> new GetAccessTokenResultsDTO.Success(
                                        success.jwt(), success.tracingId());
                        case GetAccessTokenResults.InvalidToken invalidToken -> new GetAccessTokenResultsDTO.InvalidToken(
                                        invalidToken.token(), invalidToken.tracingId());
                        case GetAccessTokenResults.InvalidRequest invalidRequest -> new GetAccessTokenResultsDTO.InvalidRequest(
                                        invalidRequest.message(), invalidRequest.tracingId());
                        case GetAccessTokenResults.SystemError systemError -> new GetAccessTokenResultsDTO.SystemError(
                                        systemError.message(), systemError.tracingId());
                };
        }
}
