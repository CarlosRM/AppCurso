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
    private boolean toast;
    private boolean status;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isToast() {
        return toast;
    }

    public boolean isStatus() {
        return status;
    }

    private String image;

    public void setToast(boolean toast) {
        this.toast = toast;
    }

    public boolean getToast () {
        return toast;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean getStatus() {
        return status;
    }

    /*public User() {

    }*/


}
