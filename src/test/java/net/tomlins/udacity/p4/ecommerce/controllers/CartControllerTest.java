package net.tomlins.udacity.p4.ecommerce.controllers;

import net.tomlins.udacity.p4.ecommerce.model.persistence.Cart;
import net.tomlins.udacity.p4.ecommerce.model.persistence.Item;
import net.tomlins.udacity.p4.ecommerce.model.persistence.User;
import net.tomlins.udacity.p4.ecommerce.model.persistence.repositories.CartRepository;
import net.tomlins.udacity.p4.ecommerce.model.persistence.repositories.ItemRepository;
import net.tomlins.udacity.p4.ecommerce.model.persistence.repositories.UserRepository;
import net.tomlins.udacity.p4.ecommerce.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class CartControllerTest {

    @InjectMocks
    private CartController cartController = new CartController();
    @Mock
    private UserRepository userRepository;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private ItemRepository itemRepository;

    private ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
    private Cart cart = new Cart();


    @Before
    public void setUp() {
        // Create the test user
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("testPassword");

        // Create the Cart
        cart.setId(1L);
        user.setCart(cart);
        cart.setUser(user);

        // Create an item for the cart
        Item item = new Item();
        item.setId(1L);
        item.setName("apples");
        item.setDescription("Red rosy apples");
        item.setPrice(BigDecimal.valueOf(5.00));

        // Create the ModifyCartRequest for user and add 2x apples
        modifyCartRequest.setUsername("testUser");
        modifyCartRequest.setItemId((1));
        modifyCartRequest.setQuantity(2);

        when(userRepository.findByUsername(modifyCartRequest.getUsername())).thenReturn(user);
        when(itemRepository.findById(modifyCartRequest.getItemId())).thenReturn(java.util.Optional.of(item));

    }

    @Test
    public void addToCart_success() {
        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);
        assertNotNull(response);

        Cart responseCart = response.getBody();
        assertEquals(1L, cart.getId().longValue());
        assertEquals(2, responseCart.getItems().size());
        assertEquals(10.0, responseCart.getTotal().doubleValue(), 0);
    }

    @Test
    public void addToCart_fail() {
        modifyCartRequest.setUsername("unknownUser");
        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void removeFromCart_success() {
        // Add items to the cart first and assert total is 10
        Cart responseCart = cartController.addTocart(modifyCartRequest).getBody();
        assertEquals(10.0, responseCart.getTotal().doubleValue(), 0);

        // Now remove the items and assert total is now zero
        responseCart = cartController.removeFromcart(modifyCartRequest).getBody();
        assertEquals(0.0, responseCart.getTotal().doubleValue(), 0);
    }

    @Test
    public void removeFromCart_fail() {
        // Add items to the cart first and assert total is 10
        Cart responseCart = cartController.addTocart(modifyCartRequest).getBody();
        assertEquals(10.0, responseCart.getTotal().doubleValue(), 0);

        // Now remove an unknown item
        modifyCartRequest.setItemId(999);
        ResponseEntity<Cart> responseFail = cartController.removeFromcart(modifyCartRequest);
        assertEquals(404, responseFail.getStatusCodeValue());
    }

}
