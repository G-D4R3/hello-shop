package jpabook.jpashop.controller;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
// Validation 추가하기
public class BookForm {

    private Long id; // 상품 수정이 있기 때문

    private String name;
    private int price;
    private int stockQuantity;

    private String author;
    private String isbn;
}
