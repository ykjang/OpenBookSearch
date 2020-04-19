package com.yorath.booksearch.service;

import com.yorath.booksearch.dto.BookSearchResponseDto;
import org.springframework.web.client.RestClientException;

public interface OpenApiService {

    /**
     * 책검색 리스트 조회
     *
     * @param keyword
     * @param page  페이지번호
     * @param size
     * @return
     * @throws RestClientException
     */
    BookSearchResponseDto searchBook(String keyword, int page, int size) throws RestClientException;


}
