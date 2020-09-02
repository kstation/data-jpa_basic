package RealSpringBootJPA.datajpa.Repository;

import RealSpringBootJPA.datajpa.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

//JPADataRepository 상속
public interface TeamRepository extends JpaRepository<Team, Long> {
}
