package com.haulmont.testtask.aspect;

import com.haulmont.testtask.models.CreditOffer;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;

@Aspect
@ControllerAdvice
public class CreditOfferLoggingAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @AfterReturning(pointcut = "execution (public * com.haulmont.testtask.service.CreditOfferService.applyCreditOffer(..))", returning = "creditOffer")
    public void afterAddEntity(CreditOffer creditOffer) {
        logger.info("ADD Credit Offer:" +
                " customer - " + creditOffer.getCustomer().getName() + " " + creditOffer.getCustomer().getSurname() +
                ", bank - " + creditOffer.getBank().getNameOfBank() +
                ", credit - " + creditOffer.getCredit().getInterestRate() + ", " + creditOffer.getCreditAmount());
    }

    @AfterReturning(pointcut = "execution (public * com.haulmont.testtask.service.CreditOfferService.deleteCreditOffers(..))", returning = "creditOffer")
    public void afterRemoveEntity(CreditOffer creditOffer) {
        logger.info("DELETE Credit Offer: " +
                " customer - " + creditOffer.getCustomer().getName() + " " + creditOffer.getCustomer().getSurname() +
                ", bank - " + creditOffer.getBank().getNameOfBank() +
                ", credit - " + creditOffer.getCredit().getInterestRate() + ", " + creditOffer.getCreditAmount());
    }
}
