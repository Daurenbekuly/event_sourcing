//package com.example.demo.castom_bean_config;
//
//import com.example.demo.v2.route.StepX;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.annotation.AnnotationUtils;
//
//@Configuration
//@ComponentScan(basePackages = "com.example.camel_demo.route")
//public class MyConfig {
//
//    private final ApplicationContext applicationContext;
//
//    public MyConfig(ApplicationContext applicationContext) {
//        this.applicationContext = applicationContext;
//    }
//
//    @Bean
//    public Object configureBeans() {
//        String[] beanNames = applicationContext.getBeanNamesForAnnotation(Step.class);
//        for (String beanName : beanNames) {
//            Object bean = applicationContext.getBean(beanName);
//            if (bean instanceof StepX step) {
//                Class<? extends StepX> clazz = step.getClass();
//                Step annotation = AnnotationUtils.findAnnotation(clazz, Step.class);
//                if (annotation == null) return bean;
//                step.setFrom(annotation.value());
//                step.setTo(annotation.receiverName());
//                step.setExceptionHandler(annotation.exceptionHandler());
//                step.setExceptionMaxRedeliveries(annotation.exceptionMaxRedeliveries());
//                step.setExceptionBackOffMultiplier(annotation.exceptionBackOffMultiplier());
//            }
//            return bean;
//        }
//        return new Object();
//    }
//}
