package com.conanthelibrarian.librarymanagementsystem.service;

import com.conanthelibrarian.librarymanagementsystem.dao.Loan;
import com.conanthelibrarian.librarymanagementsystem.dto.LoanDTO;
import com.conanthelibrarian.librarymanagementsystem.mapper.LoanMapper;
import com.conanthelibrarian.librarymanagementsystem.repository.BookRepository;
import com.conanthelibrarian.librarymanagementsystem.repository.LoanRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Log4j2
public class LoanService {

    private final LoanRepository loanRepository;
    private final LoanMapper loanMapper;
    private final BookRepository bookRepository;


    public List<LoanDTO> findAllLoan(){
        return loanRepository.findAll().stream()
                .map(loanMapper::loanToLoanDTO)
                .collect(Collectors.toList());
    }

    public Optional<LoanDTO> findLoanById(Integer id){
        return loanRepository.findById(id).stream()
                .map(loanMapper::loanToLoanDTO)
                .findAny();
    }

    public void createNewLoan(LoanDTO loanDTO){
        Loan loan = loanMapper.loanDTOToLoan(loanDTO);
        loanRepository.save(loan);
        log.info("Loan saved with id: {}", loanDTO.getLoanId());
    }

    public LoanDTO modifyLoan(Integer id, LoanDTO loanDTO){
        Optional<Loan> loanOptional = loanRepository.findById(id);
        if(loanOptional.isPresent()){
            Loan loan = loanMapper.loanDTOToLoan(loanDTO);
            loanRepository.save(loan);
            log.info("Loan modified with id: {}", loanDTO.getLoanId());
        }
        return loanDTO;
    }

    public void deleteLoanById(Integer id) {
        if(id != null){
            loanRepository.deleteById(id);
        }
    }

    public String calculateFees(Integer loanId, LocalDate localDate){
        Integer days = (int)ChronoUnit.DAYS.between(findLoanById(loanId).get().getDueDate(), localDate);
        switch ((1 <= days && days <= 5 ) ? 1 : (6 <= days && days <= 15) ? 2
                : (16 <= days && days <= 29) ? 3 : (30 <= days) ? 4 : 5)
        {
            case 1:
                log.info("First fee");
                Double firstFee = days * 0.10;
                return "First tranche (1-5 days late): The book is return with " + days + " days delay, your fee is: " + firstFee ;
            case 2:
                log.info("Second fee");
                Double secondFee = days * 0.25;
                return "Second tranche (6-15 days late): The book is return with " + days + " days delay, your fee is: " + secondFee ;
            case 3:
                log.info("Third fee");
                Double thirdFee = days * 0.50;
                return "Third tranche (16-29 days late): The book is return with " + days + " days delay, your fee is: " + thirdFee ;
            case 4:
                log.info("Fourth fee");
                Double fourthFee = days * 1.0;
                return "Fourth tranche (above 30 days late): The book is return with " + days + " days delay, your fee is: " + fourthFee ;
        }
        return "This case is not possible";
        //todo esto se puede mejorar con los SOUT en una clase de Constantes.
    }

}
