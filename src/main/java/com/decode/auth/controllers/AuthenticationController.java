package com.decode.auth.controllers;


import com.decode.auth.dtos.UserDto;
import com.decode.auth.enums.UserStatus;
import com.decode.auth.enums.UserType;
import com.decode.auth.models.UserModel;
import com.decode.auth.services.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.util.BeanUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.catalina.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Log4j2
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
        log.debug("POST registerUser userDto received {}", dto.toString());
        if(service.existByUsername(dto.getUsername())){
            log.warn("Username is already taken {}", dto.getUsername());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("error: Username is already taken.");
        }

        if(service.existByEmail(dto.getEmail())){
            log.warn("Email is already taken {}", dto.getEmail());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("error: Email is already taken.");
        }

        var userModel = new UserModel();
        BeanUtils.copyProperties(dto,userModel);
        userModel.setUserStatus(UserStatus.ACTIVE);
        userModel.setUserType(UserType.STUDENT);
        userModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        service.save(userModel);
        log.debug("POST registerUser userDto saved {}", userModel.toString());
        log.info("User saved successfully userId {}", userModel.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(userModel);
    }
}
