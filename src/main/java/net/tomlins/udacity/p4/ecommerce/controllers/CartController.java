package net.tomlins.udacity.p4.ecommerce.controllers;

import net.tomlins.udacity.p4.ecommerce.model.persistence.Cart;
import net.tomlins.udacity.p4.ecommerce.model.persistence.Item;
import net.tomlins.udacity.p4.ecommerce.model.persistence.User;
import net.tomlins.udacity.p4.ecommerce.model.persistence.repositories.CartRepository;
import net.tomlins.udacity.p4.ecommerce.model.persistence.repositories.ItemRepository;
import net.tomlins.udacity.p4.ecommerce.model.persistence.repositories.UserRepository;
import net.tomlins.udacity.p4.ecommerce.model.requests.ModifyCartRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/api/cart")
public class CartController {

	private static final Logger LOG = LoggerFactory.getLogger(CartController.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private ItemRepository itemRepository;
	
	@PostMapping("/addToCart")
	public ResponseEntity<Cart> addTocart(@RequestBody ModifyCartRequest request) {

		User user = userRepository.findByUsername(request.getUsername());
		if(user == null) {
			LOG.info("addToCart : User {} not found", request.getUsername());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		Optional<Item> item = itemRepository.findById(request.getItemId());
		if(!item.isPresent()) {
			LOG.info("addToCart : Item id {} not found", request.getItemId());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		Cart cart = user.getCart();
		IntStream.range(0, request.getQuantity())
			.forEach(i -> cart.addItem(item.get()));

		cartRepository.save(cart);
		LOG.info("addToCart : Item id {}, qty {} saved to {}'s cart",
				request.getItemId(), request.getQuantity(), request.getUsername());

		return ResponseEntity.ok(cart);
	}
	
	@PostMapping("/removeFromCart")
	public ResponseEntity<Cart> removeFromcart(@RequestBody ModifyCartRequest request) {
		User user = userRepository.findByUsername(request.getUsername());
		if(user == null) {
			LOG.info("removeFromcart : User {} not found", request.getUsername());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		Optional<Item> item = itemRepository.findById(request.getItemId());
		if(!item.isPresent()) {
			LOG.info("removeFromcart : Item id {} not found", request.getItemId());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		Cart cart = user.getCart();
		IntStream.range(0, request.getQuantity())
			.forEach(i -> cart.removeItem(item.get()));
		cartRepository.save(cart);
		LOG.info("removeFromcart : Item id {} removed from {}'s cart", request.getItemId(), request.getUsername());

		return ResponseEntity.ok(cart);
	}
		
}
