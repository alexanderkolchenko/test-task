package com.aptech.diplom.repository.auth;


import com.aptech.diplom.models.UserRole;
import com.aptech.diplom.models.UserRoles;
import org.springframework.data.repository.CrudRepository;

public interface UserRolesRepository extends CrudRepository<UserRole, Long> {
    UserRole findByRole(UserRoles role);
}