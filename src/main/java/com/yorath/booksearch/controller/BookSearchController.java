package com.yorath.booksearch.controller;

import com.yorath.booksearch.common.ApiResponseDto;
import com.yorath.booksearch.common.ApiResultStatus;
import com.yorath.booksearch.common.UserInfo;
import com.yorath.booksearch.dto.KeywordSummeryDto;
import com.yorath.booksearch.dto.MyBookSearchHistoryDto;
import com.yorath.booksearch.service.BookSearchService;
import com.yorath.booksearch.service.OpenApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@RestController
public class BookSearchController {

    @Resource
    private UserInfo userInfo;  // 세션객체

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
     * 카카오 API 호출시 에러가 발생한 네이버 API로 대체한다.
     *
     * @param keyword 검색 키워드
     * @param page    페이지번호 기본값:1
     * @param size    페이지당 검색건수 기본값: 10
     * @return
     */
    @GetMapping(value = "/book/search")
    public ResponseEntity<ApiResponseDto> getBookSearch(
            @RequestParam(value = "keyword") @NotNull String keyword,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {

        ApiResponseDto responseDto = new ApiResponseDto();
        if (userInfo.getUserId() == null) {
            responseDto.setStatus(ApiResultStatus.REQUEST_SESSION_EXPIRED);
            return new ResponseEntity<>(responseDto, HttpStatus.UNAUTHORIZED);
        }

        try {
            responseDto.setSuccess(kakaoOpenApiService.searchBook(keyword, page, size));
        } catch (RestClientException re) {
            // 원격호출 실패한 경우, 네이버 책검색 서비스 호출
            log.info("카카오 책검색 API 호출실패, 네이버 책검색 API호출 ");
            responseDto.setSuccess(naverOpenApiService.searchBook(keyword, page, size));
        }

        // 검색이력 저장(비동기 처리)
        bookSearchService.saveMyBookSearchHistory(userInfo.getUserId(), keyword);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }


    /**
     * 나의 책검색 히스토리 리스트 조회
     * @return
     */
    @GetMapping(value = "/book/search/{userId}/histories")
    public ResponseEntity<ApiResponseDto> getMyBookSearchHistories(@PathVariable String userId) {

        ApiResponseDto responseDto = new ApiResponseDto();
        if (userInfo.getUserId() == null) {
            responseDto.setStatus(ApiResultStatus.REQUEST_SESSION_EXPIRED);
            return new ResponseEntity<>(responseDto, HttpStatus.UNAUTHORIZED);
        }
        if (!userInfo.getUserId().equals(userId)) {
            responseDto.setStatus(ApiResultStatus.REQUEST_PARAM_INVALID);
            return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
        }

        List<MyBookSearchHistoryDto> historyDtoList = bookSearchService.getMyBookSearchHistoryList(userId);
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
        if (userInfo.getUserId() == null) {
            responseDto.setStatus(ApiResultStatus.REQUEST_SESSION_EXPIRED);
            return new ResponseEntity<>(responseDto, HttpStatus.UNAUTHORIZED);
        }

        List<KeywordSummeryDto> historyDtoList = bookSearchService.getTop10KeywordList();
        responseDto.setSuccess(historyDtoList);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
