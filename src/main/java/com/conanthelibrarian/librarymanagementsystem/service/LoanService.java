package com.conanthelibrarian.librarymanagementsystem.service;

import com.conanthelibrarian.librarymanagementsystem.dao.Loan;
import com.conanthelibrarian.librarymanagementsystem.dto.LoanDTO;
import com.conanthelibrarian.librarymanagementsystem.mapper.LoanMapper;
import com.conanthelibrarian.librarymanagementsystem.repository.LoanRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Log4j2
public class LoanService {

    private final LoanRepository loanRepository;
    private final LoanMapper loanMapper;


    public List<LoanDTO> findLoan(){
        return loanRepository.findAll().stream()
                .map(loanMapper::loanToLoanDTO)
                .collect(Collectors.toList());
    }

    public Optional<LoanDTO> findLoanById(Integer id){
        return loanRepository.findById(id).stream()
                .map(loanMapper::loanToLoanDTO)
                .findAny();
    }

    public void newLoan (LoanDTO loanDTO){
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
            log.info("Loan deleted with id: {}", id);
        }
    }

    public Long calculateFees(Integer loanId, LocalDate localDate){
        return loanRepository.findById(loanId).stream()
                .map(loan -> Duration.between(loan.getDueDate(), localDate).toDays())
                .count();
    }

}
