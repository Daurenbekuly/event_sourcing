package com.example.demo.v2.route.sashok_route;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StepDefExceptionHandler extends RouteBuilder {

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
