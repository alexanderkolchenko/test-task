package com.haulmont.testtask.aspect;

import com.haulmont.testtask.models.Bank;
import com.haulmont.testtask.models.Credit;
import com.haulmont.testtask.service.BankService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;


@Aspect
@ControllerAdvice
public class BankLoggingAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

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

    @AfterReturning(pointcut = "execution (public * com.haulmont.testtask.service.BankService.updateBank(..))", returning = "bank")
    public void afterUpdateEntity(JoinPoint joinPoint, Bank bank) {
        Object[] args = joinPoint.getArgs();
        Bank oldBank = null;
        for (Object o : args) {
            if (o instanceof Bank) {
                oldBank = (Bank) o;
            }
        }
        if (oldBank != null) {
            StringBuilder log = new StringBuilder();
            if (!oldBank.getNameOfBank().equals(bank.getNameOfBank())) {
                log.append("change name: previous - ")
                        .append(oldBank.getNameOfBank())
                        .append(", new - ")
                        .append(bank.getNameOfBank())
                        .append(", ");
            }
            if (bankService.getAddedCredits() != null && bankService.getAddedCredits().size() > 0) {
                log.append(" added credits - ");
                for (Credit credit : bankService.getAddedCredits()) {
                    log.append(" credit limit: ")
                            .append(credit.getCreditLimit())
                            .append(", ");
                }

                bankService.setAddedCredits(null);
            }
            if (bankService.getRemovedCredits() != null && bankService.getRemovedCredits().size() > 0) {
                log.append(" removed credits - ");
                for (Credit credit : bankService.getRemovedCredits()) {
                    log.append(" credit limit: ")
                            .append(credit.getCreditLimit())
                            .append(", ");
                }
                bankService.setRemovedCredits(null);
            }
            if (!log.toString().equals("")) {
                logger.info("CHANGE Bank: id " + bank.getId() + ", " + log.toString());
            }
        }
    }
}
