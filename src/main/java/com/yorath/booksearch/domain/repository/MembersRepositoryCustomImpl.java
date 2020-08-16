package com.yorath.booksearch.domain.repository;

import com.yorath.booksearch.domain.Members;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MembersRepositoryCustomImpl implements MembersRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Members> findByUserIdAndPassword(Set<String> userIds) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Members> query = cb.createQuery(Members.class);

        Root<Members> members = query.from(Members.class);
        Path<String> userIdPath = members.get("userId");

        List<Predicate> predicates = new ArrayList<>();
        for (String userId : userIds) {
            predicates.add(cb.like(userIdPath, userId));
        }

        query.select(members).where(cb.or(predicates.toArray(new Predicate[predicates.size()])));

        return entityManager.createQuery(query).getResultList();
    }
}
