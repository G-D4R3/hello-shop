package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name="member_id")
    private Long id;

    private String name;

    @Embedded
    private Address address;


    /**
     * 하이버네이트가 엔티티를 영속화할 때 컬렉션을 감싸서 하이버네이트가 제공하는 내장 컬렉션으로 변경함
     * 다른 임의의 메소드에서 컬렉션을 잘못 생성하면 하이버네이트 내부에서 문제가 생길 수도 있다.
     * => 컬렉션은 필드에서 바로 초기화
     */
    @OneToMany(mappedBy = "member") // Order table의 member field에 의해 mapping된 것이다. Order가 연관관계의 주인
    // @JsonIgnore 회원정보만 뿌릴 때.. 근데 Entity 자체를 수정하는 건 안좋음
    private List<Order> orders = new ArrayList<>(); // 초기화에 대해서 nullPointerException이 나는 고민을 하지 않아도 됨.


}
