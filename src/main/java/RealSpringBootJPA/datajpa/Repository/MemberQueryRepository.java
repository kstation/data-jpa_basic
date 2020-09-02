package RealSpringBootJPA.datajpa.Repository;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class MemberQueryRepository {

    @PersistenceContext
    private EntityManager em;

    List findAllMembers(){
        return em.createQuery("select m from Member m")
                .getResultList();

    }
}
