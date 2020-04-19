package com.yorath.booksearch.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yorath.booksearch.common.ApiResponseDto;
import com.yorath.booksearch.common.ApiResultStatus;
import com.yorath.booksearch.domain.BookSearchHistory;
import com.yorath.booksearch.dto.BookSearchResponseDto;
import com.yorath.booksearch.dto.MyBookSearchHistoryDto;
import com.yorath.booksearch.service.BookSearchServiceImpl;
import com.yorath.booksearch.service.OpenApiKakaoServiceImpl;
import com.yorath.booksearch.service.OpenApiNaverServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@ActiveProfiles(value = "local")
class BookSearchControllerTest {

    @InjectMocks  BookSearchController bookSearchController;

    @Mock
    BookSearchServiceImpl bookSearchService;
    @Mock
    OpenApiKakaoServiceImpl openApiKakaoService;
    @Mock
    OpenApiNaverServiceImpl openApiNaverService;

    private MockMvc mockMvc;


    private String userId;
    private String keyword;
    private int page, size;

    private BookSearchResponseDto bookSearchResponseDto;

    @BeforeEach
    public void setUp() {
        userId = "testuser1";
        keyword = "JAVA";
        page = 1;
        size = 10;

        mockMvc = MockMvcBuilders.standaloneSetup(bookSearchController).
                addFilter(new CharacterEncodingFilter("UTF-8"))
                .build();
    }

    @Test
    @DisplayName("책검색 컨트롤러 테스트")
    void searchBook() throws Exception{

        // given
        List<BookSearchResponseDto.Documents> documents = Arrays.asList(
                BookSearchResponseDto.Documents.builder().title("JAVA 1").contents("JAVA Contents1").authors(Arrays.asList("author1")).price(10000).isbn("isbn01").url("http://books/1").build(),
                BookSearchResponseDto.Documents.builder().title("JAVA 2").contents("JAVA Contents2").authors(Arrays.asList("author2")).price(20000).isbn("isbn01").url("http://books/2").build(),
                BookSearchResponseDto.Documents.builder().title("JAVA 3").contents("JAVA Contents2").authors(Arrays.asList("author3")).price(30000).isbn("isbn01").url("http://books/2").build()
        );

         bookSearchResponseDto = BookSearchResponseDto.builder()
                .meta(BookSearchResponseDto.Meta.builder().total_count(100)
                        .pageable_count(10)
                        .is_end(false).build()
                )
                .documents(documents)
                .build();

        when(openApiKakaoService.searchBook(userId, page, size)).thenReturn(bookSearchResponseDto);



        mockMvc.perform(get("/api/v1/book/search/" + userId)
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("keyword", "JAVA")
                .queryParam("page", "1")
                .queryParam("size", "10"))
                .andExpect(status().isOk())
                .andDo(print());

        //then
        verify(openApiKakaoService).searchBook(keyword, page, size);
        verifyNoInteractions(openApiKakaoService);

    }


    @Test
    @DisplayName("책검색 이력조회 컨트롤러 테스트")
    void mySearchHistoryTest() throws Exception{

        // given
        List<MyBookSearchHistoryDto> expectList = Arrays.asList(
                new MyBookSearchHistoryDto(BookSearchHistory.builder().userId(userId).keyword("java").build()),
                new MyBookSearchHistoryDto(BookSearchHistory.builder().userId(userId).keyword("spring").build()),
                new MyBookSearchHistoryDto(BookSearchHistory.builder().userId(userId).keyword("jpa").build())
        );
        when(bookSearchService.getMyBookSearchHistoryList(userId)).thenReturn(expectList);

        // when
        MvcResult mvcResult = mockMvc.perform(
                get("/api/v1/book/search/" + userId + "/histories")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
        .andReturn();

        // 읃답객체 추출
        String responseJsonString = mvcResult.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        ApiResponseDto resultResponseDto = mapper.readValue(responseJsonString, ApiResponseDto.class);

        List<MyBookSearchHistoryDto> resultMyBookSearchHistoryList = (List)resultResponseDto.getData();

        // then
        verify(bookSearchService).getMyBookSearchHistoryList(userId);
        assertEquals(ApiResultStatus.REQUEST_SUCCESS.getCode(), resultResponseDto.getCode());
        assertEquals(3, resultMyBookSearchHistoryList.size());


    }
}