package com.haulmont.testtask.aspect;


import com.haulmont.testtask.models.Credit;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import org.springframework.web.bind.annotation.ControllerAdvice;

@Aspect
@ControllerAdvice
public class CreditLoggingAspect extends LoggingAspect {

    @AfterReturning(pointcut = "execution (public * com.haulmont.testtask.service.CreditService.addCredit(..))", returning = "credit")
    public void afterAddEntity(Credit credit) {
        logger.info("ADD Credit: " + credit.getCreditLimit() + ", " + credit.getInterestRate() + "%");
    }

    @AfterReturning(pointcut = "execution (public * com.haulmont.testtask.service.CreditService.deleteCredit(..))", returning = "credit")
    public void afterRemoveEntity(Credit credit) {
        logger.info("DELETE Credit: id - " + credit.getId() + ", " + credit.getCreditLimit() + ", " + credit.getInterestRate());
    }

    @Around("execution (public * com.haulmont.testtask.service.CreditService.updateCredit(..))")
    public Credit afterUpdateEntity(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object[] args = proceedingJoinPoint.getArgs();
        Credit oldCredit = null;
        Credit newCredit = null;
        for (Object o : args) {
            if (o instanceof Credit) {
                oldCredit = (Credit) o;
            }
        }
        if (oldCredit != null) {
            int oldCreditLimit = oldCredit.getCreditLimit();
            float oldInterestRate = oldCredit.getInterestRate();

            newCredit = (Credit) proceedingJoinPoint.proceed();

            StringBuilder log = new StringBuilder();
            if (oldCreditLimit != newCredit.getCreditLimit()) {
                log.append(getTemplateString("credit limit", String.valueOf(oldCreditLimit), String.valueOf(newCredit.getCreditLimit())));
            }
            if (oldInterestRate != newCredit.getInterestRate()) {
                log.append(getTemplateString("interest rate", String.valueOf(oldInterestRate), String.valueOf(newCredit.getInterestRate())));
            }
            if (!log.toString().equals("")) {
                logger.info("CHANGE Credit: id " + newCredit.getId() + ", " + log.toString());
            }
        }
        return newCredit;
    }
}
