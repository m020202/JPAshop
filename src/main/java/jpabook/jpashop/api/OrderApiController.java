package jpabook.jpashop.api;

import jdk.jshell.Snippet;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import static java.util.stream.Collectors.toList;

@RestController
@RequiredArgsConstructor
public class OrderApiController {
    private final OrderRepository orderRepository;

    @GetMapping("/api/v1/orders")
    public List<OrderDto> ordersV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        List<OrderDto> collect = all.stream().map(o -> new OrderDto(o)).collect(toList());
        return collect;
    }

    @GetMapping("/api/v2/orders")
    public Result ordersV2() {
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        List<OrderDto> result = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(toList());
        return new Result(result, result.size());
    }

    @GetMapping("/api/v3/orders")
    public List<OrderDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithItem();
        List<OrderDto> result = orders.stream().map(o -> new OrderDto(o)).collect(toList());
        return result;
    }
    @GetMapping("/api/v3.1/orders")
    public List<OrderDto> ordersV3_page(@RequestParam(value = "offset", defaultValue = "0") int offset,
                                        @RequestParam(value = "limit", defaultValue = "100") int limit)
    {
        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);
        List<OrderDto> result = orders.stream().map(o -> new OrderDto(o)).collect(toList());
        return result;
    }

    @Data
    static class OrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        private List<OrderItemDto> orderItems;
        public OrderDto(Order o) {
            orderId = o.getId();
            name = o.getMember().getName();
            orderDate = o.getOrderDate();
            address = o.getDelivery().getAddress();
            orderItems = o.getOrderItems().stream().map(oi -> new OrderItemDto(oi))
                    .collect(toList());
        }
    }

    @Data
    static class OrderItemDto {
        private String name;
        private int orderPrice;
        private int count;

        OrderItemDto(OrderItem orderItem) {
            this.name = orderItem.getItem().getName();
            orderPrice = orderItem.getOrderPrice();
            count = orderItem.getCount();
        }
    }

    @Data
    static class Result<T> {
        private T data;
        private int size;
        Result(T data, int size) {
            this.data = data;
            this.size = size;
        }
    }

//    @GetMapping("/api/v3/orders")
//    public List<OrderDTO> ordersV3(@RequestParam(value = "offset", defaultValue = "0") int offset,
//                                   @RequestParam(value = "limit", defaultValue = "100") int limit) {
//        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);
//        List<OrderDTO> result = orders.stream().map(o -> new OrderDTO(o)).collect(toList());
//        return result;
//    }


//    @Data
//    static class OrderDTO {
//        private Long id;
//        private String name;
//        private Address address;
//        private OrderStatus status;
//        private LocalDateTime orderDate;
//        private List<OrderItemDTO> items;
//
//        OrderDTO(Order o) {
//            id = o.getId();
//            name = o.getMember().getName();
//            address = o.getDelivery().getAddress();
//            status = o.getStatus();
//            orderDate = o.getOrderDate();
//            items = o.getOrderItems().stream().map(oi -> new OrderItemDTO(oi)).collect(Collectors.toList());
//        }
//    }
//
//    @Data
//    static class OrderItemDTO {
//        private Long id;
//        private String name;
//        private int price;
//        private int count;
//
//        OrderItemDTO(OrderItem orderItem) {
//            id = orderItem.getId();
//            name = orderItem.getItem().getName();
//            price = orderItem.getOrderPrice();
//            count = orderItem.getCount();
//        }
//    }
}

//@RestController
//@RequiredArgsConstructor
//public class OrderApiController {
//    private final OrderRepository orderRepository;
//    private final OrderQueryRepository orderQueryRepository;
//
//    @GetMapping("/api/v1/orders")
//    public List<Order> ordersV1() {
//        List<Order> all = orderRepository.findAllByString(new OrderSearch());
//        for (Order order : all) {
//            order.getMember().getName();
//            order.getDelivery().getAddress();
//            List<OrderItem> orderItems = order.getOrderItems();
//            for (OrderItem orderItem : orderItems) {
//                orderItem.getItem().getName();
//            }
//        }
//        return all;
//    }
//
//    @GetMapping("/api/v2/orders")
//    public List<OrderDTO>ordersV2() {
//        List<Order> all = orderRepository.findAllByString(new OrderSearch());
//        List<OrderDTO> collect = all.stream().map(o -> new OrderDTO(o)).collect(Collectors.toList());
//
//        return collect;
//    }
//
//    @GetMapping("/api/v3/orders")
//    public List<OrderDTO>ordersV3() {
//        List<Order> all = orderRepository.findAllWithItem();
//        List<OrderDTO> result = all.stream().map(o -> new OrderDTO(o)).collect(Collectors.toList());
//
//        return result;
//    }
//
//    @GetMapping("/api/v3.1/orders")
//    public List<OrderDTO>ordersV3_page(
//            @RequestParam(value = "offset", defaultValue = "0") int offset,
//            @RequestParam(value = "limit", defaultValue = "100") int limit)
//    {
//        List<Order> all = orderRepository.findAllWithMemberDelivery(offset,limit);
//        List<OrderDTO> result = all.stream().map(o -> new OrderDTO(o)).collect(Collectors.toList());
//
//        return result;
//    }
//
//    @GetMapping("/api/v4/orders")
//    public List<OrderQueryDTO>ordersV4() {
//        return orderQueryRepository.findOrderQueryDTOs();
//    }
//
//    @GetMapping("/api/v5/orders")
//    public List<OrderQueryDTO>ordersV5() {
//        return orderQueryRepository.findAllByDto_optimization();
//    }
//
//
//
//    @Data
//    @AllArgsConstructor
//    static class OrderDTO {
//        private Long id;
//        private String name;
//        private LocalDateTime orderDate;
//        private OrderStatus orderStatus;
//        private Address address;
//        private List<OrderItemDTO> items = new ArrayList<>();
//        public OrderDTO(Order order) {
//            id = order.getId();
//            name = order.getMember().getName();
//            orderDate = order.getOrderDate();
//            orderStatus = order.getStatus();
//            address = order.getDelivery().getAddress();
//
//            items = order.getOrderItems().stream()
//                    .map(o -> new OrderItemDTO(o)).collect(Collectors.toList());
//        }
//    }
//
//    @Data
//    @AllArgsConstructor
//    static class OrderItemDTO {
//        private String name;
//        private int price;
//        private int count;
//
//        public OrderItemDTO(OrderItem orderItem) {
//            name = orderItem.getItem().getName();
//            price = orderItem.getOrderPrice();
//            count = orderItem.getCount();
//        }
//    }
//}