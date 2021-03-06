package net.tomlins.udacity.p4.ecommerce.model.persistence.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import net.tomlins.udacity.p4.ecommerce.model.persistence.User;
import net.tomlins.udacity.p4.ecommerce.model.persistence.UserOrder;

public interface OrderRepository extends JpaRepository<UserOrder, Long> {
	List<UserOrder> findByUser(User user);
}
