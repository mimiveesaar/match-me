package tech.kood.match_me.common.constants;

public enum Bodyforms {

    Gelatinous(1, "Gelatinous"),
    Tentacled(2, "Tentacled"),
    Humanoid(3, "Humanoid"),
    Energy_Based(4, "Energy Based"),
    Mechanical(5, "Mechanical"),
    Insectoid(6, "Insectoid"),
    Reptilian(7, "Reptilian"),
    Gas_Cloud(8, "Gas Cloud"),
    Crystalline(9, "Crystalline"),
    Mimetic_Blob(10, "Mimetic Blob");

    private final int code;
    private final String displayName;

    // Constructor
    Bodyforms(int code, String displayName) {
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
