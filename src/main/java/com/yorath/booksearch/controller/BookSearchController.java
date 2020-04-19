package com.yorath.booksearch.controller;

import com.yorath.booksearch.common.ApiResponseDto;
import com.yorath.booksearch.dto.KeywordSummeryDto;
import com.yorath.booksearch.dto.MyBookSearchHistoryDto;
import com.yorath.booksearch.service.BookSearchService;
import com.yorath.booksearch.service.OpenApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;

import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class BookSearchController {

    final BookSearchService bookSearchService;
    final OpenApiService kakaoOpenApiService;
    final OpenApiService naverOpenApiService;

    public BookSearchController(@Qualifier("openApiKakaoServiceImpl") OpenApiService kakaoOpenApiService,
                                @Qualifier("openApiNaverServiceImpl") OpenApiService naverOpenApiService,
                                BookSearchService bookSearchService
                                ) {
        this.bookSearchService = bookSearchService;
        this.kakaoOpenApiService = kakaoOpenApiService;
        this.naverOpenApiService = naverOpenApiService;
    }

    /**
     * 책 검색
     * @param keyword 검색 키워드
     * @return
     */
    @GetMapping(value = "/book/search/{userId}")
    public ResponseEntity<ApiResponseDto> getBookSearch(
            @PathVariable String userId,
            @RequestParam(value = "page") int page,
            @RequestParam(value = "size") int size,
            @RequestParam(value = "keyword") @NotNull String keyword) {

        ApiResponseDto responseDto = new ApiResponseDto();
        try {
            responseDto.setSuccess(kakaoOpenApiService.searchBook(keyword, page, size));
        } catch (RestClientException re) {
            // 원격호출 실패한 경우, 네이버 책검색 서비스 호출
            log.info("카카오 책검색 API 호출실패, 네이버 책검색 API호출 ");
            responseDto.setSuccess(naverOpenApiService.searchBook(keyword, page, size));
        }

        // 검색이력 저장(비동기 처리)
        bookSearchService.saveMyBookSearchHistory(userId, keyword);
        return ResponseEntity.ok(responseDto);
    }


    /**
     * 나의 책검색 히스토리 리스트 조회
     * @return
     */
    @GetMapping(value = "/book/search/{userId}/histories")
    public ResponseEntity<ApiResponseDto> getMyBookSearchHistories(@PathVariable String userId) {

        ApiResponseDto responseDto = new ApiResponseDto();

         List<MyBookSearchHistoryDto> historyDtoList =  bookSearchService.getMyBookSearchHistoryList(userId);
        responseDto.setSuccess(historyDtoList);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    /**
     * 검색 키워드 TOP10 리스트
     * @return
     */
    @GetMapping(value = "/book/keyword/top10")
    public ResponseEntity<ApiResponseDto> getTop10Keywords() {

        ApiResponseDto responseDto = new ApiResponseDto();

        List<KeywordSummeryDto> historyDtoList =  bookSearchService.getTop10KeywordList();
        responseDto.setSuccess(historyDtoList);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
