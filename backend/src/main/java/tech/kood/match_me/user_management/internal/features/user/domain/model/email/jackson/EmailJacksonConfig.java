package tech.kood.match_me.user_management.internal.features.user.domain.model.email.jackson;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.Module;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.kood.match_me.user_management.internal.features.user.domain.model.email.Email;
import tech.kood.match_me.user_management.internal.features.user.domain.model.email.EmailFactory;

@Configuration
public class EmailJacksonConfig {

    private final EmailFactory emailFactory;

    public EmailJacksonConfig(EmailFactory emailFactory) {
        this.emailFactory = emailFactory;
    }

    @Bean
    public Module emailJacksonModule() {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Email.class, new EmailDeserializer(emailFactory));
        module.addSerializer(Email.class, new EmailSerializer());
        return module;
    }
}