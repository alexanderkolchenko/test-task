package com.haulmont.testtask.aspect;

import com.haulmont.testtask.models.Bank;
import com.haulmont.testtask.models.Credit;
import com.haulmont.testtask.service.BankService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;


@Aspect
@ControllerAdvice
public class BankLoggingAspect extends LoggingAspect {

    @Autowired
    private BankService bankService;

    @AfterReturning(pointcut = "execution (public * com.haulmont.testtask.service.BankService.addBank(..))", returning = "bank")
    public void afterAddEntity(Bank bank) {
        logger.info("ADD Bank: " + bank.getNameOfBank());
    }

    @AfterReturning(pointcut = "execution (public * com.haulmont.testtask.service.BankService.deleteBank(..))", returning = "bank")
    public void afterRemoveEntity(Bank bank) {
        logger.info("DELETE Bank: id - " + bank.getId() + ", " + bank.getNameOfBank());
    }

    @Around("execution (public * com.haulmont.testtask.service.BankService.updateBank(..))")
    public Bank afterUpdateEntity(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        Object[] args = proceedingJoinPoint.getArgs();
        Bank oldBank = null;
        Bank newBank = null;

        for (Object o : args) {
            if (o instanceof Bank) {
                oldBank = (Bank) o;
            }
        }
        if (oldBank != null) {
            String oldName = oldBank.getNameOfBank();

            newBank = (Bank) proceedingJoinPoint.proceed();

            StringBuilder log = new StringBuilder();
            if (!oldName.equals(newBank.getNameOfBank())) {
                log.append(getTemplateString("name", oldName, newBank.getNameOfBank()));
            }
            if (bankService.getAddedCredits() != null && bankService.getAddedCredits().size() > 0) {
                log.append(" added credits - ");
                for (Credit credit : bankService.getAddedCredits()) {
                    log.append(credit.getCreditLimit())
                            .append(", ")
                            .append(credit.getInterestRate())
                            .append("%, ");
                }
                bankService.setAddedCredits(null);
            }
            if (bankService.getRemovedCredits() != null && bankService.getRemovedCredits().size() > 0) {
                log.append(" removed credits - ");
                for (Credit credit : bankService.getRemovedCredits()) {
                    log.append(credit.getCreditLimit())
                            .append(", ")
                            .append(credit.getInterestRate())
                            .append("%, ");
                }
                bankService.setRemovedCredits(null);
            }
            if (!log.toString().equals("")) {
                logger.info("CHANGE Bank: id " + newBank.getId() + ", " + log.toString());
            }
        }
        return newBank;
    }
}
