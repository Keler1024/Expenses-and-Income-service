package com.github.keler1024.expensesandincomeservice;

import com.github.keler1024.expensesandincomeservice.data.entities.Account;
import com.github.keler1024.expensesandincomeservice.data.entities.AccountChange;
import com.github.keler1024.expensesandincomeservice.data.entities.AccountChangeCategory;
import com.github.keler1024.expensesandincomeservice.data.enums.AccountChangeType;
import com.github.keler1024.expensesandincomeservice.data.enums.Currency;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

@SpringBootApplication
@RestController
public class ExpensesAndIncomeServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExpensesAndIncomeServiceApplication.class, args);
	}

	@GetMapping
	public List<AccountChange> hello() {
		return List.of(
				new AccountChange(
						new Account(1L, 300L, "debit card", Currency.RUB),
						AccountChangeType.EXPENSE,
						new AccountChangeCategory("Groceries", 1L),
						120L,
						LocalDateTime.of(
								2022,
								Month.AUGUST,
								20,
								17, 36
						),
						"Grocery Store",
						"Sweets"
				),
				new AccountChange(
						new Account(2L, 500L, "credit card", Currency.RUB),
						AccountChangeType.EXPENSE,
						new AccountChangeCategory("Fun", 2L),
						230L,
						LocalDateTime.of(
								2022,
								Month.SEPTEMBER,
								5,
								12, 3
						),
						"Game Store",
						"Video game"
				)
		);
	}
}
