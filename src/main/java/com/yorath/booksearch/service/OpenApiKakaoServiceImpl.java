package com.yorath.booksearch.service;

import com.yorath.booksearch.dto.BookSearchResponseDto;
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
public class OpenApiKakaoServiceImpl implements OpenApiService {

    // Kakako API Env
    @Value("${open-api.kakao.app-key}")
    private String OPEN_API_KAKAO_APP_KEY;
    @Value("${open-api.kakao.book-search-url}")
    private String OPEN_API_KAKAO_BOOK_SEARCH_URL;

    private static final int DEFAULT_START_PAGE = 1;    // 검색 사작페이지 번호
    private static final int DEFAULT_PAGE_SIZE = 10;    // 퍼이지당 검색건수

    private final RestTemplate kakaoOpenApiRestTemplate;
    public OpenApiKakaoServiceImpl(RestTemplate kakaoOpenApiRestTemplate) {
        this.kakaoOpenApiRestTemplate = kakaoOpenApiRestTemplate;
    }

    /**
     * 카카오 책검색 API 호출 서비스
     * @param keyword
     * @return
     * @throws RestClientException
     */
    @Override
    public BookSearchResponseDto searchBook(String keyword, int page, int size) throws RestClientException{

        // Header Setting
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.set("Authorization", "KakaoAK " + OPEN_API_KAKAO_APP_KEY );
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(httpHeaders);

        // Request Param Setting
        if(page == 0) page = DEFAULT_START_PAGE;
        if(size == 0) size = DEFAULT_PAGE_SIZE;

        UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromUriString(OPEN_API_KAKAO_BOOK_SEARCH_URL)
                .queryParam("query", keyword).encode(StandardCharsets.ISO_8859_1)
                .queryParam("page", page)
                .queryParam("size", size);
        log.debug("[Request URL]{}", urlBuilder.toUriString());

        // API Call
        ResponseEntity<BookSearchResponseDto> responseEntity = kakaoOpenApiRestTemplate.exchange(urlBuilder.toUriString(), HttpMethod.GET, requestEntity, BookSearchResponseDto.class);

        if (responseEntity.getStatusCode().isError()) {
            log.debug("카카오 책검색 API 호출에 실패, 네이버 API로 전환");
            throw new RestClientException(responseEntity.getStatusCode().name());
        }

        return responseEntity.getBody();

    }

}
