package com.haulmont.testtask.aspect;

import com.haulmont.testtask.models.Customer;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
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

    @AfterReturning(pointcut = "execution (public * com.haulmont.testtask.service.CustomerService.deleteCustomer(..))", returning = "customer")
    public void afterRemoveEntity(Customer customer) {
        logger.info("DELETE Customer: id - " + customer.getId() + ", " + customer.toString());
    }

    @Around("execution (public * com.haulmont.testtask.service.CustomerService.updateCustomer(..))")
    public Customer afterUpdateEntity(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        Object[] args = proceedingJoinPoint.getArgs();
        Customer oldCustomer = null;
        Customer newCustomer = null;

        for (Object o : args) {
            if (o instanceof Customer) {
                oldCustomer = (Customer) o;
            }
        }
        if (oldCustomer != null) {
            String oldName = oldCustomer.getName();
            String oldSurname = oldCustomer.getSurname();
            String oldPatronymic = oldCustomer.getPatronymic();
            String oldEmail = oldCustomer.getEmail();
            String oldPhone = oldCustomer.getPhoneNumber();
            String oldPassportNumber = oldCustomer.getPassportNumber();
            newCustomer = (Customer) proceedingJoinPoint.proceed();
            StringBuilder log = new StringBuilder();

            if (!oldName.equals(newCustomer.getName())) {
                log.append("change name: previous - ")
                        .append(oldCustomer.getName())
                        .append(", new - ")
                        .append(newCustomer.getName())
                        .append(", ");
            }

            if (!oldSurname.equals(newCustomer.getSurname())) {
                log.append("change surname: previous - ")
                        .append(oldCustomer.getSurname())
                        .append(", new - ")
                        .append(newCustomer.getSurname())
                        .append(", ");
            }

            if (!oldPatronymic.equals(newCustomer.getPatronymic())) {
                log.append("change patronymic: previous - ")
                        .append(oldCustomer.getPatronymic())
                        .append(", new - ")
                        .append(newCustomer.getPatronymic())
                        .append(", ");
            }

            if (!oldEmail.equals(newCustomer.getEmail())) {
                log.append("change email: previous - ")
                        .append(oldCustomer.getEmail())
                        .append(", new - ")
                        .append(newCustomer.getEmail())
                        .append(", ");
            }

            if (!oldPassportNumber.equals(newCustomer.getPassportNumber())) {
                log.append("change passport number: previous - ")
                        .append(oldCustomer.getPassportNumber())
                        .append(", new - ")
                        .append(newCustomer.getPassportNumber())
                        .append(", ");
            }

            if (!oldPhone.equals(newCustomer.getPhoneNumber())) {
                log.append("change phone number: previous - ")
                        .append(oldCustomer.getPhoneNumber())
                        .append(", new - ")
                        .append(newCustomer.getPhoneNumber())
                        .append(", ");
            }

            if (!log.toString().equals("")) {
                logger.info("CHANGE Customer: id " + newCustomer.getId() + ", " + log.toString());
            }
        }
        return newCustomer;
    }
}
