package RealSpringBootJPA.datajpa.Repository;

import RealSpringBootJPA.datajpa.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

}