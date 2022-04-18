package com.decode.auth.services;

import com.decode.auth.models.UserModel;
import com.decode.auth.specification.SpecificationTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    List<UserModel> findAll();

    Optional<UserModel> findById(UUID userId);


    void delete(UserModel userModelOptional);


    void save(UserModel userModel);

    boolean existByUsername(String username);

    boolean existByEmail(String email);

    Page<UserModel> findAll(Pageable pageable, Specification<UserModel> spec);
}
