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
    public ResponseEntity<List<LoanDTO>> findAllLoan(){
        if(loanService.findAllLoan().isEmpty()){
            return new ResponseEntity<>(loanService.findAllLoan(), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(loanService.findAllLoan(), HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<Optional<LoanDTO>> findLoanById(@PathVariable String id){
        if(loanService.findLoanById(Integer.parseInt(id)).isEmpty()){
            log.info("Loan not found with ID:{}", id);
            return new ResponseEntity<>(loanService.findLoanById(Integer.parseInt(id)), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(loanService.findLoanById(Integer.parseInt(id)), HttpStatus.OK);
    }


    @PostMapping("/new")
    public ResponseEntity<String> createNewLoan(@RequestBody LoanDTO loanDTO){
        try{
            loanService.createNewLoan(loanDTO);
            log.info("New loan saved");
            return ResponseEntity.status(200).body("New loan saved");
        }catch(Exception e){
            log.info("Error when saving loan: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error when saving loan:" + e.getMessage());
        }
    }

    @PutMapping("/modify/{id}")
    public ResponseEntity<String> modifyLoan(@PathVariable String id, @RequestBody LoanDTO loanDTO){
        try{
            if(loanService.findLoanById(Integer.parseInt(id)).isEmpty()){
                log.info("Loan not found with ID:{}", id);
                return new ResponseEntity<>("Loan not found", HttpStatus.NOT_FOUND);
            }
            else{
                loanService.modifyLoan(Integer.parseInt(id), loanDTO);
                log.info("Loan modified");
                return ResponseEntity.status(200).body("Loan modified");
            }
        }catch(Exception e){
            log.info("Error when saving loan: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error when saving loan:" + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteLoan(@PathVariable String id){
        try {
            loanService.deleteLoanById(Integer.parseInt(id));
            log.info("Loan deleted with Id: {}", id);
            return ResponseEntity.status(200).body("Loan deleted");
        }catch(Exception e){
            return ResponseEntity.status(500).body("Error when deleting loan: " +
                    e.getMessage());
        }
    }

    @GetMapping("/fee/{id}/{localDate}")
    public ResponseEntity<String> calculateFee(@PathVariable String id,
                                               @PathVariable String localDate){
        if(loanService.calculateFees(Integer.parseInt(id), LocalDate.parse(localDate)) == null){
            return new ResponseEntity<>
                    (loanService.calculateFees(Integer.parseInt(id), LocalDate.parse(localDate)), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>
                (loanService.calculateFees(Integer.parseInt(id), LocalDate.parse(localDate)), HttpStatus.OK);
    }


}
