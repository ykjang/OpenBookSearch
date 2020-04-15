package com.yorath.booksearch.service;

import com.yorath.booksearch.domain.Members;
import com.yorath.booksearch.dto.BookSearchResponseDto;
import com.yorath.booksearch.dto.JoinMemberDto;
import org.springframework.web.client.RestClientException;

import java.util.Optional;

public interface MemberService {

    Optional<Members> findMember(String userId) throws RestClientException;

    Members registMember(JoinMemberDto joinMemberDto);
}
