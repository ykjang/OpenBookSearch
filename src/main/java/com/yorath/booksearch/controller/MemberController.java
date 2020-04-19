package com.yorath.booksearch.controller;


import com.yorath.booksearch.common.ApiResponseDto;
import com.yorath.booksearch.common.ApiResultStatus;
import com.yorath.booksearch.common.UserInfo;
import com.yorath.booksearch.dto.JoinMemberDto;
import com.yorath.booksearch.dto.LoginDto;
import com.yorath.booksearch.dto.MemberDto;
import com.yorath.booksearch.exception.ServiceException;
import com.yorath.booksearch.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

@Slf4j
@RestController
public class MemberController {


    @Resource
    private UserInfo userInfo;

    private final MemberService memberService;
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    /**
     * 회원가입
     *
     * @param joinMemberDto
     * @return
     */
    @PostMapping(value = "/member")
    public ResponseEntity<ApiResponseDto> joinMember(@RequestBody @Valid final JoinMemberDto joinMemberDto) {
        ApiResponseDto responseDto = new ApiResponseDto();

        // ID 중복확인
        if (memberService.findMember(joinMemberDto.getUserId()).isPresent()) {
            throw new ServiceException(ApiResultStatus.MEMBER_ID_DUPLICATED, HttpStatus.BAD_REQUEST);
        }
        String newUserId = memberService.registMember(joinMemberDto);

        responseDto.setSuccess(newUserId);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }


    /**
     * 로그인
     * 로그인이 성공하면 세션객체에 사용자장보를 저장한다.
     *
     * @param loginDto
     * @return
     */
    @PostMapping(value = "/login")
    public ResponseEntity<ApiResponseDto> login(@RequestBody @Valid LoginDto loginDto) {

        ApiResponseDto responseDto = new ApiResponseDto();

        // 등록된 유저가 없는 경우
        MemberDto memberDto = memberService.findMember(loginDto.getUserId())
                .orElseThrow(() -> new ServiceException(ApiResultStatus.MEMBER_ID_NOT_EXIST, HttpStatus.NOT_FOUND));

        // 패스워드 체크
        if (BCrypt.checkpw(loginDto.getPassword(), memberDto.getPassword()) == false) {
            log.error("ErrorMessage:{}", ApiResultStatus.LOGIN_PASSWORD_INVALID.getMessage());
            throw new ServiceException(ApiResultStatus.LOGIN_PASSWORD_INVALID, HttpStatus.BAD_REQUEST);
        }

        // 세션등록
        userInfo.setUserId(loginDto.getUserId());

        responseDto.setSuccess(null);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    /**
     * 세션정보를 반환
     * 클라이언트에서 현재 세션이 유효한지 객체정보로 확인이 가능하다.
     *
     * @return
     */
    @GetMapping(value = "session")
    public ResponseEntity<UserInfo> getSessionInfo() {
        return new ResponseEntity<>(userInfo, HttpStatus.OK);
    }
}
