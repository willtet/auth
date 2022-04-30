package com.decode.auth.controllers;

import com.decode.auth.dtos.UserDto;
import com.decode.auth.models.UserModel;
import com.decode.auth.services.UserService;
import com.decode.auth.specification.SpecificationTemplate;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.extern.log4j.Log4j2;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService service;

    @GetMapping()
    public ResponseEntity<Page<UserModel>> getAllUsers(SpecificationTemplate.UserSpec spec,
                                                        @PageableDefault(page = 0, size = 10, sort = "userId", direction = Sort.Direction.ASC)
                                                        Pageable pageable){
        Page<UserModel> userModelPage = service.findAll(pageable, spec);
        if (!userModelPage.isEmpty()){
            for (UserModel model : userModelPage.toList()) {
                model.add(linkTo(methodOn(UserController.class).getOneUser(model.getUserId())).withSelfRel());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(userModelPage);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getOneUser(@PathVariable(value = "userId") UUID userId){
        Optional<UserModel> userModelOptional = service.findById(userId);
        if(!userModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }else{
            return ResponseEntity.status(HttpStatus.OK).body(userModelOptional.get());
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable(value = "userId") UUID userId){
        log.debug("DELETE deleteUser userId received {}", userId);
        Optional<UserModel> userModelOptional = service.findById(userId);
        if(!userModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }else{
            service.delete(userModelOptional.get());
            log.debug("DELETE deleteUser userId saved {}", userId);
            log.info("User deleted successfully userId {}", userId);
            return ResponseEntity.status(HttpStatus.OK).body("Deleted successfuly");
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable(value = "userId") UUID userId,
                                             @RequestBody @Validated(UserDto.UserView.UserPut.class) @JsonView({UserDto.UserView.UserPut.class}) UserDto dto){
        Optional<UserModel> userModelOptional = service.findById(userId);
        log.debug("PUT updateUser userDto received {}", dto.toString());
        if(!userModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }else{
            var userModel = userModelOptional.get();
            userModel.setFullName(dto.getFullName());
            userModel.setPhoneNumber(dto.getPhoneNumber());
            userModel.setCpf(dto.getCpf());
            userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

            service.save(userModel);

            log.debug("PUT updateUser userDto saved {}", userModel.toString());
            log.info("User updated successfully userId {}", userModel.getUserId());
            return ResponseEntity.status(HttpStatus.OK).body(userModel);
        }
    }

    @PutMapping("/{userId}/password")
    public ResponseEntity<Object> updatePassword(@PathVariable(value = "userId") UUID userId,
                                                 @RequestBody @Validated(UserDto.UserView.PasswordPut.class) @JsonView({UserDto.UserView.PasswordPut.class}) UserDto dto){
        log.debug("PUT updatePassword userDto received {} ", dto.toString());
        Optional<UserModel> userModelOptional = service.findById(userId);
        if(!userModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }if(!userModelOptional.get().getPassword().equals(dto.getOldPassword())){
            log.warn("Missmatched old password userId {}", dto.getUserId());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Missmatched old password");
        }
        else{
            var userModel = userModelOptional.get();
            userModel.setPassword(dto.getPassword());
            userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

            service.save(userModel);
            log.debug("PUT updatePassword userModel saved {} ", userModel.toString());
            log.info("Password updated successfully userId {} ", userModel.getUserId());
            return ResponseEntity.status(HttpStatus.OK).body("Password updated successfully");
        }
    }

    @PutMapping("/{userId}/image")
    public ResponseEntity<Object> updateImage(@PathVariable(value = "userId") UUID userId,
                                              @RequestBody @Validated(UserDto.UserView.ImagePut.class)
                                              @JsonView({UserDto.UserView.ImagePut.class}) UserDto dto){
        log.debug("PUT updateImage userDto received {} ", dto.toString());
        Optional<UserModel> userModelOptional = service.findById(userId);
        if(!userModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        else{
            var userModel = userModelOptional.get();
            userModel.setImageUrl(dto.getImageUrl());
            userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

            service.save(userModel);
            log.debug("PUT updateImage userModel saved {} ", userModel.toString());
            log.info("Image updated successfully userId {} ", userModel.getUserId());
            return ResponseEntity.status(HttpStatus.OK).body(userModel);
        }
    }

}
