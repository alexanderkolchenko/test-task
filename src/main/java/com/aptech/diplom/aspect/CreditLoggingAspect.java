package com.aptech.diplom.aspect;


import com.aptech.diplom.models.Credit;
import com.aptech.diplom.models.User;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;

@Slf4j
@Aspect
@ControllerAdvice
public class CreditLoggingAspect extends LoggingAspect<Credit> {

    @Override
    @AfterReturning(pointcut = "execution (public * com.aptech.diplom.service.CreditService.addCredit(..))", returning = "credit")
    public void afterCreateEntity(Credit credit) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info(user.getUsername() + ": " + "ADD Credit: " + credit.getCreditLimit() + ", " + credit.getInterestRate() + "%");
    }

    @AfterReturning(pointcut = "execution (public * com.aptech.diplom.service.CreditService.deleteCredit(..))", returning = "credit")
    public void afterRemoveEntity(Credit credit) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info(user.getUsername() + ": " + "DELETE Credit: id - " + credit.getId() + ", " + credit.getCreditLimit() + ", " + credit.getInterestRate());
    }

    @Around("execution (public * com.aptech.diplom.service.CreditService.updateCredit(..))")
    public Credit afterUpdateEntity(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
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

            StringBuilder sb = new StringBuilder();
            if (oldCreditLimit != newCredit.getCreditLimit()) {
                sb.append(getTemplateStringForChanges("credit limit", String.valueOf(oldCreditLimit), String.valueOf(newCredit.getCreditLimit())));
            }
            if (oldInterestRate != newCredit.getInterestRate()) {
                sb.append(getTemplateStringForChanges("interest rate", String.valueOf(oldInterestRate), String.valueOf(newCredit.getInterestRate())));
            }
            if (!sb.toString().equals("")) {
                log.info(user.getUsername() + ": " + "CHANGE Credit: id " + newCredit.getId() + ", " + sb.toString());
            }
        }
        return newCredit;
    }
}
