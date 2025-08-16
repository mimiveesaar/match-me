package tech.kood.match_me.user_management.internal.features.user.domain.model.password;


import org.jmolecules.architecture.layered.DomainLayer;
import org.jmolecules.ddd.types.ValueObject;
import tech.kood.match_me.user_management.internal.features.user.domain.validation.password.ValidPassword;


/**
 * Represents a user's password in plain-text form.
 * <p>
 * This class encapsulates a plain-text password value, which is subject to validation
 * via the {@link tech.kood.match_me.user_management.internal.features.user.domain.validation.password.ValidPassword} annotation.
 * <p>
 * <b>Security Warning:</b> The password is stored as a plain-text string in memory.
 * It is recommended to avoid persisting or logging this value, and to hash passwords before storage.
 */
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
