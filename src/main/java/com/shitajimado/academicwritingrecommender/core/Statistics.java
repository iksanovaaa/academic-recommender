package com.shitajimado.academicwritingrecommender.core;

public class Statistics {
    private String name;
    private Long count;

    public Statistics(String name, Long count) {
        this.name = name;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public Long getCount() {
        return count;
    }
}
