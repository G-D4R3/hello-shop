package jpabook.jpashop.service;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.MemberRepositoryOld;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    // 주문
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {

        // 엔티티 조회
        Member member = memberRepository.findById(memberId).get(); // 원래는 getOr어쩌고 써야함
        Item item = itemRepository.findOne(itemId);

        // 배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());
        delivery.setStatus(DeliveryStatus.READY);

        // 주문 상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count); // 예제 단순화를 위해 item을 하나만 넘기도록 했다.

        // 주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        // 주문 저장
        orderRepository.save(order);
        // Cascade option에 의해 orderitem, delivery를 persist하지 않아도 된다.
        // orderItem, delivery는 order에서만 참조하기 때문에 cascade를 써도 괜찮다.

        return order.getId();
    }


    // 주문 취소
    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findOne(orderId);
        // order.setStatus(cancel); => 실수할 뻔. 그래서 setter를 없애나 보다.
        order.cancel();
    }

    // 주문 검색

    public List<Order> findOrders(OrderSearch orderSearch) {
        //return orderRepository.findAllByString(orderSearch);
        return orderRepository.findAllByQueryDsl(orderSearch);
    }
}
