package RealSpringBootJPA.datajpa.Repository;

import RealSpringBootJPA.datajpa.dto.MemberDto;
import RealSpringBootJPA.datajpa.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    //네임드 쿼리 직접 호출
    @Query(name = "Member.findByUsername")
    List<Member> findByUsernameUsageNamedQuery(@Param("username") String username);

    // 네임드 쿼리 호출
    // 스크링 데이터 JPA는 선언한 "도메인클래스 + .(점) + 메서드 이름으로" NameQuery를 찾아서 실행
    //만약 실행할 쿼리가 없다면 메서드 이름으로 쿼리 생성전략을 사용
    // 필요하면 전략을 변경할수 있지만 권장하지 않음/
    //참고: 스프링 데이터 JPA를 사용하면 실무에서 Named Query를 직접 등록해서 사용하는 일은 드물다.
    // 대신 @Query 를 사용해서 리파지토리 메소드에 쿼리를 직접 정의한다
    List<Member> findByUsername(@Param("username") String username);

    //메서드에 JPQL쿼리 직접 작성
    @Query("select m from Member m where m.username= :username and m.age = :age")
    List<Member> findUserAnnotationDirectQuery(@Param("username") String username, @Param("age") int age);


    //단순히 값 하나를 조회
    @Query("select m.username from Member m")
    List<String> findUsernameList();

    //Dto로 직접 조회
    @Query("select new RealSpringBootJPA.datajpa.dto.MemberDto(m.id, m.username, t.name)"
            + "from Member m join m.team t")
    List<MemberDto> findMemberDto();

    //파라미터 바인딩
    //select m from Member m where m.username = ?0 //위치 기반
    //select m from Member m where m.username = :name //이름 기반
    @Query("select m from Member m where m.username = :name")
    Member findMembers(@Param("name") String username);

    //컬렉션 파라미터 바인딩
    //Collection 타입으로 in절 지원
    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") List<String> names);

    //Page<Member> findByUsername(String name, Pageable pageable); //count 쿼리 사용
    //Slice<Member> findByUsername(String name, Pageable pageable); //count 쿼리 사용 안함
    //List<Member> findByUsername(String name, Pageable pageable); //count 쿼리 사용 안함
    //List<Member> findByUsername(String name, Sort sort);
    Page<Member> findByAge(int age, Pageable pageable);

    //스프링 데이터 JPA를 사용한 벌크성 수정
    //벌크성 수정, 삭제 쿼리는 @Modifying 어노테이션을 사용
    //사용하지 않으면 다음 예외 발생
    //org.hibernate.hql.internal.QueryExecutionRequestException: Not supported for
    //DML operations
    //벌크성 쿼리를 실행하고 나서 영속성 컨텍스트 초기화: @Modifying(clearAutomatically = true)
    //(이 옵션의 기본값은 false )
    //이 옵션 없이 회원을 findById 로 다시 조회하면 영속성 컨텍스트에 과거 값이 남아서 문제가 될 수
    //있다. 만약 다시 조회해야 하면 꼭 영속성 컨텍스트를 초기화 하자.
    //참고: 벌크 연산은 영속성 컨텍스트를 무시하고 실행하기 때문에, 영속성 컨텍스트에 있는 엔티티의 상태와
    //DB에 엔티티 상태가 달라질 수 있다.
    //> 권장하는 방안
    //> 1. 영속성 컨텍스트에 엔티티가 없는 상태에서 벌크 연산을 먼저 실행한다.
    //> 2. 부득이하게 영속성 컨텍스트에 엔티티가 있으면 벌크 연산 직후 영속성 컨텍스트를 초기화 한다.
    @Modifying
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    //N+1 문제를 해결하기 위한 페치 조인
    @Query("select m from Member m left join fetch m.team")
    List<Member> findMemberFetchJoin();

    //엔티티그래프를 활용한 패치 조인 
    // 아래는 Member조회 시 연관된 team도 같이 조회한다.
    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();
    
    //사실상 위  내용과 동일하나. 기본 제공되는 오버라이딩 가능한 메소드가 아닌
    //추가 검색일경우 fetch 조인을 이용하고 싶을 경우 아래와 같이 entitygraph annotation에 attribvutepaths를 이용하면 
    //쉽게 fetchjoin 가능
    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

    //NamedEntityGraph 사용
    @EntityGraph("Member.all")
    @Query("select m from Member m")
    List<Member> findMemberNamedEntityGraph();

    //query hint 사용
    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value =
            "true"))
    Member findReadOnlyByUsername(String username);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUsername(String username);
}