package com.decode.auth.clients;

import com.decode.auth.dtos.CourseDto;
import com.decode.auth.dtos.ResponsePageDto;
import com.decode.auth.services.UtilsService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@Log4j2
@Component
public class UserClient {
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    UtilsService utilsService;

    public Page<CourseDto> getAllCoursesByUser(UUID userId, Pageable page){
        List<CourseDto> searchResult = null;
        String endpoint = utilsService.createUrl(userId, page);
        log.debug("Request URL {}", endpoint);
        log.info("Request URL {}", endpoint);

        try {
            ParameterizedTypeReference<ResponsePageDto<CourseDto>> responseType =
                    new ParameterizedTypeReference<ResponsePageDto<CourseDto>>() {
                    };
            ResponseEntity<ResponsePageDto<CourseDto>> result = restTemplate.exchange(endpoint, HttpMethod.GET, null, responseType);
            searchResult = result.getBody().getContent();
            log.debug("Response Number of Elements: {}", searchResult.size());
        }catch (HttpStatusCodeException e){

            log.error("Error request /courses {}", e);
        }

        log.info("Ending request /courses userId {}", userId);
        return new PageImpl<>(searchResult);
    }
}
