package com.yorath.booksearch.service;

import com.yorath.booksearch.common.ApiResultStatus;
import com.yorath.booksearch.dto.BookSearchResponseDto;
import com.yorath.booksearch.dto.NaverBookSearchResponseDto;
import com.yorath.booksearch.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
@Service
public class NaverBookSearchServiceImpl implements BookSearchService {


    @Value("${open-api.naver.client-id}")
    private String OPEN_API_NAVER_CLIENT_ID;
    @Value("${open-api.naver.client-secret}")
    private String OPEN_API_NAVER_CLIENT_SECRET;
    @Value("${open-api.naver.book-search-url}")
    private String OPEN_API_NAVER_BOOKE_SEARCH_URL;

    private final RestTemplate restTemplate;
    public NaverBookSearchServiceImpl(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Override
    public BookSearchResponseDto searchBook(String keyword) throws RestClientException {

        // Header Setting
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.set("X-Naver-Client-Id", OPEN_API_NAVER_CLIENT_ID );
        httpHeaders.set("X-Naver-Client-Secret", OPEN_API_NAVER_CLIENT_SECRET );
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(httpHeaders);

        // Request Param Setting TODO: 한글 인코딩문제 확인(한글 검색안됨)
        String encKeyword =  URLEncoder.encode(keyword, StandardCharsets.UTF_8);
        UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromHttpUrl(OPEN_API_NAVER_BOOKE_SEARCH_URL)
                .queryParam("query", encKeyword);

        ResponseEntity<NaverBookSearchResponseDto> responseEntity = restTemplate.exchange(urlBuilder.toUriString(),
                HttpMethod.GET, requestEntity, NaverBookSearchResponseDto.class);

        if (responseEntity.getStatusCode().isError()) {
            log.debug("네이버 책검색 API 호출에 실패앴습니다");
            // TODO: 예외처리
            throw new ServiceException(ApiResultStatus.INTERNAL_SERVER_ERROR);
        }

        return responseEntity.getBody().toResponseDto();
    }

}
