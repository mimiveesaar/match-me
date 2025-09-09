package tech.kood.match_me.user_management.features.user.actions.registerUser;

import org.springframework.stereotype.Component;
import com.github.javafaker.Faker;
import tech.kood.match_me.user_management.common.domain.api.EmailDTO;
import tech.kood.match_me.user_management.common.domain.api.PasswordDTO;
import tech.kood.match_me.user_management.features.user.utils.passwordFaker.PasswordFaker;
import tech.kood.match_me.user_management.features.user.actions.registerUser.api.RegisterUserRequest;

@Component
public class RegisterUserRequestMocker {

    public static Faker faker = new Faker();

    public RegisterUserRequest createValidRequest() {
        var emailDto = new EmailDTO(faker.internet().emailAddress());
        var passwordDto = new PasswordDTO(PasswordFaker.generatePassword(8,16,true,true));
        return new RegisterUserRequest(emailDto, passwordDto);
    }

    public RegisterUserRequest createInvalidPasswordRequest() {
        var emailDto = new EmailDTO(faker.internet().emailAddress());
        return new RegisterUserRequest(emailDto, new PasswordDTO("s"));
    }

    public RegisterUserRequest createNullRequest() {
        return new RegisterUserRequest(null, null);
    }

    public RegisterUserRequest createInvalidEmailRequest() {
        var passwordDto = new PasswordDTO(PasswordFaker.generatePassword(8,16,true,true));
        return new RegisterUserRequest(new EmailDTO("invalid-email"), passwordDto);
    }

    public RegisterUserRequest createLongPasswordRequest() {
        String longPassword = "p".repeat(300);
        var emailDto = new EmailDTO(faker.internet().emailAddress());
        return new RegisterUserRequest(emailDto, new PasswordDTO(longPassword));
    }
}