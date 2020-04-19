package com.yorath.booksearch.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class BookSearchHistory extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(length = 12)
    private String userId;

    @Column(length = 256)
    private String keyword;

    @Builder
    public BookSearchHistory(String userId, String keyword) {
        this.userId = userId;
        this.keyword = keyword;
    }
}
