package tech.kood.match_me.user_management.features.user.actions.registerUser;

import java.util.UUID;

import org.springframework.stereotype.Component;
import com.github.javafaker.Faker;
import tech.kood.match_me.user_management.common.domain.api.EmailDTO;
import tech.kood.match_me.user_management.common.domain.api.PasswordDTO;
import tech.kood.match_me.user_management.features.user.actions.RegisterUser;
import tech.kood.match_me.user_management.features.user.utils.passwordFaker.PasswordFaker;

@Component
public class RegisterUserRequestMocker {

    public static Faker faker = new Faker();

    public RegisterUser.Request createValidRequest() {
        var emailDto = new EmailDTO(faker.internet().emailAddress());
        var passwordDto = new PasswordDTO(PasswordFaker.generatePassword(8,16,true,true));
        return new RegisterUser.Request(emailDto, passwordDto);
    }

    public RegisterUser.Request createInvalidPasswordRequest() {
        return createValidRequest().withPassword(new PasswordDTO("s"));
    }

    public RegisterUser.Request createNullRequest() {
        return createValidRequest().withPassword(null).withEmail(null);
    }

    public RegisterUser.Request createInvalidEmailRequest() {
        return createValidRequest().withEmail(new EmailDTO("invalid-email"));
    }

    public RegisterUser.Request createLongPasswordRequest() {
        String longPassword = "p".repeat(300);
        return createValidRequest().withPassword(new PasswordDTO(longPassword));
    }
}