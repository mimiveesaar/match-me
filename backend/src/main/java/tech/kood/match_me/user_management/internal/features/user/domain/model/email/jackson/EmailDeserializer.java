package tech.kood.match_me.user_management.internal.features.user.domain.model.email.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import tech.kood.match_me.user_management.internal.features.user.domain.model.email.Email;
import tech.kood.match_me.user_management.internal.features.user.domain.model.email.EmailFactory;

import java.io.IOException;

public class EmailDeserializer extends JsonDeserializer<Email> {

    private final EmailFactory emailFactory;

    public EmailDeserializer(EmailFactory emailFactory) {
        this.emailFactory = emailFactory;
    }

    @Override
    public Email deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
        String value = p.getValueAsString();
        return emailFactory.of(value);
    }
}