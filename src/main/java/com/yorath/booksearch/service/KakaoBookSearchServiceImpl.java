package com.yorath.booksearch.service;

import com.yorath.booksearch.common.CommonResultStatus;
import com.yorath.booksearch.dto.BookSearchResponseDto;
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
public class KakaoBookSearchServiceImpl implements BookSearchService {

    /*
    open-api:
  kakao:
    app-key: 2fcfd0d6690256bb06d549f1bafb8c73
    book-search-url: https://dapi.kakao.com/v3/search/book
     */
    @Value("${open-api.kakao.app-key}")
    private String OPEN_API_KAKAO_APP_KEY;
    @Value("${open-api.kakao.book-search-url}")
    private String OPEN_API_KAKAO_BOOKE_SEARCH_URL;


    private final RestTemplate restTemplate;
    public KakaoBookSearchServiceImpl(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    /**
     * 카카오 책검색 API 호출 서비스
     * @param keyword
     * @return
     * @throws RestClientException
     */
    @Override
    public BookSearchResponseDto searchBook(String keyword) throws RestClientException{

        // Header Setting
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.set("Authorization", "KakaoAK " + OPEN_API_KAKAO_APP_KEY );
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(httpHeaders);

        // Request Param Setting TODO: 한글 인코딩문제 확인(한글 검색안됨)
        String encKeyword =  URLEncoder.encode(keyword, StandardCharsets.UTF_8);
        UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromHttpUrl(OPEN_API_KAKAO_BOOKE_SEARCH_URL)
                .queryParam("query", encKeyword);
        log.debug("[Request URL]{}", urlBuilder.toUriString());

        // API Call
        ResponseEntity<BookSearchResponseDto> responseEntity = restTemplate.exchange(urlBuilder.toUriString(), HttpMethod.GET, requestEntity, BookSearchResponseDto.class);

        if (responseEntity.getStatusCode().isError()) {
            log.debug("네이버 책검색 API 호출에 실패앴습니다");
            // TODO: 예외처리
            throw new ServiceException(CommonResultStatus.ERROR_INTERNAL_SERVER);
        }

        return responseEntity.getBody();

    }


}
