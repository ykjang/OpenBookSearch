package com.yorath.booksearch.domain.repository;

import com.yorath.booksearch.domain.Members;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface MembersRepositoryCustom {

    List<Members> findByUserIdAndPassword(Set<String> userIds);
}
