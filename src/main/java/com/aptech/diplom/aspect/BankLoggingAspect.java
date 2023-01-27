package com.aptech.diplom.aspect;

import com.aptech.diplom.models.Bank;
import com.aptech.diplom.models.Credit;
import com.aptech.diplom.service.BankService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;

@Slf4j
@Aspect
@ControllerAdvice
public class BankLoggingAspect extends LoggingAspect {

    @Autowired
    private BankService bankService;

    @AfterReturning(pointcut = "execution (public * com.aptech.diplom.service.BankService.addBank(..))", returning = "bank")
    public void afterAddEntity(Bank bank) {
        log.info("ADD Bank: " + bank.getNameOfBank());
    }

    @AfterReturning(pointcut = "execution (public * com.aptech.diplom.service.BankService.deleteBank(..))", returning = "bank")
    public void afterRemoveEntity(Bank bank) {
        log.info("DELETE Bank: id - " + bank.getId() + ", " + bank.getNameOfBank());
    }

    @Around("execution (public * com.aptech.diplom.service.BankService.updateBank(..))")
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

            StringBuilder sb = new StringBuilder();
            if (!oldName.equals(newBank.getNameOfBank())) {
                sb.append(getTemplateStringForChanges("name", oldName, newBank.getNameOfBank()));
            }
            if (bankService.getAddedCredits() != null && bankService.getAddedCredits().size() > 0) {
                sb.append(" added credits - ");
                for (Credit credit : bankService.getAddedCredits()) {
                    sb.append(credit.getCreditLimit())
                            .append(", ")
                            .append(credit.getInterestRate())
                            .append("%, ");
                }
                bankService.setAddedCredits(null);
            }
            if (bankService.getRemovedCredits() != null && bankService.getRemovedCredits().size() > 0) {
                sb.append(" removed credits - ");
                for (Credit credit : bankService.getRemovedCredits()) {
                    sb.append(credit.getCreditLimit())
                            .append(", ")
                            .append(credit.getInterestRate())
                            .append("%, ");
                }
                bankService.setRemovedCredits(null);
            }
            if (!sb.toString().equals("")) {
                log.info("CHANGE Bank: id " + newBank.getId() + ", " + sb.toString());
            }
        }
        return newBank;
    }
}
