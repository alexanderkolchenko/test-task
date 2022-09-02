package com.haulmont.testtask.aspect;

import com.haulmont.testtask.models.Customer;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.web.bind.annotation.ControllerAdvice;

@Aspect
@ControllerAdvice
public class CustomerLoggingAspect extends LoggingAspect {

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
                log.append(getTemplateString("name", oldName, newCustomer.getName()));
            }

            if (!oldSurname.equals(newCustomer.getSurname())) {
                log.append(getTemplateString("surname", oldSurname, newCustomer.getSurname()));
            }

            if (!oldPatronymic.equals(newCustomer.getPatronymic())) {
                log.append(getTemplateString("patronymic", oldPatronymic, newCustomer.getPatronymic()));
            }

            if (!oldEmail.equals(newCustomer.getEmail())) {
                log.append(getTemplateString("email", oldEmail, newCustomer.getEmail()));
            }

            if (!oldPassportNumber.equals(newCustomer.getPassportNumber())) {
                log.append(getTemplateString("passport number", oldPassportNumber, newCustomer.getPassportNumber()));
            }

            if (!oldPhone.equals(newCustomer.getPhoneNumber())) {
                log.append(getTemplateString("phone number", oldPhone, newCustomer.getPhoneNumber()));
            }

            if (!log.toString().equals("")) {
                logger.info("CHANGE Customer: id " + newCustomer.getId() + ", " + log.toString());
            }
        }
        return newCustomer;
    }
}
