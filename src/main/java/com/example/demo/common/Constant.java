package com.example.demo.common;

public interface Constant {

    String MAIN_ROUTE_RECEIVER = "mainRouteReceiver";
    String RECEIVER = "receiver";
    String RECEIVERS = "receivers";
    String INIT_SUB_ROUTE_PROCESSOR = "initSubRouteProcessor";
    String BACK_TO_MAIN_ROUTE_PROCESSOR = "backToMainProcessor";
    String FIRST_STEP_PROCESSOR = "firstStepProcessor";
    String LAST_STEP_PROCESSOR = "lastStepProcessor";
    String TIMEOUT = "timeout";

    //EXCEPTION
    String DEFAULT_EXCEPTION_HANDLER_PROCESSOR = "exceptionHandlerProcessor";
    Integer DEFAULT_MAX_REDELIVERIES = 5;
    Double DEFAULT_EXCEPTION_BACK_OFF_MULTI = 1.5;
    Long DEFAULT_REDELIVERY_DELAY = 5000L;

    //EXECUTION
    Long DEFAULT_EXECUTION_TIME_TO_WAIT = 15000L;
}
