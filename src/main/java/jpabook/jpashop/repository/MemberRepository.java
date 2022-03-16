package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class MemberRepository {

    @PersistenceContext // JPA의 em을 스프링의 em으로 자동 주입해준다.
    // @Autowired로 바꿀 수 있다.
    private EntityManager em; // 얘도 constructor injection 가능

    public void save(Member member){
        em.persist(member);

        // generate 전략에서는 persist를 한다고 해서 insert 구문이 나가지 않는다.
        // transaction이 commit 될 때 flush되면서 insert 쿼리가 한번에 나간다.
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}
