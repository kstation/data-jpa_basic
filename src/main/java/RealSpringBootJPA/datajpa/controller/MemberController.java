package RealSpringBootJPA.datajpa.controller;

import RealSpringBootJPA.datajpa.Repository.MemberRepository;
import RealSpringBootJPA.datajpa.dto.MemberDto;
import RealSpringBootJPA.datajpa.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id) {
       Member member = memberRepository.findById(id).get();
       return member.getUsername();
    }


    //도메인 클래스 컨버터
    //HTTP 요청은 회원 id 를 받지만 도메인 클래스 컨버터가 중간에 동작해서 회원 엔티티 객체를 반환
    //도메인 클래스 컨버터도 리파지토리를 사용해서 엔티티를 찾음
    //도메인 클래스 컨버터로 엔티티를 파라미터로 받으면, 이 엔티티는 단순 조회용으로만 사용해야 한다.
    @GetMapping("/members2/{id}")
    public String findMember2(@PathVariable("id") Member member) {
        return member.getUsername();
    }

    // http://localhost:8080/members?page=1 page =2 page 3....&size=10&sort=id,desc
    //글로벌 설정을 통한 pagation은 application.properties or application.yml을 수정
    @GetMapping("/members")
    public Page<Member> list(Pageable pageable){
        Page<Member> page = memberRepository.findAll(pageable);
        return page;
    }

    //개별 설정을 통한 pagnation  설정
    @GetMapping("/members2")
    public Page<Member> list2(@PageableDefault(size=5, sort="username") Pageable pageable){
        Page<Member> page = memberRepository.findAll(pageable);
        return page;
    }

    @GetMapping("/members3")
    public Page<MemberDto> list3(Pageable pageable) {
        Page<Member> page = memberRepository.findAll(pageable);

        Page<MemberDto> map = page.map(member -> new MemberDto(member.getId(), member.getUsername(), null));
        //Page<MemberDto> map = page.map(MemberDto::new);
        return map;
    }

    //스크링 부팅시 한번 실행
    @PostConstruct
    public void init(){
        for ( int i=0; i < 100 ; i++ ){
            memberRepository.save(new Member("user" + i, i));    
        }
    }
}
