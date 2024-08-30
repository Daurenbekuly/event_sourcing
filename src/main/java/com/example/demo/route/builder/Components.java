package com.example.demo.route.builder;

import com.example.demo.route.processor.AbstractSashokProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Components {

    private final ApplicationContext applicationContext;

    @Value("${app.steps}")
    private List<String> steps;

    public Components(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public List<String> getAllProcessors() {
        return applicationContext
                .getBeansOfType(AbstractSashokProcessor.class)
                .keySet()
                .stream()
                .toList();
    }

    public List<String> getAllSteps() {
        return steps;
    }

}
