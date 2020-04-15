package com.yorath.booksearch.controller;


import com.yorath.booksearch.common.CommonResponseDto;
import com.yorath.booksearch.common.CommonResultStatus;
import com.yorath.booksearch.dto.JoinMemberDto;
import com.yorath.booksearch.exception.ServiceException;
import com.yorath.booksearch.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/v1")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    /**
     * 회원가입
     * @param joinMemberDto
     * @return
     */
    @PostMapping(value = "/member")
    public ResponseEntity<CommonResponseDto> joinMember(@RequestBody @Valid JoinMemberDto joinMemberDto) {

        CommonResponseDto responseDto = new CommonResponseDto();

        // ID 중복확인
        memberService.findMember(joinMemberDto.getUserId()).ifPresentOrElse(
                members -> {
                    throw new ServiceException(CommonResultStatus.ERROR_MEMBER_ID_DUPLICATED);
                },
                () -> {
                    // 신규등록
                    memberService.registMember(joinMemberDto);
                    responseDto.setSuccess(null);
                });

        return  new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }
}
