package tech.kood.match_me.user_management.internal.features.user.domain.model.password;


import org.jmolecules.architecture.layered.DomainLayer;
import org.jmolecules.ddd.types.ValueObject;
import tech.kood.match_me.user_management.internal.features.user.domain.validation.password.ValidPassword;


@DomainLayer
public final class Password implements ValueObject {

    @ValidPassword
    private final String value;

    Password(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
