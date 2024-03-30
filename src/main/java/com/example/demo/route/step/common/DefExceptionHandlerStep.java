package com.example.demo.route.step.common;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Service;

@Service
public class DefExceptionHandlerStep extends RouteBuilder {

    @Override
    public void configure() {
        errorHandler(
                defaultErrorHandler()
                        .maximumRedeliveries(0)
        );

        from("direct:defErrorHandler")
                .log("Handling error: ${exception}")
                .end();
    }

}
