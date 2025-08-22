package tech.kood.match_me.user_management.common.domain.internal.password;


import org.jmolecules.architecture.layered.DomainLayer;
import org.jmolecules.ddd.types.ValueObject;
import tech.kood.match_me.user_management.common.validation.password.ValidPassword;


/**
 * Represents a userId's password in plain-text form.
 * <p>
 * This class encapsulates a plain-text password value, which is subject to validation
 * via the {@link ValidPassword} annotation.
 * <p>
 * <b>Security Warning:</b> The password is stored as a plain-text string in memory.
 */
@DomainLayer
public final class Password implements ValueObject {

    @ValidPassword
    private final String value;

    Password(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
