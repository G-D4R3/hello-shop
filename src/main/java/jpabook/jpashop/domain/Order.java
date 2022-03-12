package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="orders")
@Getter @Setter
public class Order {

    @Id @GeneratedValue
    @Column(name="order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id") // member_id의 값이 변경되면 member가 변경된다.
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL) // cascade = CascadeType.ALL : orderItems에 저장하면 order_item에도 저장
    private List<OrderItem> orderItems = new ArrayList<>();

    // persist(orderItemA)
    // persist(orderItemB)
    // persist(orderItemC)
    // 해야하는데 cascade = CascadeType.ALL 해놓으면 persist(order) 하면 위에도 다 됨

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id") // 연관관계의 주인을 order로 설정
    private Delivery delivery;

    private LocalDateTime orderDate; // 주문 시간

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문상태 [ORDER, CANCEL]

    //==연관관계 메서드==//

    /**
     * 양방향 연관관계 메소드는 control하는 쪽이 들고있으면 좋음
     */


    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    /**
     * ## setMember(Meberm member)로 아래 코드를 대체할 수 있음
     *
     * public static void main(String[] args) {
     *  Member member = new Member();
     *  Order order = new Order();
     *
     *  member.getOrders().add(order);
     *  order.setMember(member);
     * }
     *
     */

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }
}
