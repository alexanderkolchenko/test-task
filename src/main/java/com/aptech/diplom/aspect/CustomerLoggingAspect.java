package com.aptech.diplom.aspect;

import com.aptech.diplom.models.Customer;
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
public class CustomerLoggingAspect extends LoggingAspect<Customer> {

    @AfterReturning(pointcut = "execution (public * com.aptech.diplom.service.CustomerService.addCustomer(..))", returning = "customer")
    public void afterCreateEntity(Customer customer) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info(user.getUsername() + ": " + "ADD Customer: " + customer.toString());
    }


    @AfterReturning(pointcut = "execution (public * com.aptech.diplom.service.CustomerService.deleteCustomer(..))", returning = "customer")
    public void afterRemoveEntity(Customer customer) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info(user.getUsername() + ": " + "DELETE Customer: id - " + customer.getId() + ", " + customer.toString());
    }

    @Around("execution (public * com.aptech.diplom.service.CustomerService.updateCustomer(..))")
    public Customer afterUpdateEntity(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
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
            StringBuilder sb = new StringBuilder();

            if (!oldName.equals(newCustomer.getName())) {
                sb.append(getTemplateStringForChanges("name", oldName, newCustomer.getName()));
            }

            if (!oldSurname.equals(newCustomer.getSurname())) {
                sb.append(getTemplateStringForChanges("surname", oldSurname, newCustomer.getSurname()));
            }

            if (!oldPatronymic.equals(newCustomer.getPatronymic())) {
                sb.append(getTemplateStringForChanges("patronymic", oldPatronymic, newCustomer.getPatronymic()));
            }

            if (!oldEmail.equals(newCustomer.getEmail())) {
                sb.append(getTemplateStringForChanges("email", oldEmail, newCustomer.getEmail()));
            }

            if (!oldPassportNumber.equals(newCustomer.getPassportNumber())) {
                sb.append(getTemplateStringForChanges("passport number", oldPassportNumber, newCustomer.getPassportNumber()));
            }

            if (!oldPhone.equals(newCustomer.getPhoneNumber())) {
                sb.append(getTemplateStringForChanges("phone number", oldPhone, newCustomer.getPhoneNumber()));
            }

            if (!sb.toString().equals("")) {
                log.info(user.getUsername() + ": " + "CHANGE Customer: id " + newCustomer.getId() + ", " + sb.toString());
            }
        }
        return newCustomer;
    }
}
