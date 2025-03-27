package com.example.demo.route.step;

import com.example.demo.common.CancelException;
import com.example.demo.common.ForbiddenException;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.demo.common.Constant.CANCEL_PROCESSOR;
import static com.example.demo.common.Constant.FORBIDDEN_PROCESSOR;
import static com.example.demo.common.Header.BACK_OFF_MULTIPLIER;
import static com.example.demo.common.Header.MAX_REDELIVERY;
import static com.example.demo.common.Header.REDELIVERY_DELAY;
import static org.apache.camel.LoggingLevel.ERROR;

public abstract class AbstractSashokStep extends RouteBuilder {

    protected String exceptionHandler;
    protected Integer maximumRedeliveries;
    protected Long redeliveryDelay;
    protected Double backOffMultiplier;
    protected Long executionTime;

    @Override
    public void configure() {
        onException(CancelException.class)
                .log(ERROR, "Handling error: ${exception.stacktrace}")
                .handled(true)
                .process(CANCEL_PROCESSOR)
                .end();

        onException(ForbiddenException.class)
                .log(ERROR, "Handling error: ${exception.stacktrace}")
                .handled(true)
                .process(FORBIDDEN_PROCESSOR)
                .end();

        onException(Exception.class)
                .process(this::fillHeader)
                .maximumRedeliveryDelay(Long.MAX_VALUE)
                .maximumRedeliveries(0)
                .redeliveryDelay(0L)
                .handled(true)
                .process(exceptionHandler)
                .end();

        declareStep();
    }

    public abstract void declareStep();

    private void fillHeader(Exchange exchange) {
        exchange.getIn().setHeader(MAX_REDELIVERY, maximumRedeliveries);
        exchange.getIn().setHeader(REDELIVERY_DELAY, redeliveryDelay);
        exchange.getIn().setHeader(BACK_OFF_MULTIPLIER, backOffMultiplier);
    }

    public static void nameValidator(String name) {
        String pattern = "direct:r:.+?:s:.+?:v:.+?";
        Pattern regexPattern = Pattern.compile(pattern);
        Matcher matcher = regexPattern.matcher(name);
        if (!matcher.matches())
            throw new RuntimeException("Name " + name + "is not valid, validator patter: " + pattern);
    }
}
