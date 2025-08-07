package tech.kood.match_me.user_management.api.endpoints;

import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import tech.kood.match_me.user_management.api.UserManagementPublisher;
import tech.kood.match_me.user_management.api.DTOs.GetAccessTokenRequestDTO;
import tech.kood.match_me.user_management.api.DTOs.GetAccessTokenResultsDTO;
import tech.kood.match_me.user_management.internal.domain.features.jwt.getAccessToken.GetAccessTokenHandler;
import tech.kood.match_me.user_management.internal.domain.features.jwt.getAccessToken.GetAccessTokenRequest;
import tech.kood.match_me.user_management.internal.mappers.GetAccessTokenMapper;

@RestController
@RequestMapping("/api/v1/user-management/auth")
@Tag(name = "User Management")
public class GetAccessTokenEndpoint {

        private final GetAccessTokenMapper getAccessTokenMapper;

        private final UserManagementPublisher userManagementPublisher;

        public GetAccessTokenEndpoint(GetAccessTokenHandler getAccessTokenHandler,
                        GetAccessTokenMapper getAccessTokenMapper,
                        UserManagementPublisher userManagementPublisher) {
                this.getAccessTokenMapper = getAccessTokenMapper;
                this.userManagementPublisher = userManagementPublisher;
        }

        @PostMapping("/access-token")
        @ApiResponses(value = {@ApiResponse(responseCode = "200",
                        description = "Access token retrieved successfully.",
                        content = @Content(mediaType = "application/json", schema = @Schema(
                                        implementation = GetAccessTokenResultsDTO.Success.class))),
                        @ApiResponse(responseCode = "400", description = "Invalid request.",
                                        content = @Content(mediaType = "application/json",
                                                        schema = @Schema(
                                                                        discriminatorProperty = "kind",
                                                                        implementation = GetAccessTokenResultsDTO.InvalidRequest.class))),
                        @ApiResponse(responseCode = "401", description = "Invalid refresh token.",
                                        content = @Content(mediaType = "application/json",
                                                        schema = @Schema(
                                                                        implementation = GetAccessTokenResultsDTO.InvalidToken.class))),
                        @ApiResponse(responseCode = "500", description = "Internal server error.",
                                        content = @Content(mediaType = "application/json",
                                                        schema = @Schema(
                                                                        implementation = GetAccessTokenResultsDTO.SystemError.class)))})

        public ResponseEntity<GetAccessTokenResultsDTO> getAccessToken(
                        @RequestBody GetAccessTokenRequestDTO request) {

                var internalRequest = new GetAccessTokenRequest(request.refreshToken(),
                                UUID.randomUUID().toString());


                var result = userManagementPublisher.publish(internalRequest);
                var responseDTO = getAccessTokenMapper.toDTO(result);

                return switch (responseDTO) {
                        case GetAccessTokenResultsDTO.Success success -> ResponseEntity.ok(success);
                        case GetAccessTokenResultsDTO.InvalidToken invalidToken -> ResponseEntity
                                        .badRequest().body(invalidToken);
                        case GetAccessTokenResultsDTO.InvalidRequest invalidRequest -> ResponseEntity
                                        .badRequest().body(invalidRequest);
                        case GetAccessTokenResultsDTO.SystemError systemError -> ResponseEntity
                                        .status(500).body(systemError);
                };

        }
}
