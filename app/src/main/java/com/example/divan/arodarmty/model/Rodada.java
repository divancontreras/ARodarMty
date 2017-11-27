package com.example.divan.arodarmty.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Divan on 11/20/2017.
 */

public class Rodada implements Serializable {
    private String title;
    private String description;
    private double kilometers;
    private Date date;
    private String duration;

    public Rodada() {
    }

    public Rodada(String title, String description, double kilometers, Date date) {
        this.title = title;
        this.description = description;
        this.kilometers = kilometers;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getKilometers(){
        return kilometers;
    }

    public void setKilometers(double kilometers) {
        this.kilometers = kilometers;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
