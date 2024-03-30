//package com.example.demo.route;
//
//import com.example.demo.entity.StepEntity;
//import com.example.demo.model.Step;
//import com.example.demo.repository.SashokJdbcRepo;
//import com.example.demo.repository.StepRepository;
//import com.example.demo.util.JsonUtil;
//import org.apache.camel.Exchange;
//import org.apache.camel.builder.RouteBuilder;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//
//import java.time.Instant;
//import java.util.Map;
//import java.util.Optional;
//import java.util.UUID;
//
//import static org.apache.camel.Exchange.REDELIVERY_COUNTER;
//import static org.apache.camel.Exchange.REDELIVERY_MAX_COUNTER;
//
//public abstract class StepX extends RouteBuilder {
//
//    private String from;
//    private String to;
//    private String exceptionHandler;
//    private Integer exceptionMaxRedeliveries;
//    private Integer exceptionBackOffMultiplier;
//    private String kafkaPath;
//    private Integer defaultExceptionRedeliveryDelay = 200;
//    private Integer defaultExceptionMaximumRedeliveries = 5;
//
//    @Autowired
//    private StepRepository stepRepository;
//
//    @Autowired
//    private SashokJdbcRepo jdbcRepo;
//
//    protected StepX(String from) {
//        this.from = from;
//        this.to = "";
//        this.exceptionHandler = "direct:def";
//        this.exceptionMaxRedeliveries = 5;
//        this.exceptionBackOffMultiplier = 1;
//    }
//
//    protected StepX(String from, String to) {
//        this.from = from;
//        this.to = to;
//        this.exceptionHandler = "direct:def";
//        this.exceptionMaxRedeliveries = 5;
//        this.exceptionBackOffMultiplier = 1;
//    }
//
//    protected StepX(String from, String to, String exceptionHandler) {
//        this.from = from;
//        this.to = to;
//        this.exceptionHandler = exceptionHandler;
//        this.exceptionMaxRedeliveries = 5;
//        this.exceptionBackOffMultiplier = 1;
//    }
//
//    protected StepX(String from,
//                    String to,
//                    String exceptionHandler,
//                    Integer exceptionMaxRedeliveries,
//                    Integer exceptionBackOffMultiplier) {
//        this.from = from;
//        this.to = to;
//        this.exceptionHandler = exceptionHandler;
//        this.exceptionMaxRedeliveries = exceptionMaxRedeliveries;
//        this.exceptionBackOffMultiplier = exceptionBackOffMultiplier;
//    }
//
//    public void setFrom(String from) {
//        this.from = from;
//    }
//
//    public void setTo(String to) {
//        this.to = to;
//    }
//
//    public void setExceptionHandler(String exceptionHandler) {
//        this.exceptionHandler = exceptionHandler;
//    }
//
//    public void setExceptionMaxRedeliveries(Integer exceptionMaxRedeliveries) {
//        this.exceptionMaxRedeliveries = exceptionMaxRedeliveries;
//    }
//
//    public void setExceptionBackOffMultiplier(Integer exceptionBackOffMultiplier) {
//        this.exceptionBackOffMultiplier = exceptionBackOffMultiplier;
//    }
//
//    @Value("${app.s.kafka-path}")
//    public void setKafkaPath(String kafkaPath) {
//        this.kafkaPath = kafkaPath;
//    }
//
//    @Value("${app.s.def.exception-max-re-count}")
//    public void setDefaultExceptionMaximumRedeliveries(Integer defaultExceptionMaximumRedeliveries) {
//        this.defaultExceptionMaximumRedeliveries = defaultExceptionMaximumRedeliveries;
//    }
//
//    @Value("${app.s.def.exception-re-delay}")
//    public void setDefaultExceptionRedeliveryDelay(Integer defaultExceptionRedeliveryDelay) {
//        this.defaultExceptionRedeliveryDelay = defaultExceptionRedeliveryDelay;
//    }
//
//    @Override
//    public void configure() throws Exception {
//        errorHandler(
//                defaultErrorHandler()
//                        .maximumRedeliveries(defaultExceptionMaximumRedeliveries)
//                        .redeliveryDelay(defaultExceptionRedeliveryDelay)
//                        .logRetryAttempted(true)
//        );
//
//        onException(RuntimeException.class)
//                .maximumRedeliveries(exceptionMaxRedeliveries)
//                .useExponentialBackOff()
//                .backOffMultiplier(exceptionBackOffMultiplier)
//                .onRedelivery(this::reduceRetryCount)
//                .logRetryAttempted(true)
//                .handled(true)
//                .log("Message Exhausted after " + exceptionMaxRedeliveries + " retries...")
//                .to(exceptionHandler)
//                .end();
//
//        from(from)
//                .process(this::processing)
//                .choice()
//                .when(Exchange::isRouteStop)
//                .end()
//                .to(kafkaPath)
//                .end();
//    }
//
//    private void reduceRetryCount(Exchange exchange) {
//        Integer current = exchange.getIn().getHeader(REDELIVERY_COUNTER, Integer.class);
//        Integer max = exchange.getIn().getHeader(REDELIVERY_MAX_COUNTER, Integer.class);
//        Integer availableTryCount = max - current;
//        String body = exchange.getIn().getBody().toString();
//        Optional<Step> optEvent = JsonUtil.toObject(body, Step.class);
//        if (optEvent.isPresent()) {
//            Step step = optEvent.get();
//            Map<String, String> road = step.road();
//            String exchangeId = exchange.getExchangeId();
//            UUID uuid = UUID.nameUUIDFromBytes(exchangeId.getBytes());
//            Step newStep = new Step(from, to, step.jsonValue(), Instant.now(), availableTryCount, road);
//            StepEntity stepEntity = new StepEntity(uuid, newStep, availableTryCount);
//            StepEntity saved = stepRepository.save(stepEntity);
//            road.put(saved.getStepId().toString(), from);
//            Optional<String> json = JsonUtil.toJson(newStep);
//            exchange.getIn().setBody(json.get());
//        }
//    }
//
//    public void processing(Exchange exchange) {
//        String body = exchange.getIn().getBody().toString();
//        Optional<Step> optEvent = JsonUtil.toObject(body, Step.class);
//        Step newStep = null;
//        if (optEvent.isPresent()) {
//            Step step = optEvent.get();
//            Map<String, String> road = step.road();
//            String jsonValue = invoke(step.jsonValue());
//            newStep = new Step(from, to, jsonValue, Instant.now(), exceptionMaxRedeliveries, road);
//            StepEntity stepEntity = new StepEntity(newStep);
//            StepEntity saved = stepRepository.save(stepEntity);
//            road.put(saved.getStepId().toString(), from);
//            Optional<String> json = JsonUtil.toJson(newStep);
//            exchange.getIn().setBody(json.get());
//        } else {
//            throw new RuntimeException("WTF");
//        }
//        if (to.isEmpty()) {
//            jdbcRepo.create(newStep);
//            exchange.setRouteStop(true);
//        }
//    }
//
//    public abstract String invoke(String jsonValue);
//}
