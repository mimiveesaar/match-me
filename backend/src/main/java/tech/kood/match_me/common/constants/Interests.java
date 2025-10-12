package tech.kood.match_me.common.constants;

public enum Interests {
    Telepathic_Chess(1, "Telepathic Chess"),
    Black_Hole_Karaoke(2, "Black Hole Karaoke"),
    Baking(3, "Baking"),
    Binary_Poetry(4, "Binary Poetry"),
    Painting(5, "Painting"),
    Parallel_Parking(6, "Parallel Parking"),
    Reading(7, "Reading"),
    Collecting_Rocks(8, "Collecting Rocks"),
    Butterfly_watching(9, "Butterfly-watching"),
    Plasma_Sculpting(10, "Plasma Sculpting"),
    Terraforming(11, "Terraforming"),
    Zero_G_Yoga(12, "Zero-G Yoga"),
    Fishing(13, "Fishing"),
    Galactic_Geocaching(14, "Galactic Geocaching"),
    Nebula_Photography(15, "Nebula Photography"),
    Starship_Racing(16, "Starship Racing"),
    Archaeology(17, "Archaeology"),
    Cooking(18, "Cooking"),
    Lightspeed_Surfing(19, "Lightspeed Surfing"),
    Wormhole_Navigation(20, "Wormhole Navigation"),
    Cryosleep(21, "Cryosleep"),
    Martian_Mining(22, "Martian Mining"),
    Solarwind(23, "Solarwind"),
    Sailing(24, "Sailing"),
    Meditation(25, "Meditation"),
    Opera_Singing(26, "Opera Singing"),
    Ballet(27, "Ballot"),
    Fashion_Design(28, "Fashion Design"),
    Black_Market(29, "Black Market"),
    Trading(30, "Trading"),
    Cosmic_Comics(31, "Cosmic Comics"),
    Meteorite_Hunting(32, "Meteorite Hunting"),
    Exoplanet_Exploration(33, "Exoplanet Exploration"),
    StarMap(34, "StarMap"),
    Galactic_Diplomacy(35, "Galactic Diplomacy"),
    Gardening(36, "Gardening"),
    Interstellar_DJing(37, "Interstellar DJing"),
    Teleportation_Tricks(38, "Teleporation Tricks"),
    Brewing(39, "Brewing"),
    Droid_Repair(40, "Droid Repair"),
    Cryptography(41, "Cryptography"),
    Wormhole_Jumping(42, "Wormhole Jumping");


    private final int code;
    private final String displayName;

    Interests(int code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    public String DisplayName() {
        return displayName;
    }

    public int Code() {
        return code;
    }
}