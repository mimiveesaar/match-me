package tech.kood.match_me.common.constants;

public enum Planets {
    Xeron_5(1, "Xeron 5", 45.2f, 130.5f),
    Draknor(2, "Draknor", -33.1f, 102.9f),
    Vega_Prime(3, "Vega Prime", 12.5f, -45f),
    Bloop_X12(4, "Bloop X12", 78.3f, 60.2f),
    Zal_Tek_Major(5, "Zal'Tek Major", -82f, -135.7f),
    Nimbus_9(6, "Nimbus 9", 5.4f, 80.1f),
    Krylon_Beta(7, "Krylon Beta", -25.6f, 145.3f),
    Nova_Eden(8, "Nova Eden", 33.3f, -73.2f),
    Tharnis(9, "Tharnis", -47.8f, 12f),
    Quarnyx_Delta(10, "Quarnyx Delta", 66f, -88.8f),
    Glooporia(11, "Glooporia", 15.2f, 140f),
    Skarn(12, "Skarn", -10.5f, -120.6f),
    Uvuul_4(13, "Uvuul-4", 58.9f, 170f),
    Oortania(14, "Oortania", -60.4f, -40.1f),
    Vrexalon(15, "Vrexalon", 0f, 0f);

    private final int code;
    private final String displayName;
    private final float latitude;
    private final float longitude;

    Planets(int code, String displayName, float latitude, float longitude) {
        this.code = code;
        this.displayName = displayName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int Code() {
        return code;
    }

    public String DisplayName() {
        return displayName;
    }

    public float Latitude() {
        return latitude;
    }

    public float Longitude() {
        return longitude;
    }
}