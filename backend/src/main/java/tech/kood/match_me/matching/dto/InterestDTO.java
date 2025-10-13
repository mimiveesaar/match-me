package tech.kood.match_me.matching.dto;

public class InterestDTO {
    private final Integer id;
    private final String name;

    public InterestDTO(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() { return id; }
    public String getName() { return name; }
}
