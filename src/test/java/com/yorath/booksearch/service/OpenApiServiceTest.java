package com.yorath.booksearch.service;

import com.yorath.booksearch.dto.BookSearchResponseDto;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.hamcrest.CoreMatchers.is;

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
    int page=1, size=10;

    @BeforeEach
    void initTest() {
        keyword = "자바";
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