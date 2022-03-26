package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderSearch;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSimpleQueryDto;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

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

    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch()); // Order -> Member -> orders -> member -> ... 무한 루프로 에러 따라서 한 쪽에 JsonIgnore를 걸지 않으면 해결이 x
        // 양방향으로 걸려있는 연관관계는 @JsonIgnore을 통해 끊어줘야함
        for (Order order : all) {
            order.getMember().getName(); //Lazy 강제 초기화
            order.getDelivery().getAddress(); //Lazy 강제 초기화
        }
        return all;
    }

    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> ordersV2() { // 여전히 LAZY loading으로 쿼리가 너무 많이 호출된다는 문제가 있다.
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());

        // order 하나 조회 당 2개의 쿼리가 추가로 날라감
        // order가 2개 -> 5개 쿼리 : O(1+N+N), (회원, 배송 N=2)
        // 지연로딩은 영속성 컨텍스트에서 조회하므로, 이미 조회된 경우 쿼리를 생략한다.
        List<SimpleOrderDto> result = orders.stream()
                .map(o -> new SimpleOrderDto(o)) // map(SimpleOrderDto::new)
                .collect(toList());
        return result;
    }

    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithMemberDelivery();

        List<SimpleOrderDto> result = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(toList());
        return result;
    }

    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDto> ordersV4() {

        /**
         * api spec이 repository에 들어가버렸다
         * v3까지는 사용해도 Entity의 순수성이 유지가 된다.
         * v4는 논리적으로는 계층이 깨지게 된다. => api를 고치면 repository 코드를 고쳐야 하기 때문
         * 하지만 성능상으로 v4가 좀 더 괜찮다.
         * trade off로 선택해야함.. 성능 테스트로 더 괜찮은 것을 고른다.
         * (대부분의 성능은 join에서 크게 걸린다. 지금은 별 차이가 없다.)
         */
        return orderRepository.findOrderDtos();
    }

    @Data
    static class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName(); // Member를 들고오는 쿼리 -> LAZY 초기화
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress(); // LAZY 초기화
        }
    }

}
