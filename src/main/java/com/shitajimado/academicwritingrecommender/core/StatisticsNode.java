package com.shitajimado.academicwritingrecommender.core;

public class StatisticsNode {
    private String name;
    private Long count;

    public StatisticsNode(String name, Long count) {
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
