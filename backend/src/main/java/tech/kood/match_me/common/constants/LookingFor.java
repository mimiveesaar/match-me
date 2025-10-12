package tech.kood.match_me.common.constants;

public enum LookingFor {
    Friendship(1,"Friendship"),
    Romance(2, "Romance"),
    Strategic_Alliance(3, "Strategic Alliance"),
    Coparenting_Hatchlings(4, "Coparenting Hatchlings"),
    Host_Symbiosis(5, "Host Symbiosis"),
    Chtulhu(6, "Chtulhu");

    private final int code;
    private final String displayName;

    LookingFor(int code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    public int Code() {
        return code;
    }

    public String DisplayName() {
        return displayName;
    }
}