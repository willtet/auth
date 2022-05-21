package com.decode.auth.controllers;

import com.decode.auth.dtos.InstructorDto;
import com.decode.auth.enums.UserType;
import com.decode.auth.models.UserModel;
import com.decode.auth.services.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Log4j2
@RestController
@RequestMapping("/instructors")
@CrossOrigin(origins = "*", maxAge = 3600)
public class InstructorController {
    @Autowired
    UserService userService;

    @PostMapping("/subscription")
    public ResponseEntity<Object> saveSubscriptionInstructor(@RequestBody @Valid InstructorDto instructorDto){
        Optional<UserModel> userModelOptional = userService.findById(instructorDto.getUserId());
        if(!userModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }else{
            UserModel userModel = userModelOptional.get();
            userModel.setUserType(UserType.INSTRUCTOR);
            userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
            userService.save(userModel);
            return ResponseEntity.status(HttpStatus.OK).body(userModel);
        }
    }
}

