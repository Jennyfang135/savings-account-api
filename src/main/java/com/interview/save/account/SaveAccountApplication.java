package com.interview.save.account;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SaveAccountApplication
{
	public static void main(String[] args)
	{
		SpringApplication.run(SaveAccountApplication.class, args);
	}
}
