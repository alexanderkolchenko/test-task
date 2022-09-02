package com.haulmont.testtask.aspect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class LoggingAspect {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected String getTemplateString(String changeField, String oldData, String newData) {
        return "change " + changeField + ": previous - " + oldData + ", new - " + newData + ", ";
    }
}
