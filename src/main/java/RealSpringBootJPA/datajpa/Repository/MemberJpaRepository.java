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

    //특정이름이고 나이가 입력받은 수 이상인 경우만 조회
    public List findByUsernameAndAgeGreaterThan(String username, int age){
        return em.createQuery("select m from Member m where m.username = :username and m.age > :age")
                .setParameter("username", username)
                .setParameter( "age", age)
                .getResultList();
    }

    //JPA를 직접 사용해서  Named 쿼리 호출
    public List<Member> findByUsernameUsageNamedQuery(String username){
        return em.createNamedQuery("Member.findByUsername", Member.class)
                .setParameter("username", username)
                .getResultList();
    }

    //JPA 순수 레파지토리 이용 페이징 메소드
    public List findByPage(int age, int offset, int limit) {
        return em.createQuery("select m from Member m where m.age = :age order by m.username desc")
                        .setParameter("age", age)
                        .setFirstResult(offset)
                        .setMaxResults(limit)
                        .getResultList();
    }

    //해당 나이에 해당하는 멤버의 전체 카운트 조회
    public long totalCount(int age) {
        return em.createQuery("select count(m) from Member m where m.age = :age",
                Long.class)
                .setParameter("age", age)
                .getSingleResult();
    }

    //JPA를 사용한 벌크성 수정 쿼리
    public int bulkAgePlus(int age) {
        int resultCount = em.createQuery(
                "update Member m set m.age = m.age + 1" +
                        "where m.age >= :age")
                .setParameter("age", age)
                .executeUpdate();
        return resultCount;
    }

}
