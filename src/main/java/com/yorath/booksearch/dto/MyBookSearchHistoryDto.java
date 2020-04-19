package com.yorath.booksearch.dto;


import com.yorath.booksearch.domain.BookSearchHistory;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Getter
@NoArgsConstructor
public class MyBookSearchHistoryDto {

    private String userId;
    private String keyword;
    private String createDate;

    /**
     * 회원별 책검색이력 Domain 엔티티를 인자로 받아 나의 검색이력 DTO 객체로 생성하는 생성자 메서드
     * @param bookSearchHistory
     */
    public MyBookSearchHistoryDto(BookSearchHistory bookSearchHistory) {
        userId = bookSearchHistory.getUserId();
        keyword = bookSearchHistory.getKeyword();
        createDate = convertDateTimeToString(bookSearchHistory.getCreatedDate());
    }

    /**
     * 로컬데이트타입 -> String 으로 변환
     * @param localDateTime
     * @return
     */
    private String convertDateTimeToString(LocalDateTime localDateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return Optional.ofNullable(localDateTime)
                .map(formatter::format)
                .orElse("");
    }
}
