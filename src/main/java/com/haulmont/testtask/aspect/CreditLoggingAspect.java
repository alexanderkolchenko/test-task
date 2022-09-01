package com.haulmont.testtask.aspect;

import com.haulmont.testtask.models.Credit;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;

@Aspect
@ControllerAdvice
public class CreditLoggingAspect {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @AfterReturning(pointcut = "execution (public * com.haulmont.testtask.service.CreditService.addCredit(..))", returning = "credit")
    public void afterAddEntity(Credit credit) {
        logger.info("ADD Credit: " + credit.toString());
    }

    public void afterRemoveEntity() {

    }

    public void afterUpdateEntity() {

    }
}
