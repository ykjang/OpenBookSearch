package com.yorath.booksearch.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class SearchKeywordSummery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(length = 256)
    private String keyword;

    @Column
    private long searchCount;

    @Builder
    public SearchKeywordSummery(String keyword, long searchCount) {
        this.keyword = keyword;
        this.searchCount = searchCount;
    }

    /**
     * 검색수 증가
     */
    public void increaseCount() {
        this.searchCount++;
    }
}
