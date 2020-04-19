package com.yorath.booksearch.service;

import com.yorath.booksearch.domain.BookSearchHistory;
import com.yorath.booksearch.domain.SearchKeywordSummery;
import com.yorath.booksearch.domain.repository.BookSearchHistoryRepository;
import com.yorath.booksearch.domain.repository.SearchKeywordSummeryRepository;
import com.yorath.booksearch.dto.KeywordSummeryDto;
import com.yorath.booksearch.dto.MyBookSearchHistoryDto;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookSearchServiceImpl implements BookSearchService {

    private final BookSearchHistoryRepository historyRepository;
    private final SearchKeywordSummeryRepository summeryRepository;

    public BookSearchServiceImpl(BookSearchHistoryRepository historyRepository, SearchKeywordSummeryRepository summeryRepository) {
        this.historyRepository = historyRepository;
        this.summeryRepository = summeryRepository;
    }

    /**
     * 나의 검색 히스토리 리스트 조회
     * @param userId    회원 ID
     * @return
     */
    @Override
    public List<MyBookSearchHistoryDto> getMyBookSearchHistoryList(String userId) {
        return historyRepository.findByUserIdOrderByCreatedDateDesc(userId).stream()
                .map(bookSearchHistory -> new MyBookSearchHistoryDto(bookSearchHistory))
                .collect(Collectors.toList());
    }

    /**
     * 검색 키워드 TOP10 리스트 조회
     * @return
     */
    @Override
    public List<KeywordSummeryDto> getTop10KeywordList() {
        return summeryRepository.findTop10ByOrderBySearchCountDesc().stream()
                .map(keywordSummery -> new KeywordSummeryDto(keywordSummery))
                .collect(Collectors.toList());
    }

    /**
     * 나의 검색 히스토리 저장
     * @param userId
     * @param keyword
     * @return
     */
    @Transactional
    @Async
    @Override
    public void saveMyBookSearchHistory(String userId, String keyword) {

        // 나의 검색 히스토리 저장
        historyRepository.save(new BookSearchHistory().builder()
                .userId(userId)
                .keyword(keyword)
                .build()
        );

        // Keyword 검색횟수 저장
        // TODO  동시성 검토
        summeryRepository.findFirstByKeyword(keyword).ifPresentOrElse(
                // 검색횟수 증가
                keywordSummery -> keywordSummery.increaseCount(),
                // 최초 등록
                () -> summeryRepository.save(SearchKeywordSummery.builder()
                        .keyword(keyword)
                        .searchCount(1)
                        .build())
        );
    }

}
