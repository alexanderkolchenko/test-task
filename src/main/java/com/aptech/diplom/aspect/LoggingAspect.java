package com.aptech.diplom.aspect;

public abstract class LoggingAspect<T> {

    protected String getTemplateStringForChanges(String changeField, String oldData, String newData) {
        return "change " + changeField + ": previous - " + oldData + ", new - " + newData + ", ";
    }
    public abstract void afterCreateEntity(T t);
    public abstract void afterRemoveEntity(T t);
}
