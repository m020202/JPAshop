package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    public void save(Member member) {
        em.persist(member);
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        String query = "select m from Member m";
        List<Member> members = em.createQuery(query, Member.class)
                .getResultList();
        return members;
    }

    public List<Member> findByName(String name) {
        String query = "select m from Member m where m.name = :name";
        List<Member> result = em.createQuery(query, Member.class)
                .setParameter("name", name)
                .getResultList();
        return result;
    }
}
