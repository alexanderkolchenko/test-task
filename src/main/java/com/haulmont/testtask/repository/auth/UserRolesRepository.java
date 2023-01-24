package com.haulmont.testtask.repository.auth;


import com.haulmont.testtask.models.UserRole;
import com.haulmont.testtask.models.UserRoles;
import org.springframework.data.repository.CrudRepository;

public interface UserRolesRepository extends CrudRepository<UserRole, Long> {
    UserRole findByRole(UserRoles role);
}