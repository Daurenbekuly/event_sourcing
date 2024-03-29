package com.example.demo.v2.route.vertical1;

import com.example.demo.v2.route.sashok_route.SashOkRouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class FirstRouteExceptionHandler extends SashOkRouteBuilder {

    @Override
    public void buildRoute() {

        from("direct:routeFirst:s2:eh")
                .throwException(new RuntimeException("${exception.message}"))
                .onException(RuntimeException.class)
                    .maximumRedeliveries(7)
                    .redeliveryDelay(5000)
                    .useExponentialBackOff()
                    .backOffMultiplier(exceptionBackOffMultiplier)
                    .onRedelivery(this::reduceRetryCount)
                    .logRetryAttempted(true)
                    .handled(true)
                    .log("Message Exhausted after " + maximumRedeliveries + " retries...")
                    .to(exceptionHandler)
                .end();
    }


}
