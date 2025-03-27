package com.example.demo.common;

public interface Constant {

    Integer MAX_REDELIVERY = 3;
    Double BACKOFF_MULTIPLIER = 2.0;
    Long REDELIVERY_DELAY = 5000L;
    Long EXECUTION_TIME = 5000L;

    String INIT_SUB_ROUTE_PROCESSOR = "initSubRouteProcessor";
    String BACK_TO_MAIN_ROUTE_PROCESSOR = "backToMainProcessor";
    String FIRST_STEP_PROCESSOR = "firstStepProcessor";
    String LAST_STEP_PROCESSOR = "lastStepProcessor";
    String EXCEPTION_HANDLER_PROCESSOR = "exceptionHandlerProcessor";
    String CANCEL_PROCESSOR = "cancelProcessor";
    String FORBIDDEN_PROCESSOR = "forbiddenProcessor";
    String BEFORE_USER_TASK_PROCESSOR = "beforeUserTaskProcessor";
}
