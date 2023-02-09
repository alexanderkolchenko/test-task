package com.aptech.diplom.aspect;

import com.aptech.diplom.models.CreditOffer;
import com.aptech.diplom.models.User;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;

@Slf4j
@Aspect
@ControllerAdvice
public class CreditOfferLoggingAspect extends LoggingAspect<CreditOffer>{

    @Override
    @AfterReturning(pointcut = "execution (public * com.aptech.diplom.service.CreditOfferService.applyCreditOffer(..))", returning = "creditOffer")
    public void afterCreateEntity(CreditOffer creditOffer) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info(user.getUsername() + ": " + "ADD Credit Offer:" +
                " customer - " + creditOffer.getCustomer().getName() + " " + creditOffer.getCustomer().getSurname() +
                ", bank - " + creditOffer.getBank().getName() +
                ", credit - " + creditOffer.getCredit().getInterestRate() + ", " + creditOffer.getCreditAmount());
    }

    @AfterReturning(pointcut = "execution (public * com.aptech.diplom.service.CreditOfferService.deleteCreditOffers(..))", returning = "creditOffer")
    public void afterRemoveEntity(CreditOffer creditOffer) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info(user.getUsername() + ": " + "DELETE Credit Offer: " +
                " customer - " + creditOffer.getCustomer().getName() + " " + creditOffer.getCustomer().getSurname() +
                ", bank - " + creditOffer.getBank().getName() +
                ", credit - " + creditOffer.getCreditAmount() + ", " + creditOffer.getCredit().getInterestRate() + " %");
    }
}
