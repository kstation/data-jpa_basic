package RealSpringBootJPA.datajpa.entity;

import RealSpringBootJPA.datajpa.domain.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class MemberJpaRepository {
    @PersistenceContext
    private EntityManager em;

    public RealSpringBootJPA.datajpa.domain.Member save(RealSpringBootJPA.datajpa.domain.Member member){
        em.persist(member);

        return member;
    }

    public RealSpringBootJPA.datajpa.domain.Member find(Long id){
        return em.find(Member.class, id);
    }
}
