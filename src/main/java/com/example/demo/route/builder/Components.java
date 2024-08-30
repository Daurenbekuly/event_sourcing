package com.example.demo.route.builder;

import com.example.demo.route.builder.step.IStepBuilder;
import com.example.demo.route.processor.AbstractSashokProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Components {

    private final ApplicationContext applicationContext;

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
        return applicationContext
                .getBeansOfType(IStepBuilder.class)
                .keySet()
                .stream()
                .toList();
    }

}
