package jpabook.jpashop.domain;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable // 값타입
@Getter //  값타입은 값이 변경되면 안된다.
public class Address {

    private String city;
    private String street;
    private String zipcode;

    protected Address() {
        // JPA 기본 스펙이 reflection이나 proxy등을 써야되는 경우가 많은데 이 때, 기본생성자가 필요하기 때문
        // 따라서 protected로 정의해서 다른데서 막 사용하지 못하도록 표시
    }

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
