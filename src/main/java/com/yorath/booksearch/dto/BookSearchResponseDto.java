package com.yorath.booksearch.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;


/**
 * 오픈 책검색 API의 응답객체,
 * 항목구성은 카카오 책검색 API의 항목들로 정의
 */
@ToString
@Builder
@Getter
@NoArgsConstructor @AllArgsConstructor
public class BookSearchResponseDto {

    private Meta meta;
    @Builder.Default
    private List<Documents> documents = new ArrayList<>();

    @ToString
    @Builder
    @Getter
    @NoArgsConstructor @AllArgsConstructor
    public static class Meta {
        private int total_count;
        private int pageable_count;
        private boolean is_end;
    }

    @ToString
    @Builder
    @Getter
    @NoArgsConstructor @AllArgsConstructor
    public static class Documents {
        private String url;
        @Builder.Default
        private List<String> translators = new ArrayList<>();
        private String title;
        private String thumbnail;
        private String status;
        private double sale_price;
        private String publisher;
        private double price;
        private String isbn;
        private String datetime;
        private String contents;
        @Builder.Default
        private List<String> authors = new ArrayList<>();
    }


}
