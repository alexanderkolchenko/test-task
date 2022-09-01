package com.haulmont.testtask.aspect;

import com.haulmont.testtask.models.Customer;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;

@Aspect
@ControllerAdvice
public class CustomerLoggingAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @AfterReturning(pointcut = "execution (public * com.haulmont.testtask.service.CustomerService.addCustomer(..))", returning = "customer")
    public void afterAddEntity(Customer customer) {
        logger.info("ADD Customer: " + customer.toString());
    }

    public void afterRemoveEntity() {

    }

    public void afterUpdateEntity() {

    }
}
