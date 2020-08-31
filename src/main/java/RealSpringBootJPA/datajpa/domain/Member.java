package RealSpringBootJPA.datajpa.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter@Setter
public class Member {
    @Id @GeneratedValue
    private Long Id;
    private String username;

    protected Member(){}

    public Member(String username){
        this.username = username;
    }
}


