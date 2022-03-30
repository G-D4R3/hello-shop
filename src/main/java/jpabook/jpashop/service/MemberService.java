package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.MemberRepositoryOld;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) // readOnly = true하면 JPA가 조회하는 곳에서는 좀 더 성능을 최적화 한다.
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 회원 가입
      */
    @Transactional // 따로 설정하면 이게 우선권(default : readOnly = false)
    public Long join(Member member) {

        // 중복 회원 검증
        validateDuplicateMember(member);

        memberRepository.save(member);
        return member.getId();
        // em.persist하면 @GeneratedValue가 항상 생성되어있는게 보장된다. 따라서 아직 DB에 저장되지 않아도 ID를 받아올 수 있다.
    }

    private void validateDuplicateMember(Member member) {
        // 중복 회원이면 Exception
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }

        // 그럼에도 불구하고 멀티 스레드로 동작하면 같은 이름으로 동시에 조회할 수 있게 되므로 DB에 name에 unique를 걸어준다.
    }

    // 회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Member findOne(Long id) {
        return memberRepository.findById(id).get();
    }

    @Transactional
    public void update(Long id, String name) {
        Member member = memberRepository.findById(id).get(); // member는 영속성
        member.setName(name); // 변경감지 사용
    }
}
