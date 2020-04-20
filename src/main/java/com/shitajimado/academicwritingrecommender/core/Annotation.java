package com.shitajimado.academicwritingrecommender.core;

public class Annotation {
    private Long id;
    private String name;
    private String type;
    private Long startNode;
    private Long endNode;

    public Annotation(Long id, String name, String type, Long startNode, Long endNode) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.startNode = startNode;
        this.endNode = endNode;
    }

    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public Long getStartNode() {
        return startNode;
    }

    public Long getEndNode() {
        return endNode;
    }

    public String getName() {
        return name;
    }
}
