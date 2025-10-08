package tech.kood.match_me.common.dto;

import java.util.UUID;

public class LookingForDTO {
    private final Integer id;
    private final String name;

    public LookingForDTO(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() { return id; }
    public String getName() { return name; }
}

