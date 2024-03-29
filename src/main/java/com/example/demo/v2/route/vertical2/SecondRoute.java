package com.example.demo.v2.route.vertical2;

import com.example.demo.v2.route.sashok_route.SashOkRouteBuilder;
import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

import static com.example.demo.v2.route.common.Constant.BACK_TO_MAIN_PROCESS;
import static com.example.demo.v2.route.common.Constant.KAFKA_PATH;
import static com.example.demo.v2.route.common.Constant.RECEIVER;

@Component
public class SecondRoute extends SashOkRouteBuilder {

    @Override
    public void buildRoute() {
        from("direct:routeSecond:s1")
                .setHeader(RECEIVER, constant("direct:routeSecond:s2"))
                .process("processor4")
                .to(KAFKA_PATH)
                .end();

        from("direct:routeSecond:s2")
                .setHeader(RECEIVER, constant("direct:routeSecond:s3"))
                .process("processor5")
                .to(KAFKA_PATH)
                .end();

        from("direct:routeSecond:s3")
                .process("processor6")
                .process(BACK_TO_MAIN_PROCESS)
                .choice()
                .when(Exchange::isRouteStop)
                .end()
                .to(KAFKA_PATH)
                .end();
    }
}
