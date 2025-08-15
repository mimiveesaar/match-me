package tech.kood.match_me.user_management.internal.features.user.domain.model.email.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import tech.kood.match_me.user_management.internal.features.user.domain.model.email.Email;
import java.io.IOException;

public class EmailSerializer extends StdSerializer<Email> {
    public EmailSerializer() {
        super(Email.class);
    }

    @Override
    public void serialize(Email value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeString(value.toString());
    }
}
