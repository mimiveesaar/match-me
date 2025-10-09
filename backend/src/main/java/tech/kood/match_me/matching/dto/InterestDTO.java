package tech.kood.match_me.matching.dto;

public class InterestDTO {
    private final Long id;
    private final String name;

    public InterestDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
}
