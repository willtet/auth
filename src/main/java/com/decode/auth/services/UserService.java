package com.decode.auth.services;

import com.decode.auth.models.UserModel;

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
}
