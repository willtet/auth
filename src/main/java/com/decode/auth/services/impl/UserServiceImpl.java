package com.decode.auth.services.impl;

import com.decode.auth.clients.CourseClient;
import com.decode.auth.models.UserCourseModel;
import com.decode.auth.models.UserModel;
import com.decode.auth.repositories.UserCourseRepository;
import com.decode.auth.repositories.UserRepository;
import com.decode.auth.services.UserService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository repository;

    @Autowired
    UserCourseRepository userCourseRepository;

    @Autowired
    CourseClient courseClient;

    @Override
    public List<UserModel> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<UserModel> findById(UUID userId) {
        return repository.findById(userId);
    }

    @Transactional
    @Override
    public void delete(UserModel userModelOptional) {
        boolean deleteUserCourseInCourse = false;
        List<UserCourseModel> userCourseModelList = userCourseRepository.findAllUserCourseIntoUser(userModelOptional.getUserId());
        if(!userCourseModelList.isEmpty()) {
            userCourseRepository.deleteAll(userCourseModelList);
            deleteUserCourseInCourse = true;
        }

        repository.delete(userModelOptional);

        if (deleteUserCourseInCourse){
            courseClient.deleteUserInCourse(userModelOptional.getUserId());
        }
    }


    @Override
    public void save(UserModel userModel) {
        repository.save(userModel);
    }

    @Override
    public boolean existByUsername(String username) {
        return repository.existsByUsername(username);
    }

    @Override
    public boolean existByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public Page<UserModel> findAll(Pageable pageable, Specification<UserModel> spec) {
        return repository.findAll(spec, pageable );
    }

}
