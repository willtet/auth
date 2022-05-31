package com.decode.auth.services;

import com.decode.auth.models.UserCourseModel;
import com.decode.auth.models.UserModel;

import java.util.UUID;

public interface UserCourseService {
    boolean existsByUserAndCourseId(UserModel userModel, UUID courseId);

    UserCourseModel save(UserCourseModel model);

    boolean existsByCourseId(UUID courseId);

    void deleteUserCoursesByCourse(UUID courseId);
}
