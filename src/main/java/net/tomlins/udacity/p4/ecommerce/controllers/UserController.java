package net.tomlins.udacity.p4.ecommerce.controllers;

import net.tomlins.udacity.p4.ecommerce.model.persistence.Cart;
import net.tomlins.udacity.p4.ecommerce.model.persistence.User;
import net.tomlins.udacity.p4.ecommerce.model.persistence.repositories.CartRepository;
import net.tomlins.udacity.p4.ecommerce.model.persistence.repositories.UserRepository;
import net.tomlins.udacity.p4.ecommerce.model.requests.CreateUserRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

	private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@GetMapping("/id/{id}")
	public ResponseEntity<User> findById(@PathVariable Long id) {
		LOG.info("User requesting findById, {}", id);
		return ResponseEntity.of(userRepository.findById(id));
	}
	
	@GetMapping("/{username}")
	public ResponseEntity<User> findByUserName(@PathVariable String username) {
		LOG.info("User requesting findByUserName, {}", username);
		User user = userRepository.findByUsername(username);
		return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
	}
	
	@PostMapping("/create")
	public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {

		LOG.info("createUser requested...");

		// Refactored this method as the original project code put the User creation and Cart creation
		// BEFORE the password was validated!


		if(createUserRequest.getPassword().length()>=7) {
			if(!createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword())) {
				LOG.info("Chosen password does not match, {}, {}",
						createUserRequest.getPassword(), createUserRequest.getConfirmPassword());
				return ResponseEntity.badRequest().build();
			}
		} else {
			LOG.info("Chosen password does not meet minimum length, {}", createUserRequest.getPassword());
			return ResponseEntity.badRequest().build();
		}


		User user = new User();
		user.setUsername(createUserRequest.getUsername());

		// The encode method already adds salt
		user.setPassword(bCryptPasswordEncoder.encode(createUserRequest.getPassword()));

		Cart cart = new Cart();
		cartRepository.save(cart);
		user.setCart(cart);
		userRepository.save(user);

		LOG.info("User successfully created, {}", user.getUsername());
		return ResponseEntity.ok(user);
	}
	
}
