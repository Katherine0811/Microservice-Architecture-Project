package com.tus.loans.service.impl;
import com.tus.loans.constants.LoansConstants;
import com.tus.loans.dto.LoansDto;
import com.tus.loans.entity.Loans;
import com.tus.loans.exception.LoanAlreadyExistsException;
import com.tus.loans.exception.ResourceNotFoundException;
import com.tus.loans.mapper.LoansMapper;
import com.tus.loans.repository.LoansRepository;
import com.tus.loans.service.ILoansService;
import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.Random;


@Service
@AllArgsConstructor
public class LoansServiceImpl implements ILoansService{
	
	@Autowired
	 private LoansRepository loansRepository;
	 @Override
	    public void createLoan(String mobileNumber) {
	        Optional<Loans> optionalLoans= loansRepository.findByMobileNumber(mobileNumber);
	        if(optionalLoans.isPresent()){
	            throw new LoanAlreadyExistsException("Loan already registered with given mobileNumber "+mobileNumber);
	        }
	        loansRepository.save(createNewLoan(mobileNumber));
	    }

	    /**
	     * @param mobileNumber - Mobile Number of the Customer
	     * @return the new loan details
	     */
	    private Loans createNewLoan(String mobileNumber) {
	        Loans newLoan = new Loans();
	        long randomLoanNumber = 100000000000L + new Random().nextInt(900000000);
	        newLoan.setLoanNumber(Long.toString(randomLoanNumber));
	        newLoan.setMobileNumber(mobileNumber);
	        newLoan.setLoanType(LoansConstants.HOME_LOAN);
	        newLoan.setTotalLoan(LoansConstants.NEW_LOAN_LIMIT);
	        newLoan.setAmountPaid(0);
	        newLoan.setOutstandingAmount(LoansConstants.NEW_LOAN_LIMIT);
	        return newLoan;
	    }

	    /**
	     *
	     * @param mobileNumber - Input mobile Number
	     * @return Loan Details based on a given mobileNumber
	     */
	    @Override
	    public LoansDto fetchLoan(String mobileNumber) {
	        Loans loans = loansRepository.findByMobileNumber(mobileNumber).orElseThrow(
	                () -> new ResourceNotFoundException("Loan", "mobileNumber", mobileNumber)
	        );
	        return LoansMapper.mapToLoansDto(loans, new LoansDto());
	    }

	    /**
	     *
	     * @param loansDto - LoansDto Object
	     * @return boolean indicating if the update of loan details is successful or not
	     */
	    @Override
	    public boolean updateLoan(LoansDto loansDto) {
	        Loans loans = loansRepository.findByLoanNumber(loansDto.getLoanNumber()).orElseThrow(
	                () -> new ResourceNotFoundException("Loan", "LoanNumber", loansDto.getLoanNumber()));
	        LoansMapper.mapToLoans(loansDto, loans);
	        loansRepository.save(loans);
	        return  true;
	    }

	    /**
	     * @param mobileNumber - Input MobileNumber
	     * @return boolean indicating if the delete of loan details is successful or not
	     */
	    @Override
	    public boolean deleteLoan(String mobileNumber) {
	        Loans loans = loansRepository.findByMobileNumber(mobileNumber).orElseThrow(
	                () -> new ResourceNotFoundException("Loan", "mobileNumber", mobileNumber)
	        );
	        loansRepository.deleteById(loans.getLoanId());
	        return true;
	    }


	}