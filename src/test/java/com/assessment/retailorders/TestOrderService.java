package com.assessment.retailorders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import com.assessment.retailorders.constant.OrderConstants;
import com.assessment.retailorders.entity.Order;
import com.assessment.retailorders.exception.OrderNotFoundException;
import com.assessment.retailorders.repository.OrderRepository;
import com.assessment.retailorders.service.OrderService;


@SpringBootTest
@TestPropertySource("classpath:application.properties")
public class TestOrderService {
	
	@MockBean
    private OrderRepository orderRepository;

    @MockBean
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private OrderService orderService;

    private Order order;
    
    @BeforeEach
    void setUp() {
    	order = new Order();
    	order.setId(1L);
    	order.setCustomerName("Bob");
    	order.setOrderDate(new Date());
    	order.setStatus("PENDING");
    	order.setTotalAmount(300.00D);
    }
    
    @Test
    void testGetAllOrders() {
    	List<Order> orders = Arrays.asList(order);
    	when(orderRepository.findAll()).thenReturn(orders);
    	List<Order> allOrders = orderService.getAllOrders();
    	assertNotNull(allOrders);
    	assertEquals(1, allOrders.size());
    	assertEquals(order, allOrders.get(0));
    }
    
    @Test
    void testGetOrderById() {
    	when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
    	Order orderById = orderService.getOrderById(1L);
    	assertNotNull(orderById);
    	assertEquals(order, order);
    	assertEquals("PENDING", order.getStatus());
    }
    
    @Test 
    void testGetOrderByIdNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(OrderNotFoundException.class, () -> orderService.getOrderById(10L));
        verify(orderRepository, times(1)).findById(10L);
    }
    
    @Test
    public void testSaveOrder() {
    	Order newOrder = new Order(2L, "Alice", new Date(), 500.25D, "CREATED");
        when(orderRepository.save(any(Order.class))).thenReturn(newOrder);
        Order result = orderService.saveOrder(newOrder);
        assertNotNull(result);
        assertEquals("Alice", result.getCustomerName());
        verify(rabbitTemplate, times(1)).convertAndSend(OrderConstants.ORDER_EXCHANGE, OrderConstants.ORDER_CREATED_ROUTE, newOrder.getId());
    }
    
    @Test
    void testUpdateOrder() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        order.setCustomerName("Bob");
        order.setTotalAmount(350.00D);
        Order result = orderService.updateOrder(1L, order);
        assertNotNull(result);
        assertEquals("Bob", result.getCustomerName());
        assertEquals(350.00D, result.getTotalAmount());
        verify(orderRepository, times(1)).findById(1L);
        verify(orderRepository, times(1)).save(any(Order.class));
    }
    
    @Test
    public void testUpdateOrderNotFound() {
        when(orderRepository.findById(5L)).thenReturn(Optional.empty());
        assertThrows(OrderNotFoundException.class, () -> orderService.updateOrder(5L, order));
        verify(orderRepository, times(1)).findById(5L);
    }
    
    @Test
    public void testDeleteOrderById() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        orderService.deleteOrderById(1L);
        verify(orderRepository, times(1)).deleteById(1L);
    }
    
    @Test
    public void testDeleteOrderNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(OrderNotFoundException.class, () -> orderService.deleteOrderById(1L));
        verify(orderRepository, times(1)).findById(1L);
    }
}
