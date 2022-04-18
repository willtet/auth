package com.decode.auth.repositories;

import com.decode.auth.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;


public interface UserRepository extends JpaRepository<UserModel, UUID> , JpaSpecificationExecutor<UserModel> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);


}
