package com.tus.accounts.service.impl;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tus.accounts.constants.AccountsConstants;
import com.tus.accounts.dto.CustomerDto;
import com.tus.accounts.entity.Accounts;
import com.tus.accounts.entity.Customer;
import com.tus.accounts.mapper.CustomerMapper;
import com.tus.accounts.repository.AccountsRepository;
import com.tus.accounts.repository.CustomerRepository;
import com.tus.accounts.service.IAccountsService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AccountsServiceImpl implements IAccountsService{

	// framework will automatically autowire as one single constructor
	@Autowired
	private AccountsRepository accountsRepository;
	@Autowired
	private CustomerRepository customerRepository;
	
	@Override
	public void createAccount(CustomerDto customerDto) {
		Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());
		customer.setCreatedAt(LocalDateTime.now());
		customer.setCreatedBy("default");
		customer.setUpdatedAt(LocalDateTime.now());
		customer.setUpdatedBy("default");
		Customer savedCustomer = customerRepository.save(customer);
		accountsRepository.save(createNewAccount(savedCustomer));
	}

	private Accounts createNewAccount(Customer customer) {
		Accounts newAccount = new Accounts();
		newAccount.setCustomerId(customer.getCustomerId());;
		long randomAccNumber = 1000000000L + new Random().nextInt(900000000);
		
		newAccount.setAccountNumber(randomAccNumber);
		newAccount.setAccountType(AccountsConstants.SAVINGS);
		newAccount.setBranchAddress(AccountsConstants.ADDRESS);
		newAccount.setCreatedAt(LocalDateTime.now());
		newAccount.setCreatedBy("default");
		newAccount.setUpdatedAt(LocalDateTime.now());
		newAccount.setUpdatedBy("default");
		
		return newAccount;
	}

}
