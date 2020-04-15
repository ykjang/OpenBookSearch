package com.yorath.booksearch.service;

import com.yorath.booksearch.dto.BookSearchResponseDto;
import org.springframework.web.client.RestClientException;

public interface BookSearchService {
    BookSearchResponseDto searchBook(String keyword) throws RestClientException;
}
