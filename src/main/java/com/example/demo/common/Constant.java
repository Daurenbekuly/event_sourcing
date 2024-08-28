package com.example.demo.common;

public interface Constant {

    Integer MAXIMUM_REDELIVERIES = 5;
    Double EXCEPTION_BACKOFF_MULTIPLIER = 2.0;
    Long REDELIVERY_DELAY = 5000L;
    Long EXECUTION_TIME_TO_WAIT = 5000L;

    String MAIN_ROUTE_RECEIVER = "mainRouteReceiver";
    String RECEIVER = "receiver";
    String RECEIVERS = "receivers";
    String TIMEOUT = "timeout";

    String INIT_SUB_ROUTE_PROCESSOR = "initSubRouteProcessor";
    String BACK_TO_MAIN_ROUTE_PROCESSOR = "backToMainProcessor";
    String FIRST_STEP_PROCESSOR = "firstStepProcessor";
    String LAST_STEP_PROCESSOR = "lastStepProcessor";
    String EXCEPTION_HANDLER_PROCESSOR = "exceptionHandlerProcessor";
    String BEFORE_USER_TASK_PROCESSOR = "beforeUserTaskProcessor";
}
