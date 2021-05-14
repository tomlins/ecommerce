package net.tomlins.udacity.p4.ecommerce.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.tomlins.udacity.p4.ecommerce.model.persistence.Item;
import net.tomlins.udacity.p4.ecommerce.model.persistence.repositories.ItemRepository;

@RestController
@RequestMapping("/api/item")
public class ItemController {

	private static final Logger LOG = LoggerFactory.getLogger(ItemController.class);

	@Autowired
	private ItemRepository itemRepository;
	
	@GetMapping
	public ResponseEntity<List<Item>> getItems() {
		LOG.info("getItems : User requesting all items");
		return ResponseEntity.ok(itemRepository.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Item> getItemById(@PathVariable Long id) {
		LOG.info("getItemById : User requesting item Id, {}", id);
		return ResponseEntity.of(itemRepository.findById(id));
	}
	
	@GetMapping("/name/{name}")
	public ResponseEntity<List<Item>> getItemsByName(@PathVariable String name) {
		LOG.info("getItemsByName : User requesting item, {}", name);
		List<Item> items = itemRepository.findByName(name);

		if (items == null || items.isEmpty()) {
			LOG.info("getItemsByName : Item {} does not exist!", name);
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok(items);
	}
	
}
