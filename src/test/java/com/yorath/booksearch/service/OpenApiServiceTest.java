package com.yorath.booksearch.service;

import com.yorath.booksearch.dto.BookSearchResponseDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles(value = "local")
class OpenApiServiceTest {

    @Autowired
    @Qualifier("openApiKakaoServiceImpl")
    OpenApiService kakaoOpenApiService;

    @Autowired
    @Qualifier("openApiNaverServiceImpl")
    OpenApiService naverOpenApiService;

    String keyword;
    int page, size;

    @BeforeEach
    void setUp() {
        keyword = "자바";
        page = 1;
        size = 10;
    }

    @Test
    @DisplayName("카카오 책검색 API 호출 테스트")
    void kakaoSearchBookTest() {

        // then
        BookSearchResponseDto bookSearchResponseDto = kakaoOpenApiService.searchBook(keyword, page, size);
        System.out.println(bookSearchResponseDto.toString());
        // expect
        Assertions.assertNotNull(bookSearchResponseDto);
    }

    @Test
    @DisplayName("네이버 책검색 API 호출 테스트")
    void naverSearchBookTest() {

        // then
        BookSearchResponseDto bookSearchResponseDto = naverOpenApiService.searchBook(keyword, page, size);
        System.out.println(bookSearchResponseDto.toString());
        // expect
        Assertions.assertNotNull(bookSearchResponseDto);
    }
}