package com.yorath.booksearch.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 네이버 책검색 API의 응답객체
 */
@ToString
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class NaverBookSearchResponseDto {

    private int start;  // 검색시적점 (페이지번호)
    private int total;  // 충 검색건수
    private int display;    // 페이지당 표시건수
    private String lastBuildDate;

    private List<Items> items = new ArrayList<>();


    @ToString
    @Getter @Setter
    public static class Items {
        private String description;
        private String isbn;
        private String pubdate;
        private String publisher;
        private double discount;
        private double price;
        private String author;
        private String image;
        private String link;
        private String title;
    }

    /**
     * 네이버 책검색 API의 읃답객체를 기본 응답객체로 매핑,변환하는  메서드
     * @return
     */
    public BookSearchResponseDto toResponseDto() {

        // 책 상세정보 목록 매핑
        List<BookSearchResponseDto.Documents> documents = this.getItems().stream().map(items -> BookSearchResponseDto.Documents.builder()
                .title(items.getTitle())
                .authors(Arrays.asList(items.getAuthor()))
                .contents(items.getDescription())
                .url(items.getLink())
                .isbn(items.getIsbn())
                .publisher(items.getPublisher())
                .price(items.getPrice())
                .sale_price(items.getDiscount())
                .datetime(items.getPubdate())
                .thumbnail(items.getImage())
                .build()).collect(Collectors.toList());

        // 책 검색 페이징정보
        BookSearchResponseDto.Meta meta = BookSearchResponseDto.Meta.builder()
                .total_count(this.getTotal())
                .pageable_count(this.getDisplay())
                .is_end(isEnd())
                .build();

        return BookSearchResponseDto.builder()
                .meta(meta)
                .documents(documents)
                .build();
    }

    /**
     * 검색페이지의 끝 여부 확인
     *
     * @return
     */
    private boolean isEnd() {

        if( this.getTotal() == 0 || this.getDisplay() == 0) return true;
        // 총페이지수 계산
        int totalPageCount = this.getTotal()/this.getDisplay();

        if (this.getTotal() % this.getDisplay() > 0) {
            totalPageCount = totalPageCount + 1 ;
        }
        return this.start == totalPageCount;
    }
}
