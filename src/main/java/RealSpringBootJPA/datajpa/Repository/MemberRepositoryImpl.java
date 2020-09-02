package RealSpringBootJPA.datajpa.Repository;

import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import java.util.List;

//Custom 인터페이스를 상속한 (이 샘플에서는 MemberRepositry) 이름 + Impl로 클래스명을 생성해야함.
//Spring Data JPA에서 스캔을 위한 필수 규칙
// 별도로 Postfix설정이 가능하나 웬만하면 규칙을 따르자.
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom{
    private final EntityManager em;

    @Override
    public List findMemberCustom(){
        return em.createQuery("select m from Member m")
                .getResultList();
    }
}
