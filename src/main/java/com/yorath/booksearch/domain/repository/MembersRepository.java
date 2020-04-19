package com.yorath.booksearch.domain.repository;

import com.yorath.booksearch.domain.Members;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MembersRepository extends JpaRepository<Members, Long> {

    Optional<Members> findFirstByUserId(String userId);
}
