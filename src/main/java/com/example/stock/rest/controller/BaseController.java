package com.example.stock.rest.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseController {
    private static final Logger logger = LoggerFactory.getLogger(BaseController.class);

    protected boolean validateRequest() {
        logger.info("method=validateRequest; message=Start Validation;");
        return true;
    }
}
