package net.tomlins.udacity.p4.ecommerce.controllers;

import net.tomlins.udacity.p4.ecommerce.model.persistence.Cart;
import net.tomlins.udacity.p4.ecommerce.model.persistence.Item;
import net.tomlins.udacity.p4.ecommerce.model.persistence.User;
import net.tomlins.udacity.p4.ecommerce.model.persistence.UserOrder;
import net.tomlins.udacity.p4.ecommerce.model.persistence.repositories.OrderRepository;
import net.tomlins.udacity.p4.ecommerce.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderControllerTest {

    @InjectMocks
    private OrderController orderController = new OrderController();

    @Mock
    private UserRepository userRepository;
    @Mock
    private OrderRepository orderRepository;

    private Cart cart = new Cart();
    private Item item = new Item();
    private User user = new User();
    private UserOrder userOrder = new UserOrder();

    @Before
    public void setUp() {
        // Create the test user
        user.setUsername("testUser");
        user.setPassword("testPassword");

        // Create the Cart
        cart.setId(1L);
        user.setCart(cart);
        cart.setUser(user);

        // Create an item for the cart
        item.setId(1L);
        item.setName("apples");
        item.setDescription("Red rosy apples");
        item.setPrice(BigDecimal.valueOf(5.00));

        cart.addItem(item);

        userOrder.setId(1L);
        userOrder.setUser(user);
        userOrder.setItems(Arrays.asList(item));
        userOrder.setTotal(new BigDecimal(5));

        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        when(orderRepository.findByUser(user)).thenReturn(Arrays.asList(userOrder));

    }

    @Test
    public void submitOrder_success() {
        ResponseEntity<UserOrder> response = orderController.submit("testUser");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        UserOrder userOrder = response.getBody();
        assertEquals("testUser", userOrder.getUser().getUsername());
        assertEquals(5.0, userOrder.getTotal().doubleValue(), 0);
    }

    @Test
    public void submitOrder_fail() {
        ResponseEntity<UserOrder> response = orderController.submit("invalidUser");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void getOrdersForuser_success() {
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("testUser");
        assertNotNull(response);

        // Get only UserOrder from list
        List<UserOrder> userOrders = response.getBody();
        assertEquals(1, userOrders.size());
        UserOrder onlyUserOrder = userOrders.get(0);

        // Assert that the only item in the UserOrder is 'apples'
        assertEquals("apples", onlyUserOrder.getItems().get(0).getName());

    }

    @Test
    public void getOrdersForuser_fail() {
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("invalidUser");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

}
