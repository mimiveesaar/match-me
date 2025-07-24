
package tech.kood.match_me.user_management.api.DTOs.registerUser;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "RegisterUserResultsDTO", discriminatorProperty = "kind",
                description = "Base interface for all user registration results",
                oneOf = {RegisterUserSuccessDTO.class, RegisterUserUsernameExistsDTO.class,
                                RegisterUserEmailExistsDTO.class, RegisterUserInvalidEmailDTO.class,
                                RegisterUserInvalidPasswordDTO.class,
                                RegisterUserInvalidUsernameDTO.class,
                                RegisterUserSystemErrorDTO.class})
public sealed interface RegisterUserResultsDTO permits RegisterUserSuccessDTO,
                RegisterUserUsernameExistsDTO, RegisterUserEmailExistsDTO,
                RegisterUserInvalidEmailDTO, RegisterUserInvalidPasswordDTO,
                RegisterUserInvalidUsernameDTO, RegisterUserSystemErrorDTO {



}
