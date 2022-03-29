package jpabook.jpashop.repository.order.query; // 화면과 관련된 로직을 따로 분리


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

    private final EntityManager em;


    public List<OrderQueryDto> findOrderQueryDto() {
        List<OrderQueryDto> result = findOrders(); // query 1번 -> N개
        result.forEach(o-> {
            List<OrderItemQueryDto> orderItems = findOrderItems(o.getOrderId()); // query N번
            o.setOrderItems(orderItems);
        });
        return result;

    }

    private List<OrderItemQueryDto> findOrderItems(Long orderId) {
        return em.createQuery("select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
                " from OrderItem oi" +
                " join oi.item i" +
                " where oi.order.id = : orderId", OrderItemQueryDto.class) // order.id는 foreign key로 바로 가져올 수 있다.
                .setParameter("orderId", orderId)
                .getResultList();
    }

    private List<OrderQueryDto> findOrders() {
        return em.createQuery("select new jpabook.jpashop.repository.order.query.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
                        " from Order o" +
                        " join o.member m" +
                        " join o.delivery d", OrderQueryDto.class)
                .getResultList();
    }
}
