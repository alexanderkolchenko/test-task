package com.haulmont.testtask.aspect;

public abstract class LoggingAspect {
    protected String getTemplateStringForChanges(String changeField, String oldData, String newData) {
        return "change " + changeField + ": previous - " + oldData + ", new - " + newData + ", ";
    }
}
