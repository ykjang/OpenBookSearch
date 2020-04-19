package com.yorath.booksearch.service;

import com.yorath.booksearch.common.ApiResultStatus;
import com.yorath.booksearch.dto.BookSearchResponseDto;
import com.yorath.booksearch.dto.NaverBookSearchResponseDto;
import com.yorath.booksearch.exception.OpenApiCallException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
@Service
public class OpenApiNaverServiceImpl implements OpenApiService {

    @Value("${open-api.naver.client-id}")
    private String OPEN_API_NAVER_CLIENT_ID;
    @Value("${open-api.naver.client-secret}")
    private String OPEN_API_NAVER_CLIENT_SECRET;
    @Value("${open-api.naver.book-search-url}")
    private String OPEN_API_NAVER_BOOK_SEARCH_URL;

    private static final int DEFAULT_START_PAGE = 1;    // 검색 사작페이지 번호
    private static final int DEFAULT_PAGE_SIZE = 10;    // 퍼이지당 검색건수

    private final RestTemplate naverOpenApiRestTemplate;
    public OpenApiNaverServiceImpl(RestTemplate naverOpenApiRestTemplate) {
        this.naverOpenApiRestTemplate = naverOpenApiRestTemplate;
    }

    @Override
    public BookSearchResponseDto searchBook(String keyword, int page, int size){

        // Header Setting
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.set("X-Naver-Client-Id", OPEN_API_NAVER_CLIENT_ID );
        httpHeaders.set("X-Naver-Client-Secret", OPEN_API_NAVER_CLIENT_SECRET );
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(httpHeaders);

        // Request Param Setting
        if(page == 0) page = DEFAULT_START_PAGE;
        if(size == 0) size = DEFAULT_PAGE_SIZE;

        UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromUriString(OPEN_API_NAVER_BOOK_SEARCH_URL)
                .queryParam("query", keyword).encode(StandardCharsets.ISO_8859_1)
                .queryParam("start", page)
                .queryParam("display", size)
                ;
        log.debug("[Request URL]{}", urlBuilder.toUriString());

        ResponseEntity<NaverBookSearchResponseDto> responseEntity;
        try {
            responseEntity = naverOpenApiRestTemplate.exchange(urlBuilder.toUriString(), HttpMethod.GET, requestEntity, NaverBookSearchResponseDto.class);
        } catch (RestClientException re) {
            log.error("[Naver 책검색 API 호출실패]{}", re.getMessage());
            throw new OpenApiCallException(ApiResultStatus.REMOTE_API_CALL_ERROR);
        }

        if (responseEntity.getStatusCode().isError()) {
            log.error("[Naver 책검색 API 호출실패]{}", responseEntity.getStatusCode());
            throw new OpenApiCallException(ApiResultStatus.REMOTE_API_CALL_ERROR, responseEntity.getStatusCode());
        }

        // 공통 DTO로 변환 후 리턴
        return responseEntity.getBody().toResponseDto();
    }

}
