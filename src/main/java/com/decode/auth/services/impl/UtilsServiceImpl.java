package com.decode.auth.services.impl;

import com.decode.auth.services.UtilsService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UtilsServiceImpl implements UtilsService {

    public String createUrlGetAllCoursesByUser(UUID userId, Pageable page){
        return "/courses?userId="+userId+"&page="+page.getPageNumber()+
                "&size="+page.getPageSize()+"&sort="+page.getSort().toString().replaceAll(": ", ",");
    }
}
