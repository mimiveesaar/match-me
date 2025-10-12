package tech.kood.match_me.user_management.features.user.domain.internal.user;

public enum UserState {
    PENDING(0),
    ACTIVE(1),
    DELETED(2);

    private final int code;

    UserState(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static UserState fromCode(int code) {
        for (UserState state : UserState.values()) {
            if (state.getCode() == code) {
                return state;
            }
        }
        throw new IllegalArgumentException("Invalid user state code: " + code);
    }
}