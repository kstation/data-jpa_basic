package RealSpringBootJPA.datajpa.Repository;

import RealSpringBootJPA.datajpa.entity.Member;
import RealSpringBootJPA.datajpa.entity.Team;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
public class MemberRepositoryTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TeamRepository teamRepository;

    @Test
    public void testMember(){
        System.out.println("*******memberRepository=>" + memberRepository.getClass());
        Member member = new Member("memberB");
        Member savedMember = memberRepository.save(member);
        Member findMember =
                memberRepository.findById(savedMember.getId()).get();
        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
        Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        Assertions.assertThat(findMember).isEqualTo(member); //JPA 엔티티 동일성 보장
    }

    //순수 JPA 기반 레파지토리 테스트와 동일한 코드지만 잘 동작
    @Test
    public void basicCRUD(){
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);
        //단건 조회 검증
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        Assertions.assertThat(findMember1).isEqualTo(member1);
        Assertions.assertThat(findMember2).isEqualTo(member2);
        //리스트 조회 검증
        List<Member> all = memberRepository.findAll();
        Assertions.assertThat(all.size()).isEqualTo(2);
        //카운트 검증
        long count = memberRepository.count();
        Assertions.assertThat(count).isEqualTo(2);
        //삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);
        long deletedCount = memberRepository.count();
        Assertions.assertThat(deletedCount).isEqualTo(0);
    }

    @Test
    public void findByUsernameAndAgeGreaterThan() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);
        List<Member> result =
                memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);
        Assertions.assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        Assertions.assertThat(result.get(0).getAge()).isEqualTo(20);
        Assertions.assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void findByUsernameUsageNamedQueryTest(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        //스프링데이터 JPA로 NamedQuery 사용
        List<Member> result = memberRepository.findByUsernameUsageNamedQuery("AAA");
        Assertions.assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        Assertions.assertThat(result.size()).isEqualTo(2);

        //스프링 데이터 JPA로 Named 쿼리 호출
        List<Member> result2 = memberRepository.findByUsername("AAA");
        Assertions.assertThat(result2.get(0).getUsername()).isEqualTo("AAA");
        Assertions.assertThat(result2.size()).isEqualTo(2);
    }

    @Test
    public void findUserAnnotationDirectQuery() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        //스프링데이터 JPA로 NamedQuery 사용
        List<Member> result = memberRepository.findUserAnnotationDirectQuery("AAA", 10);
        Assertions.assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        Assertions.assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void findUsernameList() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        //스프링데이터 JPA로 NamedQuery 사용
        List<String> result = memberRepository.findUsernameList();
        Assertions.assertThat(result.get(0)).isEqualTo("AAA");
        Assertions.assertThat(result.get(1)).isEqualTo("BBB");
        Assertions.assertThat(result.size()).isEqualTo(2);
    }

    @Test
    public void findMemberDto() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        //스프링데이터 JPA로 NamedQuery 사용
        List<MemberDto> result = memberRepository.findMemberDto();
        Assertions.assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        Assertions.assertThat(result.get(2).getUsername()).isEqualTo("BBB");
        Assertions.assertThat(result.size()).isEqualTo(2);

    }

    @Test
    public void findMembers() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        Member result = memberRepository.findMembers("AAA");
        Assertions.assertThat(result.getUsername()).isEqualTo("AAA");
        Assertions.assertThat(result.getAge()).isEqualTo(10);

    }

    @Test
    public void findByNames() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<String> response = new ArrayList<>();
        response.add("AAA");
        response.add("BBB");
        List<Member> result = memberRepository.findByNames(response);
        Assertions.assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        Assertions.assertThat(result.get(1).getUsername()).isEqualTo("BBB");
        Assertions.assertThat(result.size()).isEqualTo(2);

    }

    //검색 조건: 나이가 10살
    //정렬 조건: 이름으로 내림차순
    //페이징 조건: 첫 번째 페이지, 페이지당 보여줄 데이터는 3건
    //두 번째 파라미터로 받은 Pagable 은 인터페이스다. 따라서 실제 사용할 때는 해당 인터페이스를 구현한
    //org.springframework.data.domain.PageRequest 객체를 사용한다.
    //PageRequest 생성자의 첫 번째 파라미터에는 현재 페이지를, 두 번째 파라미터에는 조회할 데이터 수를
    //입력한다. 여기에 추가로 정렬 정보도 파라미터로 사용할 수 있다. 참고로 페이지는 0부터 시작한다.
    //> 주의: Page는 1부터 시작이 아니라 0부터 시작이다.
    @Test
    public void findByAge() throws Exception{
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));
        //when
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC,
                "username"));
        Page<Member> page = memberRepository.findByAge(10, pageRequest);
        //then
        List<Member> content = page.getContent(); //조회된 데이터
        Assertions.assertThat(content.size()).isEqualTo(3); //조회된 데이터 수
        Assertions.assertThat(page.getTotalElements()).isEqualTo(5); //전체 데이터 수
        Assertions.assertThat(page.getNumber()).isEqualTo(0); //페이지 번호
        Assertions.assertThat(page.getTotalPages()).isEqualTo(2); //전체 페이지 번호
        Assertions.assertThat(page.isFirst()).isTrue(); //첫번째 항목인가?
        Assertions.assertThat(page.hasNext()).isTrue(); //다음 페이지가 있는가?
    }

    @Test
    public void bulkUpdate() throws Exception {
//given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 19));
        memberRepository.save(new Member("member3", 20));
        memberRepository.save(new Member("member4", 21));
        memberRepository.save(new Member("member5", 40));
//when
        int resultCount = memberRepository.bulkAgePlus(20);
        //em.flush();
       // em.clear();
        List<Member> result = memberRepository.findByUsername("member5");

        Member member5 = result.get(0);
        System.out.println("member5 = " + member5);
//then
        Assertions.assertThat(resultCount).isEqualTo(3);
    }

    @Test
    public void findMemberLazy() throws Exception {
        //given
        //member1 -> teamA
        //member2 -> teamB
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);
        memberRepository.save(new Member("member1", 10, teamA));
        memberRepository.save(new Member("member2", 20, teamB));

        //영속성 컨텍스트 개체 DB 반영후 완전 초기화
        em.flush();
        em.clear();
        //when
        // Member 테이블만 Query 하여 값을 가져온다.
        // MemberQuery -> GetName()시 hibernateproxy에 빈 값이기 때문에 실제 DB 쿼리를 날려 팀 이름을 가지고 온다.
        // N + 1 문제 ( 쿼리를 한개 날렸으나 연관된 엔티티에 대한 쿼리가 N만큼 더 조회한다)
        // 이 문제를 해결하기 위해 FetchJoin을 통해 해결한다.
        List<Member> members = memberRepository.findAll();
        //then
        for (Member member : members) {
            System.out.println("******member = " + member.getUsername());
            System.out.println("******member.teamClass = " + member.getTeam().getClass());
            System.out.println("******member.team = " + member.getTeam().getName());
        }
    }

    @Test
    public void findMemberFetchJoin() {
        //given
        //member1 -> teamA
        //member2 -> teamB
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);
        memberRepository.save(new Member("member1", 10, teamA));
        memberRepository.save(new Member("member2", 20, teamB));

        //영속성 컨텍스트 개체 DB 반영후 완전 초기화
        em.flush();
        em.clear();
        //when
        // Member 테이블만 Query 하여 값을 가져온다.
        // MemberQuery -> GetName()시 hibernateproxy에 빈 값이기 때문에 실제 DB 쿼리를 날려 팀 이름을 가지고 온다.
        // N + 1 문제 ( 쿼리를 한개 날렸으나 연관된 엔티티에 대한 쿼리가 N만큼 더 조회한다)
        // 이 문제를 해결하기 위해 FetchJoin을 통해 해결한다.
        List<Member> members = memberRepository.findMemberFetchJoin();
        //then
        for (Member member : members) {
            System.out.println("******member = " + member.getUsername());
            System.out.println("******member.teamClass = " + member.getTeam().getClass());
            System.out.println("******member.team = " + member.getTeam().getName());
        }
    }

    @Test
    public void findMemberNamedEntityGraph() {
        //given
        //member1 -> teamA
        //member2 -> teamB
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);
        memberRepository.save(new Member("member1", 10, teamA));
        memberRepository.save(new Member("member2", 20, teamB));

        //영속성 컨텍스트 개체 DB 반영후 완전 초기화
        em.flush();
        em.clear();
        //when
        // Member 테이블만 Query 하여 값을 가져온다.
        // MemberQuery -> GetName()시 hibernateproxy에 빈 값이기 때문에 실제 DB 쿼리를 날려 팀 이름을 가지고 온다.
        // N + 1 문제 ( 쿼리를 한개 날렸으나 연관된 엔티티에 대한 쿼리가 N만큼 더 조회한다)
        // 이 문제를 해결하기 위해 FetchJoin을 통해 해결한다.
        List<Member> members = memberRepository.findMemberNamedEntityGraph();
        //then
        for (Member member : members) {
            System.out.println("******member = " + member.getUsername());
            System.out.println("******member.teamClass = " + member.getTeam().getClass());
            System.out.println("******member.team = " + member.getTeam().getName());
        }
    }

    @Test
    public void queryHint(){
        //given
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        //when
        // query hint로 readonly를 주었기 때문에 실제 setusername을 무시해버린다.
        Member findMember = memberRepository.findReadOnlyByUsername("member1");
        findMember.setUsername("member2");

        em.flush();
    }

    @Test
    public void findLockByUsername() {
        //given
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        List<Member> findMember = memberRepository.findLockByUsername("member1");
    }

    @Test
    public void callCustom(){
        List<Member> result = memberRepository.findMemberCustom();
    }
}