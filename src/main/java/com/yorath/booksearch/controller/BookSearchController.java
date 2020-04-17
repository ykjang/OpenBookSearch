package com.yorath.booksearch.controller;

import com.yorath.booksearch.common.ApiResponseDto;
import com.yorath.booksearch.service.BookSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;

import javax.validation.constraints.NotNull;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class BookSearchController {

    final BookSearchService kakaoBookSearchService;
    final BookSearchService naverBookSearchService;

    public BookSearchController(@Qualifier("kakaoBookSearchServiceImpl") BookSearchService kakaoBookSearchService,
                                @Qualifier("naverBookSearchServiceImpl") BookSearchService naverBookSearchService) {
        this.kakaoBookSearchService = kakaoBookSearchService;
        this.naverBookSearchService = naverBookSearchService;
    }


    @GetMapping(value = "/search/book")
    public ResponseEntity<ApiResponseDto> searchBook(@RequestParam(value = "keyword") @NotNull String keyword) {

        // Response Data
        ApiResponseDto responseDto = new ApiResponseDto();

        try {
            responseDto.setSuccess(kakaoBookSearchService.searchBook(keyword));
        } catch (RestClientException re) {
            // 원격호출 실패한 경우, 네이버 책검색 서비스 호출
            log.info("카카오 책검색 API 호출실패, 네이버 책검색 API호출 ");
            responseDto.setSuccess(naverBookSearchService.searchBook(keyword));
        }
        return ResponseEntity.ok(responseDto);
    }
}
