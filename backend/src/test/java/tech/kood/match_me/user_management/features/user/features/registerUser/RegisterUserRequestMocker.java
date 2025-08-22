package tech.kood.match_me.user_management.features.user.features.registerUser;

import java.util.UUID;

import org.springframework.stereotype.Component;
import com.github.javafaker.Faker;
import tech.kood.match_me.user_management.common.domain.api.EmailDTO;
import tech.kood.match_me.user_management.common.domain.api.PasswordDTO;
import tech.kood.match_me.user_management.features.user.utils.passwordFaker.PasswordFaker;
import tech.kood.match_me.user_management.features.user.features.registerUser.api.RegisterUserRequest;

@Component
public class RegisterUserRequestMocker {

    public static Faker faker = new Faker();

    public RegisterUserRequest createValidRequest() {
        var requestId = UUID.randomUUID();
        var emailDto = new EmailDTO(faker.internet().emailAddress());
        var passwordDto = new PasswordDTO(PasswordFaker.generatePassword(8,16,true,true));
        return new RegisterUserRequest(requestId, emailDto, passwordDto, null);
    }

    public RegisterUserRequest createInvalidPasswordRequest() {
        return createValidRequest().withPassword(new PasswordDTO("s"));
    }

    public RegisterUserRequest createNullRequest() {
        return createValidRequest().withPassword(null).withEmail(null);
    }

    public RegisterUserRequest createInvalidEmailRequest() {
        return createValidRequest().withEmail(new EmailDTO("invalid-email"));
    }

    public RegisterUserRequest createLongPasswordRequest() {
        String longPassword = "p".repeat(300);
        return createValidRequest().withPassword(new PasswordDTO(longPassword));
    }
}