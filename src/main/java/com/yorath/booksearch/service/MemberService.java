package com.yorath.booksearch.service;

import com.yorath.booksearch.dto.JoinMemberDto;
import com.yorath.booksearch.dto.MemberDto;

import java.util.Optional;

public interface MemberService {
    Optional<MemberDto> findMember(String userId);
    String registMember(JoinMemberDto joinMemberDto);
}
