package com.decode.auth.services.impl;

import com.decode.auth.models.UserCourseModel;
import com.decode.auth.models.UserModel;
import com.decode.auth.repositories.UserCourseRepository;
import com.decode.auth.services.UserCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserCourseServiceImpl implements UserCourseService {
    @Autowired
    UserCourseRepository repository;

    @Override
    public boolean existsByUserAndCourseId(UserModel userModel, UUID courseId) {
        return repository.existsByUserAndCourseId(userModel, courseId);
    }

    @Override
    public UserCourseModel save(UserCourseModel model) {
        return repository.save(model);
    }
}
