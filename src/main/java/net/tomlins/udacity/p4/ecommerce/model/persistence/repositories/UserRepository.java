package net.tomlins.udacity.p4.ecommerce.model.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import net.tomlins.udacity.p4.ecommerce.model.persistence.User;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByUsername(String username);
}
