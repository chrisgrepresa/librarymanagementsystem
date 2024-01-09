package com.conanthelibrarian.librarymanagementsystem.service;

import com.conanthelibrarian.librarymanagementsystem.dao.Book;
import com.conanthelibrarian.librarymanagementsystem.dao.Loan;
import com.conanthelibrarian.librarymanagementsystem.dao.User;
import com.conanthelibrarian.librarymanagementsystem.dto.BookDTO;
import com.conanthelibrarian.librarymanagementsystem.dto.LoanDTO;
import com.conanthelibrarian.librarymanagementsystem.dto.UserDTO;
import com.conanthelibrarian.librarymanagementsystem.mapper.LoanMapper;
import com.conanthelibrarian.librarymanagementsystem.repository.LoanRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanServiceTest {

    @InjectMocks
    LoanService loanService;

    @Mock
    LoanRepository loanRepository;

    @Mock
    LoanMapper loanMapper;

    @Test
    @DisplayName("Find All Loans")
    public void findAllLoanTest() {
        Loan loan = new Loan();
        LoanDTO loanDTO = new LoanDTO();
        List<Loan> loanList = Collections.singletonList(loan);
        when(loanRepository.findAll()).thenReturn(loanList);
        when(loanMapper.loanToLoanDTO(any(Loan.class))).thenReturn(loanDTO);
        List<LoanDTO> result = loanService.findAllLoan();
        assertEquals(1, result.size());
        assertEquals(loanDTO, result.get(0));
        Mockito.verify(loanMapper).loanToLoanDTO(loan);
    }

    @Test
    @DisplayName("Find Loan By Id")
    public void findLoanByIdTest(){
        Integer loanId = 1;
        LocalDate localDateStart = LocalDate.of(2023,12,28);
        LocalDate localDateEnd = LocalDate.of(2023,12,30);
        Loan loan = new Loan();
        LoanDTO loanDTO = new LoanDTO(2, 1,1, localDateStart, localDateEnd);
        Optional<Loan> loanOptional = Optional.of(loan);
        when(loanRepository.findById(loanId)).thenReturn(loanOptional);
        when(loanMapper.loanToLoanDTO(any(Loan.class))).thenReturn(loanDTO);
        Optional<LoanDTO> result = loanService.findLoanById(loanId);
        assertEquals(LocalDate.of(2023,12, 28), result.get().getLoanDate());
        Mockito.verify(loanMapper).loanToLoanDTO(loan);
    }

    @Test
    @DisplayName("Create New Loan")
    public void createNewLoanTest(){
        Loan loan = new Loan();
        LocalDate localDateStart = LocalDate.of(2023,12,28);
        LocalDate localDateEnd = LocalDate.of(2023,12,30);
        LoanDTO loanDTO = new LoanDTO(2, 1,1, localDateStart, localDateEnd);
        when(loanMapper.loanDTOToLoan(any(LoanDTO.class))).thenReturn(loan);
        loanService.createNewLoan(loanDTO);
        verify(loanRepository,times(1)).save(loan);
        Mockito.verify(loanMapper).loanDTOToLoan(loanDTO);
    }

    //todo CORREGIR:
    @Test
    @DisplayName("Modify Loan If Optional Is Present")
    public void modifyLoanTest(){
        Integer id= 1;
        Loan loan = new Loan();
        LocalDate localDateStart = LocalDate.of(2023,12,28);
        LocalDate localDateEnd = LocalDate.of(2023,12,30);
        LoanDTO loanDTO = new LoanDTO(2, 1,1, localDateStart, localDateEnd);
        when(loanMapper.loanDTOToLoan(any(LoanDTO.class))).thenReturn(loan);
        loanService.modifyLoan(id,loanDTO);
        verify(loanRepository,times(1)).save(loan);
        Mockito.verify(loanMapper).loanDTOToLoan(loanDTO);
    }

    @Test
    @DisplayName("Delete Loan")
    public void deleteLoanTest(){
        Integer loanId = 1;
        loanService.deleteLoanById(loanId);
        verify(loanRepository,times(1)).deleteById(loanId);
    }

}