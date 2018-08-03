package app.model;

import java.io.Serializable;

public class Repo implements Serializable {

    public final long id;
    public final String name;
    public final String description;
    public final User owner;
    public final long stars;
    public final long forks;

    public Repo(long id, String name, String description, User owner, long stars, long forks) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.owner = owner;
        this.stars = stars;
        this.forks = forks;
    }
}
