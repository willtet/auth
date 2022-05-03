package com.decode.auth.services.impl;

import com.decode.auth.repositories.UserCourseRepository;
import com.decode.auth.services.UserCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserCourseServiceImpl implements UserCourseService {
    @Autowired
    UserCourseRepository repository;
}
