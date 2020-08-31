package RealSpringBootJPA.datajpa.Repository;

import RealSpringBootJPA.datajpa.entity.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class MemberJpaRepository {
    @PersistenceContext
    private EntityManager em;

    public Member save(Member member){
        em.persist(member);
        return member;
    }

    public void delete(Member member){
        em.remove(member);
    }

    //전체 조회 또는 특정조건 조회시는 별도의 메소드를 제공안하기에
    //jpql을 이용해야한다.
    public List<Member> findAll(){
        List<Member> result =  em.createQuery("select m from Member m", Member.class).getResultList();
        return result;
    }

    //Member가 null이기에 Optional 조회
    public Optional<Member> findById(Long id){
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    //RowCount조회
    public long count(){
        return em.createQuery("select count(m) from Member m", Long.class).getSingleResult();
    }
    public Member find(Long id){
        return em.find(Member.class, id);
    }
}
