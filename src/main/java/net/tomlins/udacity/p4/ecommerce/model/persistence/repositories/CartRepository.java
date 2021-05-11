package net.tomlins.udacity.p4.ecommerce.model.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import net.tomlins.udacity.p4.ecommerce.model.persistence.Cart;
import net.tomlins.udacity.p4.ecommerce.model.persistence.User;

public interface CartRepository extends JpaRepository<Cart, Long> {
	Cart findByUser(User user);
}
