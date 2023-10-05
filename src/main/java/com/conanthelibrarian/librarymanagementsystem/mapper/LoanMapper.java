package com.conanthelibrarian.librarymanagementsystem.mapper;

import com.conanthelibrarian.librarymanagementsystem.dao.Loan;
import com.conanthelibrarian.librarymanagementsystem.dto.LoanDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class LoanMapper {

    public Loan loanDTOToLoan(LoanDTO loanDTO){
        if(loanDTO == null){
            return null;
        }
        Loan loan = new Loan();
        loan.setUserId(loanDTO.getUserId());
        loan.setBookId(loanDTO.getBookId());
        loan.setLoanDate(loanDTO.getLoanDate());
        loan.setDueDate(loanDTO.getDueDate());
        return loan;
    }

    public LoanDTO loanToLoanDTO(Loan loan){
        if(loan == null){
            return null;
        }
        LoanDTO loanDTO = new LoanDTO();
        loanDTO.setUserId(loan.getUserId());
        loanDTO.setBookId(loan.getBookId());
        loanDTO.setLoanDate(loan.getLoanDate());
        loanDTO.setDueDate(loan.getDueDate());
        return loanDTO;
    }
}

