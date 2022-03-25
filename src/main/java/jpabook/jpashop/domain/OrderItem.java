package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jpabook.jpashop.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private Order order;

    private int orderPrice; // 주문 가격
    private int count; // 주문 수량

    /**protected OrderItem() {
        // OrderItem orderItem1 = new OrderItem();
        // orderItem1.setCount(count);
        // => 생성 로직에서 field를 추가한다거나 변경해야할 때 분산되는 것을 막기 위해 constructor로 다른 스타일의 생성로직을 막는다.
    }**/ // => Lombok : @NoArgsConstructor(access = AccessLevel.PROTECTED)

    //==생성 메서드==//
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice); // 상품 할인등을 고려해서 갖고있어야함
        orderItem.setCount(count);

        item.removeStock(count);
        return orderItem;
    }

    //== 비즈니스 로직==//
    public void cancel() {
        getItem().addStock(count); // 재고수량 원복
    }

    public int getTotalPrice() {
        return getOrderPrice()*getCount();
    }
}
