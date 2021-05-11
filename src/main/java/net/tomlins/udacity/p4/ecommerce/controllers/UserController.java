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
		return ResponseEntity.of(userRepository.findById(id));
	}
	
	@GetMapping("/{username}")
	public ResponseEntity<User> findByUserName(@PathVariable String username) {
		User user = userRepository.findByUsername(username);
		return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
	}
	
	@PostMapping("/create")
	public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {

		User user = new User();
		user.setUsername(createUserRequest.getUsername());
		LOG.debug("username set to ", createUserRequest.getUsername());
		LOG.warn("username set to ", createUserRequest.getUsername());
		LOG.error("username set to ", createUserRequest.getUsername());
		LOG.info("username set to ", createUserRequest.getUsername());

		/**
		 * TODO:
		 * This looks dodgy. We're creating and saving a cart to the database before
		 * we have verified the password is acceptable and save the user.
		 */
		Cart cart = new Cart();
		cartRepository.save(cart);
		user.setCart(cart);

		if(createUserRequest.getPassword().length()<7
				|| !createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword())){
			return ResponseEntity.badRequest().build();
		}

		// The encode method also adds salt
		user.setPassword(bCryptPasswordEncoder.encode(createUserRequest.getPassword()));

		userRepository.save(user);
		return ResponseEntity.ok(user);
	}
	
}
