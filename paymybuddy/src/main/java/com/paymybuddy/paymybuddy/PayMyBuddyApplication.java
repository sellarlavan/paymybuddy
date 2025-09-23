package com.paymybuddy.paymybuddy;

import com.paymybuddy.paymybuddy.model.Users;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PayMyBuddyApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(PayMyBuddyApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Users u = new Users();
		u.setEmail("hello@gmail.com");

		System.out.println(u.getUsername());
	}
}
