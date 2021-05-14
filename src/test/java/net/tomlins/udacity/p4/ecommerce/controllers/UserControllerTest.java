package net.tomlins.udacity.p4.ecommerce.controllers;

import net.tomlins.udacity.p4.ecommerce.model.persistence.User;
import net.tomlins.udacity.p4.ecommerce.model.persistence.repositories.CartRepository;
import net.tomlins.udacity.p4.ecommerce.model.persistence.repositories.UserRepository;
import net.tomlins.udacity.p4.ecommerce.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    @InjectMocks
    private UserController userController = new UserController();
    @Mock
    private UserRepository userRepository;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private BCryptPasswordEncoder encoder;

    @Before
    public void setUp() {
        User testUser = new User();
        testUser.setId(0);
        testUser.setUsername("testUser");
        testUser.setPassword("testPassword");
        when(userRepository.findById(0L)).thenReturn(java.util.Optional.of(testUser));
        when(userRepository.findByUsername("testUser")).thenReturn(testUser);
    }

    @Test
    public void createUser_HappyPath() {
        when(encoder.encode("testPassword")).thenReturn("thisIsHashed");
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("testUser");
        userRequest.setPassword("testPassword");
        userRequest.setConfirmPassword("testPassword");

        final ResponseEntity<User> response = userController.createUser(userRequest);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User user = response.getBody();
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals("testUser", user.getUsername());
        assertEquals("thisIsHashed", user.getPassword());
    }

    @Test
    public void createUser_invalidPassword_toShort() {
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("testUser");
        userRequest.setPassword("123456");
        userRequest.setConfirmPassword("123456");

        final ResponseEntity<User> response = userController.createUser(userRequest);
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());

    }

    @Test
    public void createUser_invalidPassword_doNotMatch() {
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("testUser");
        userRequest.setPassword("1234567");
        userRequest.setConfirmPassword("7654321");

        final ResponseEntity<User> response = userController.createUser(userRequest);
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());

    }
    @Test
    public void findUserById_success() {
        ResponseEntity<User> user = userController.findById(0L);
        assertEquals(0, user.getBody().getId());
    }

    @Test
    public void findUserById_fail() {
        ResponseEntity<User> user = userController.findById(1L);
        assertEquals(404, user.getStatusCodeValue());
    }

    @Test
    public void findUserByUserName_success() {
        ResponseEntity<User> user = userController.findByUserName("testUser");
        assertEquals("testUser", user.getBody().getUsername());
    }

    @Test
    public void findUserByUserName_fail() {
        ResponseEntity<User> user = userController.findByUserName("blahblah");
        assertEquals(404, user.getStatusCodeValue());

    }
}
