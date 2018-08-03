package app.model;

import java.io.Serializable;

public class User implements Serializable {

    public final String login;

    public User(String login) {
        this.login = login;
    }
}
