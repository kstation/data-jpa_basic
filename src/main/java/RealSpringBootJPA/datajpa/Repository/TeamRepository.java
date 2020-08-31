package RealSpringBootJPA.datajpa.Repository;

import RealSpringBootJPA.datajpa.entity.Member;
import RealSpringBootJPA.datajpa.entity.Team;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class TeamRepository {

    //JPA의 EntityManager를 Injection해주는 Annotation
    @PersistenceContext
    private EntityManager em;

    public Team save(Team team){
        em.persist(team);
        return team;
    }

    public void delete(Team team){
        em.remove(team);
    }

    public List<Team> findAll(){
        return em.createQuery("select t from Team t", Team.class).getResultList();
    }

    //Team null이기에 Optional 조회
    public Optional<Team> findById(Long id){
        Team team = em.find(Team.class, id);
        return Optional.ofNullable(team);
    }

    //RowCount조회
    public long count(){
        return em.createQuery("select count(t) from Team t", Long.class).getSingleResult();
    }

    public Team find(Long id){
        return em.find(Team.class, id);
    }
}
