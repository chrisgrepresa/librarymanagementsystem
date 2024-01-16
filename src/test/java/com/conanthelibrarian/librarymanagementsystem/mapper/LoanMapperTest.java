package com.conanthelibrarian.librarymanagementsystem.mapper;

import com.conanthelibrarian.librarymanagementsystem.dao.Loan;
import com.conanthelibrarian.librarymanagementsystem.dto.LoanDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LoanMapperTest {

    @InjectMocks
    LoanMapper loanMapper;

    @Test
    @DisplayName("LoanDTO to Loan")
    public void loanDTOToLoanTest(){
        LocalDate localDateStart = LocalDate.of(2023,12,28);
        LocalDate localDateEnd = LocalDate.of(2023,12,30);
        LoanDTO loanDTO = new LoanDTO(2, 1,1, localDateStart, localDateEnd);
        Loan loan = loanMapper.loanDTOToLoan(loanDTO);
        assertEquals(1, loanDTO.getBookId());
    }
    @Test
    @DisplayName("LoanDTO to Loan Null")
    public void loanDTOToLoanNullTest(){
        LoanDTO loanDTO = null;
        Loan loan = loanMapper.loanDTOToLoan(loanDTO);
        assertNull(loan);
    }

    @Test
    @DisplayName("Loan To LoanDTO")
    public void loanToLoanDTOTest(){
        LocalDate localDateStart = LocalDate.of(2023,12,28);
        LocalDate localDateEnd = LocalDate.of(2023,12,30);
        Loan loan = new Loan(2, 1,1, localDateStart, localDateEnd);
        LoanDTO loanDTO = loanMapper.loanToLoanDTO(loan);
        assertEquals(1, loan.getBookId());
    }

    @Test
    @DisplayName("Loan To LoanDTO Null")
    public void loanToLoanDTONullTest(){
        Loan loan = null;
        LoanDTO loanDTO = loanMapper.loanToLoanDTO(loan);
        assertNull(loanDTO);
    }
}