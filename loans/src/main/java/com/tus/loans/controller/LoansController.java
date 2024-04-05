package com.tus.loans.controller;

import com.tus.loans.constants.LoansConstants;
import com.tus.loans.dto.LoansContactInfoDto;
import com.tus.loans.dto.LoansDto;
import com.tus.loans.dto.ResponseDto;
import com.tus.loans.service.ILoansService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
//@AllArgsConstructor
@Validated
public class LoansController {
	@Autowired
	private ILoansService iLoansService;
	    
	public LoansController(ILoansService iLoansService) {
		this.iLoansService = iLoansService;
	}
	
	@Value("${build.version}")
	private String buildVersion;
	
	@Autowired
	private Environment environment;
	
	@Autowired
	private LoansContactInfoDto loansContactInfoDto;
		
	@GetMapping("/build-info")
	public ResponseEntity<String> getBuildInfo() {
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(buildVersion);
	}
	
	@GetMapping("/java-version")
	public ResponseEntity<String> getJavaVersion() {
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(environment.getProperty("JAVA_HOME"));
	}
	
	@GetMapping("/contact-info")
	public ResponseEntity<LoansContactInfoDto> getContactInfo() {
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(loansContactInfoDto);
	}
	
	@PostMapping("/loans")
	public ResponseEntity<ResponseDto> createLoan(@RequestParam @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits") String mobileNumber) {
		 iLoansService.createLoan(mobileNumber);
	     return ResponseEntity
	    		 .status(HttpStatus.CREATED)
	             .body(new ResponseDto(LoansConstants.STATUS_201, LoansConstants.MESSAGE_201));
	    }
	  
	@GetMapping("/loans")
	public ResponseEntity<LoansDto> fetchLoanDetails(@RequestParam @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits") String mobileNumber) {
        LoansDto loansDto = iLoansService.fetchLoan(mobileNumber);
        return ResponseEntity.status(HttpStatus.OK).body(loansDto);
    }
  
	@PutMapping("/loans")
    public ResponseEntity<ResponseDto> updateLoanDetails(@Valid @RequestBody LoansDto loansDto) {
        boolean isUpdated = iLoansService.updateLoan(loansDto);
        if(isUpdated) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(LoansConstants.STATUS_200, LoansConstants.MESSAGE_200));
        }else{
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(LoansConstants.STATUS_417, LoansConstants.MESSAGE_417_UPDATE));
        }
    }
	  
	@DeleteMapping("/loans")
	public ResponseEntity<ResponseDto> deleteLoanDetails(@RequestParam @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits") String mobileNumber) {
	    boolean isDeleted = iLoansService.deleteLoan(mobileNumber);
	    if(isDeleted) {
	        return ResponseEntity
	                .status(HttpStatus.OK)
	                .body(new ResponseDto(LoansConstants.STATUS_200, LoansConstants.MESSAGE_200));
	    }else{
	        return ResponseEntity
	                .status(HttpStatus.EXPECTATION_FAILED)
	                .body(new ResponseDto(LoansConstants.STATUS_417, LoansConstants.MESSAGE_417_DELETE));
	    }
	}
  
}
