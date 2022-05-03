package com.decode.auth.services.impl;

import com.decode.auth.services.UtilsService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UtilsServiceImpl implements UtilsService {

    String RESQUEST_URL = "http://localhost:8082";

    public String createUrl(UUID userId, Pageable page){
        return RESQUEST_URL+"/courses?userId="+userId+"&page="+page.getPageNumber()+
                "&size="+page.getPageSize()+"&sort="+page.getSort().toString().replaceAll(": ", ",");
    }
}
