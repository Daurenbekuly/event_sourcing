package com.example.demo.v2.route.vertical1;

import com.example.demo.v2.route.sashok_route.SashOkRouteBuilder;
import org.springframework.stereotype.Component;

import static com.example.demo.v2.route.common.Constant.CORRELATION;
import static com.example.demo.v2.route.common.Constant.INIT_SUB_PROCESS;
import static com.example.demo.v2.route.common.Constant.KAFKA_PATH;
import static com.example.demo.v2.route.common.Constant.RECEIVER;

@Component
public class FirstRoute extends SashOkRouteBuilder {

    @Override
    public void buildRoute() {

        from("direct:routeFirst:s1")
                .setHeader(RECEIVER, constant("direct:routeFirst:s2"))
                .process("processor1")
                .to(KAFKA_PATH)
                .end();

        from("direct:routeFirst:s2")
                .doTry()
                    .setHeader(RECEIVER, constant("direct:routeSecond:s1"))
                    .process("processor2")
                    .setHeader(CORRELATION, constant("direct:routeFirst:s3"))
                    .process(INIT_SUB_PROCESS)
                    .to(KAFKA_PATH)
                .doCatch(RuntimeException.class)
                    .log("Handling error: ${exception.stacktrace}")
                    .to("direct:routeFirst:s2:eh")
                .end();

        from("direct:routeFirst:s3")
                .process("processor3")
                .end();
    }


}
