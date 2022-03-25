package jpabook.jpashop.api;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderSearch;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * X to One (Many To One과 One To One에서의 성능 최적화)
 * Order 조회
 * Order -> Member
 * Order -> Delivery 연관
 *
 */

@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;

    @GetMapping("/api/v1/sample-orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch()); // Order -> Member -> orders -> member -> ... 무한 루프로 에러 따라서 한 쪽에 JsonIgnore를 걸지 않으면 해결이 x
        // 양방향으로 걸려있는 연관관계는 @JsonIgnore을 통해 끊어줘야함
        return all;
    }

}
