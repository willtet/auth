package com.decode.auth.controllers;


import com.decode.auth.dtos.UserDto;
import com.decode.auth.enums.UserStatus;
import com.decode.auth.enums.UserType;
import com.decode.auth.models.UserModel;
import com.decode.auth.services.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.util.BeanUtil;
import org.apache.catalina.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/auth")
public class AuthenticationController {
    @Autowired
    UserService service;

    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(@RequestBody
                                                @Validated(UserDto.UserView.ResgistrationPost.class)
                                                @JsonView(UserDto.UserView.ResgistrationPost.class) UserDto dto){
        if(service.existByUsername(dto.getUsername())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("error: Username is already taken.");
        }

        if(service.existByEmail(dto.getEmail())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("error: Email is already taken.");
        }

        var userModel = new UserModel();
        BeanUtils.copyProperties(dto,userModel);
        userModel.setUserStatus(UserStatus.ACTIVE);
        userModel.setUserType(UserType.STUDENT);
        userModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        service.save(userModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(userModel);
    }
}
