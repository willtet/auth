package com.decode.auth.controllers;

import com.decode.auth.clients.CourseClient;
import com.decode.auth.dtos.CourseDto;
import com.decode.auth.dtos.UserCourseDto;
import com.decode.auth.models.UserCourseModel;
import com.decode.auth.models.UserModel;
import com.decode.auth.services.UserCourseService;
import com.decode.auth.services.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserCourseController {

    @Autowired
    CourseClient courseClient;

    @Autowired
    UserService service;

    @Autowired
    UserCourseService userCourseService;

    @GetMapping("/users/{userId}/courses")
    public ResponseEntity<Object> getAllCoursesByUser(@PageableDefault(page = 0, size = 10, sort = "courseId", direction = Sort.Direction.ASC)
                                                               Pageable pageable,
                                                       @PathVariable(value = "userId") UUID userId){
        Optional<UserModel> userModelOptional = service.findById(userId);
        if(!userModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(courseClient.getAllCoursesByUser(userId, pageable));
    }

    @PostMapping("/users/{userId}/courses/subscription")
    public ResponseEntity<Object> saveSubscriptionUserInCourse(@PathVariable(value = "userId") UUID userId,
                                                               @RequestBody @Valid UserCourseDto userCourseDto){
        Optional<UserModel> userModelOptional = service.findById(userId);
        if(!userModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        if(userCourseService.existsByUserAndCourseId(userModelOptional.get(), userCourseDto.getCourseId())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: subscription already exists!");
        }

        UserCourseModel userCourseModel = userCourseService.save(userModelOptional.get().convertToUserCourseModel(userCourseDto.getCourseId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(userCourseModel);
    }

    @DeleteMapping("/users/courses/{courseId}")
    public ResponseEntity<Object> deleteUserCourseByCourse(@PathVariable(value = "courseId") UUID courseId){
        if(!userCourseService.existsByCourseId(courseId)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("UserCourse not found");
        }

        userCourseService.deleteUserCoursesByCourse(courseId);
        return ResponseEntity.status(HttpStatus.OK).body("UserCourse deleted successfully");
    }
}
