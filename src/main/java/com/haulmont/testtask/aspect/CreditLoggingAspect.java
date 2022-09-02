package com.haulmont.testtask.aspect;


import com.haulmont.testtask.models.Credit;
import com.haulmont.testtask.service.CreditOfferService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
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
                log.append("change credit limit: previous - ")
                        .append(oldCredit.getCreditLimit())
                        .append(", new - ")
                        .append(newCredit.getCreditLimit())
                        .append(", ");
            }
            if (oldInterestRate != newCredit.getInterestRate()) {
                log.append("change interest rate: previous - ")
                        .append(CreditOfferService.withMathRound(oldInterestRate, 2))
                        .append(", new - ")
                        .append(CreditOfferService.withMathRound(newCredit.getInterestRate(), 2))
                        .append(", ");
            }
            if (!log.toString().equals("")) {
                logger.info("CHANGE Credit: id " + newCredit.getId() + ", " + log.toString());
            }
        }
        return newCredit;
    }
}
