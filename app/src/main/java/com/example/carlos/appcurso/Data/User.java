package com.example.carlos.appcurso.Data;

import static com.example.carlos.appcurso.R.id.points;

/**
 * Created by Carlos on 31/01/2017.
 */

public class User {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoints4() {
        return points4;
    }

    public int getPoints6() { return points6; }

    public void setPoints4(int points) {
        this.points4 = points;
    }

    public void setPoints6(int points) {
        this.points6 = points;
    }

    private String name;
    private int points4;
    private int points6;

    /*public User() {

    }*/


}
