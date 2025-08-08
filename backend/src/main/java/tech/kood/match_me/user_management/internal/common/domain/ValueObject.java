package tech.kood.match_me.user_management.internal.common.domain;

public interface ValueObject {
    boolean equals(Object o);

    int hashCode();
}
