package com.tus.accounts.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.tus.accounts.dto.AccountsDto;
import com.tus.accounts.dto.CustomerDto;
import com.tus.accounts.entity.Accounts;
import com.tus.accounts.entity.Customer;
import com.tus.accounts.exception.CustomerAlreadyExistsException;
import com.tus.accounts.mapper.AccountsMapper;
import com.tus.accounts.mapper.CustomerMapper;
import com.tus.accounts.repository.AccountsRepository;
import com.tus.accounts.repository.CustomerRepository;

@ExtendWith(MockitoExtension.class)
class AccountsServiceImplTests {
	// Mock Service
	@Mock
    private AccountsRepository accountsRepository;
	@Mock
    private CustomerRepository customerRepository;
    // Class to be Tested
	@InjectMocks
    private AccountsServiceImpl accountsService;
    // Entity Class
    private AccountsDto accountsDto;
    private CustomerDto customerDto;
    private CustomerDto customerDto2;
    private Accounts accounts;
    private Customer customer;
    // Constant Variables - AccountsDTO
    static final long ACCOUNT_NUMBER = 1234567890;
    static final String ACCOUNT_TYPE = "Savings";
    static final String BRANCH_ADDRESS = "123 Main Street, New York";
    // Constant Variables - CustomerDTO
    static final long CUSTOMER_ID = 1234567890;
    static final String NAME = "Katherine";
    static final String NAME2 = "Yan Jie";
    static final String EMAIL = "kathy.lee0356@gmail.com";
    static final String MOBILE_NUMBER = "0894949352";

    @BeforeEach
    public void setUp() {
        accountsDto = new AccountsDto();
        accountsDto.setAccountNumber(ACCOUNT_NUMBER);
        accountsDto.setAccountType(ACCOUNT_TYPE);
        accountsDto.setBranchAddress(BRANCH_ADDRESS);
        
        customerDto = new CustomerDto();
        customerDto.setName(NAME);
        customerDto.setEmail(EMAIL);
        customerDto.setMobileNumber(MOBILE_NUMBER);
        customerDto.setAccountsDto(accountsDto);
        
        customerDto2 = new CustomerDto();
        customerDto2.setName(NAME2);
        customerDto2.setEmail(EMAIL);
        customerDto2.setMobileNumber(MOBILE_NUMBER);
        customerDto2.setAccountsDto(accountsDto);
        
        accounts = AccountsMapper.mapToAccounts(accountsDto, new Accounts());
        accounts.setCustomerId(CUSTOMER_ID);
        customer = CustomerMapper.mapToCustomer(customerDto, new Customer());
        customer.setCustomerId(CUSTOMER_ID);
    }

    @Test
    public void testCreateAccountHappyPath() {
        // Setup
    	when(customerRepository.findByMobileNumber(MOBILE_NUMBER)).thenReturn(Optional.empty());
    	when(customerRepository.save(any())).thenReturn(customer);
        // Execute
    	accountsService.createAccount(customerDto);
        // Verify
    	verify(customerRepository, times(1)).save(any());
        verify(accountsRepository, times(1)).save(any());
    }

    @Test
    public void testFetchAccountHappyPath() {
        // Setup
    	when(customerRepository.findByMobileNumber(MOBILE_NUMBER)).thenReturn(Optional.of(customer));
        when(accountsRepository.findByCustomerId(CUSTOMER_ID)).thenReturn(Optional.of(accounts));
        // Execute
        CustomerDto result = accountsService.fetchAccount(MOBILE_NUMBER);
        // Verify
        assertNotNull(result);
        assertEquals(NAME, result.getName());
        assertEquals(EMAIL, result.getEmail());
        assertEquals(MOBILE_NUMBER, result.getMobileNumber());
        AccountsDto accountsDto = result.getAccountsDto();
        assertEquals(ACCOUNT_NUMBER, accountsDto.getAccountNumber());
        verify(customerRepository, times(1)).findByMobileNumber(MOBILE_NUMBER);
        verify(accountsRepository, times(1)).findByCustomerId(CUSTOMER_ID);
    }

    @Test
    public void testUpdateAccountHappyPath() {
        // Setup
        when(accountsRepository.findById(ACCOUNT_NUMBER)).thenReturn(Optional.of(accounts));
        when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.of(customer));
        when(accountsRepository.save(any())).thenReturn(accounts);

        // Execute
        boolean result = accountsService.updateAccount(customerDto);

        // Verify
        assertTrue(result);
        verify(accountsRepository, times(1)).findById(ACCOUNT_NUMBER);
        verify(accountsRepository, times(1)).save(accounts);
        verify(customerRepository, times(1)).findById(CUSTOMER_ID);
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    public void testDeleteAccountHappyPath() {
        // Setup
    	when(customerRepository.findByMobileNumber(MOBILE_NUMBER)).thenReturn(Optional.of(customer));
        // Execute
    	boolean result = accountsService.deleteAccount(MOBILE_NUMBER);
        // Verify
    	assertTrue(result);
        verify(accountsRepository, times(1)).deleteByCustomerId(CUSTOMER_ID);
        verify(customerRepository, times(1)).deleteById(CUSTOMER_ID);
    }

    @Test
    public void testCreateAccountCustomerAlreadyExists() {
        // Setup
    	when(customerRepository.findByMobileNumber(MOBILE_NUMBER)).thenReturn(Optional.of(customer));
        // Execute
    	Throwable exception = assertThrows(CustomerAlreadyExistsException.class, () -> accountsService.createAccount(customerDto));
        // Verify
    	assertEquals("Customer already registered with given mobile number " + MOBILE_NUMBER, exception.getMessage());
    	verify(customerRepository, times(0)).save(any());
        verify(accountsRepository, times(0)).save(any());
    }
}
