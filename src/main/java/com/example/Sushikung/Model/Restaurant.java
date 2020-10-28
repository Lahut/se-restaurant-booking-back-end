package com.example.Sushikung.Model;

import java.util.ArrayList;
import java.util.List;

public class Restaurant {
    private List<Duration> durations = new ArrayList<>();
    private String name ;

    public List<Duration> getDurations() {
        return durations;
    }

    public void setDurations(List<Duration> durations) {
        this.durations = durations;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
