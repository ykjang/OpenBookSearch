package com.yorath.booksearch.service;

import com.yorath.booksearch.domain.repository.MembersRepository;
import com.yorath.booksearch.dto.JoinMemberDto;
import com.yorath.booksearch.dto.MemberDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class MemberServiceImpl implements MemberService {

    private final MembersRepository membersRepository;
    public MemberServiceImpl(MembersRepository membersRepository) {
        this.membersRepository = membersRepository;
    }

    /**
     * 회원정보 조회
     * @param userId
     * @return
     * @throws RestClientException
     */
    @Override
    public Optional<MemberDto> findMember(String userId) throws RestClientException {
        return membersRepository.findFirstByUserId(userId).map(MemberDto::new);
    }

    /**
     * 회원 등록 
     * @param joinMemberDto
     * @return
     */
    @Transactional
    @Override
    public String registMember(JoinMemberDto joinMemberDto) {
        return membersRepository.save(joinMemberDto.toDomainEntity()).getUserId();
    }
}
