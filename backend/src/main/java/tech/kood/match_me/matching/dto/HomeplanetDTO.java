package tech.kood.match_me.matching.dto;

public class HomeplanetDTO {
    private final Integer id;
    private final String name;

    public HomeplanetDTO(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() { return id; }
    public String getName() { return name; }
}
