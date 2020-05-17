package com.shitajimado.academicwritingrecommender.core;

import com.shitajimado.academicwritingrecommender.entities.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Statistics {
    private List<StatisticsNode> annotations;

    public Statistics(List<StatisticsNode> annotations) {
        this.annotations = annotations;
    }

    public static Statistics fromTextStream(Stream<Text> texts) {
        Map<String, Long> counts = new HashMap<>();

        texts.map(Text::getAnnotationList).forEach(
                annotationList -> {
                    for (var annotation : annotationList) {
                        counts.putIfAbsent(annotation.getName(), 1L);
                        counts.computeIfPresent(annotation.getName(), (key, value) -> value + 1);
                    }
                }
        );

        List<StatisticsNode> nodes = new ArrayList<>();

        for (var name : counts.keySet()) {
            nodes.add(new StatisticsNode(name, counts.get(name)));
        }

        return new Statistics(nodes);
    }

    public List<StatisticsNode> getAnnotations() {
        return annotations;
    }
}
