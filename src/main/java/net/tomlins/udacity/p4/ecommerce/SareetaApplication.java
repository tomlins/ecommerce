package net.tomlins.udacity.p4.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableJpaRepositories("net.tomlins.udacity.p4.ecommerce.model.persistence.repositories")
@EntityScan("net.tomlins.udacity.p4.ecommerce.model.persistence")

// Excluded because we are using JWT
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})


public class SareetaApplication {
	/**
	 * bCrypt is generally used to generate the hash for user-passwords
	 * and is based on the Blowfish cipher algorithm
	 * @return BCryptPasswordEncoder
	 */
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder(){
		return new BCryptPasswordEncoder();
	}
	public static void main(String[] args) {
		SpringApplication.run(SareetaApplication.class, args);
	}

}
