package jpabook.jpashop.repository.order.query;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository { // 엔티티가 아닌 화면을 위한 API 처리가 필요할 때
    private final EntityManager em;

    public List<OrderQueryDTO> findOrderQueryDTOs() {
        List<OrderQueryDTO> result = findOrders();
        result.forEach(o -> {
            List<OrderItemQueryDTO> orderItems = findOrderItems(o.getId());
            o.setOrderItems(orderItems);
        });
        return result;
    }

    public List<OrderQueryDTO> findAllByDto_optimization() {
        List<OrderQueryDTO> result = findOrders();

        List<Long> orderIds = result.stream()
                .map(o -> o.getId())
                .collect(Collectors.toList());


        List<OrderItemQueryDTO> orderItems = em.createQuery(
                "select new jpabook.jpashop.repository.order.query.OrderItemQueryDTO(oi.order.id, i.name, oi.orderPrice, oi.count) from OrderItem oi join oi.item i where oi.order.id in :orderIds"
                , OrderItemQueryDTO.class
        ).setParameter("orderIds", orderIds).getResultList();

        Map<Long, List<OrderItemQueryDTO>> orderItemMap = orderItems.stream()
                .collect(Collectors.groupingBy(orderItemQueryDTO -> orderItemQueryDTO.getOrderId()));

        result.forEach(o -> o.setOrderItems(orderItemMap.get(o.getId())));
        return result;
    }

    private List<OrderItemQueryDTO> findOrderItems(Long id) {
        return em.createQuery(
                "select new jpabook.jpashop.repository.order.query.OrderItemQueryDTO(oi.order.id, i.name, oi.orderPrice, oi.count) from OrderItem oi join oi.item i where oi.order.id =:id"
                , OrderItemQueryDTO.class).setParameter("id", id)
                .getResultList();
    }

    private List<OrderQueryDTO> findOrders() {
        return em.createQuery("select new jpabook.jpashop.repository.order.query.OrderQueryDTO(o.id, m.name, o.orderDate, o.status, d.address) from Order o join o.member m join o.delivery d",
                OrderQueryDTO.class).getResultList();
    }

    public List<OrderFlatDTO> findAllByDto_flat() {
        return em.createQuery(
                "select new " +
                        "jpabook.jpashop.repository.order.query.OrderFlatDTO(o.id, m.name, o.orderDate, o.status, d.address, i.name, oi.orderPrice, oi.count)" +
                        "from Order o " +
                        "join o.member m " +
                        "join o.delivery d " +
                        "join o.orderItems oi " +
                        "join oi.item i", OrderFlatDTO.class)
                .getResultList();
    }
}
