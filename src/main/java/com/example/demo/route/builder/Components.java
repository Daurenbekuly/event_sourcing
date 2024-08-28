package com.example.demo.route.builder;

import com.example.demo.route.processor.AbstractSashokProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class Components {

    private final ApplicationContext applicationContext;
    private final List<Steps> STEPS = Arrays.stream(Steps.values()).toList();

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

    public List<Steps> getAllSteps() {
        return STEPS;
    }

    public enum Steps {
        FIRST_STEP,
        STEP,
        STEP_SP,
        LAST_STEP,
        LAST_STEP_BM,
        STEP_BEFORE_UT,
        STEP_AFTER_UT
    }

}
