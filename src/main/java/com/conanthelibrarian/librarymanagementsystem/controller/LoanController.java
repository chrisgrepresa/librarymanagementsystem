package com.conanthelibrarian.librarymanagementsystem.controller;

import com.conanthelibrarian.librarymanagementsystem.dto.LoanDTO;
import com.conanthelibrarian.librarymanagementsystem.service.LoanService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/loan")
@Log4j2
public class LoanController {

    private final LoanService loanService;


    @GetMapping("/all")
    public ResponseEntity<List<LoanDTO>> findAllLoan() {
        if (loanService.findAllLoan().isEmpty()) {
            return new ResponseEntity<>(loanService.findAllLoan(), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(loanService.findAllLoan(), HttpStatus.OK);
    }

    @GetMapping("/find/{loanId}")
    public ResponseEntity<Optional<LoanDTO>> findLoanById(@PathVariable String loanId) {
        if (loanService.findLoanById(Integer.parseInt(loanId)).isEmpty()) {
            log.info("Loan not found with ID:{}", loanId);
            return new ResponseEntity<>(loanService.findLoanById(Integer.parseInt(loanId)), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(loanService.findLoanById(Integer.parseInt(loanId)), HttpStatus.OK);
    }


    @PostMapping("/new")
    public ResponseEntity<String> createNewLoan(@RequestBody LoanDTO loanDTO) {
        try {
            loanService.createNewLoan(loanDTO);
            log.info("New loan saved");
            return ResponseEntity.status(200).body("New loan saved");
        } catch (Exception e) {
            log.info("Error when saving loan: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error when saving loan:" + e.getMessage());
        }
    }

    @PutMapping("/modify/{loanId}")
    public ResponseEntity<String> modifyLoan(@PathVariable String loanId, @RequestBody LoanDTO loanDTO) {
        try {
            if(loanService.findLoanById(Integer.parseInt(loanId)).isEmpty()){
                log.info("Loan not found");
                return ResponseEntity.status(404).body("Loan not found");
            }
            else {
                loanService.modifyLoan(loanDTO.getLoanId(), loanDTO);
                log.info("Loan modified");
                return ResponseEntity.status(200).body("Loan modified");
            }
        } catch (Exception e) {
            log.info("Error when saving loan: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error when saving loan:" + e.getMessage());
        }
    }


    @DeleteMapping("/delete/{loanId}")
    public ResponseEntity<String> deleteLoan(@PathVariable String loanId) {
        try {
            loanService.deleteLoanById(Integer.parseInt(loanId));
            log.info("Loan deleted with Id: {}", loanId);
            return ResponseEntity.status(200).body("Loan deleted");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error when deleting loan: " +
                    e.getMessage());
        }
    }

    @GetMapping("/fee/{loanId}/{localDate}")
    public ResponseEntity<String> calculateFee(@PathVariable String loanId,
                                               @PathVariable String localDate) {

        if (loanService.calculateFees(Integer.parseInt(loanId), LocalDate.parse(localDate)) == null) {
            return new ResponseEntity<>(loanService.calculateFees(Integer.parseInt(loanId), LocalDate.parse(localDate)), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(loanService.calculateFees(Integer.parseInt(loanId), LocalDate.parse(localDate)), HttpStatus.OK);
    }


}
