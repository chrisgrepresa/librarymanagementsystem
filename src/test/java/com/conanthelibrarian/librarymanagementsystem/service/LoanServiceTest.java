package com.conanthelibrarian.librarymanagementsystem.service;

import com.conanthelibrarian.librarymanagementsystem.dao.Loan;
import com.conanthelibrarian.librarymanagementsystem.dto.LoanDTO;
import com.conanthelibrarian.librarymanagementsystem.mapper.LoanMapper;
import com.conanthelibrarian.librarymanagementsystem.repository.LoanRepository;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

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
        //Given:
        Loan loan = Loan.builder()
                .userId(2)
                .build();
        LoanDTO loanDTO = new LoanDTO();
        List<Loan> loanList = List.of(loan);
        //When:
        when(loanRepository.findAll()).thenReturn(loanList);
        when(loanMapper.loanToLoanDTO(any(Loan.class))).thenReturn(loanDTO);
        //Then:
        List<LoanDTO> result = loanService.findAllLoan();
        assertEquals(1, result.size());
        assertEquals(loanDTO, result.get(0));
        Mockito.verify(loanMapper).loanToLoanDTO(loan);
    }

    @Test
    @DisplayName("Find All loan No Content")
    public void findAllLoanNoContentTest() {
        //Given:
        List<Loan> loanList = List.of();
        //When:
        when(loanRepository.findAll()).thenReturn(loanList);
        //Then:
        List<LoanDTO> result = loanService.findAllLoan();
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Find Loan By Id")
    public void findLoanByIdTest() {
        //Given:
        Integer loanId = 1;
        Loan loan = new Loan();
        LoanDTO loanDTO = LoanDTO.builder()
                .loanDate(LocalDate.of(2023,12,28))
                .build();
        Optional<Loan> loanOptional = Optional.of(loan);
        //When:
        when(loanRepository.findById(loanId)).thenReturn(loanOptional);
        when(loanMapper.loanToLoanDTO(any(Loan.class))).thenReturn(loanDTO);
        //Then:
        Optional<LoanDTO> result = loanService.findLoanById(loanId);
        assertEquals(LocalDate.of(2023, 12, 28), result.get().getLoanDate());
        Mockito.verify(loanMapper).loanToLoanDTO(loan);
    }

    @Test
    @DisplayName("Find Loan By Id Not Found")
    public void findLoanByIdNotFoundTest() {
        //When:
        Integer loanId = 1;
        Optional<Loan> loanOptional = Optional.empty();
        //Given:
        when(loanRepository.findById(loanId)).thenReturn(loanOptional);
        //Then:
        Optional<LoanDTO> result = loanService.findLoanById(loanId);
        assertTrue(result.isEmpty());

    }

    @Test
    @DisplayName("Find Loan By Id Wrong Path Variable")
    public void findLoanByIdWrongPathVariableTest() {
        String loanId = "ñ";
        assertThrows(Exception.class, () -> {
            Optional<LoanDTO> result = loanService.findLoanById(Integer.parseInt(loanId));
        });
    }

    @Test
    @DisplayName("Create New Loan")
    public void createNewLoanTest() {
        //Given:
        Loan loan = new Loan();
        LoanDTO loanDTO = LoanDTO.builder()
                .loanId(1)
                .build();
        //When:
        when(loanMapper.loanDTOToLoan(any(LoanDTO.class))).thenReturn(loan);
        //Then:
        loanService.createNewLoan(loanDTO);
        verify(loanRepository, times(1)).save(loan);
        Mockito.verify(loanMapper).loanDTOToLoan(loanDTO);
    }


    @Test
    @DisplayName("Modify Loan")
    public void modifyLoanTest() {
        //Given:
        LoanDTO loanDTO = LoanDTO.builder()
                .loanId(1)
                .build();
        Optional<Loan> optionalLoan = Optional.of(Loan.builder()
                .loanId(1)
                .build());
        //When:
        when(loanRepository.findById(Mockito.anyInt())).thenReturn(optionalLoan);
        //Then:
        loanService.modifyLoan(loanDTO);
        verify(loanRepository, times(1)).save(Mockito.any());
    }

    @Test
    @DisplayName("Modify Loan Not Found")
    public void modifyLoanTestNoFoundTest() {
        //Given:
        LoanDTO loanDTO = LoanDTO.builder()
                .loanId(1)
                .build();
        Optional<Loan> optionalLoan = Optional.empty();
        //When:
        when(loanRepository.findById(Mockito.anyInt())).thenReturn(optionalLoan);
        //Then:
        loanService.modifyLoan(loanDTO);
        Mockito.verify(loanRepository, never()).save(Mockito.any());
    }

    @Test
    @DisplayName("Delete Loan")
    public void deleteLoanTest() {
        Integer loanId = 1;
        loanService.deleteLoanById(loanId);
        verify(loanRepository, times(1)).deleteById(loanId);
    }

    @Test
    @DisplayName("Delete Loan Wrong Path Variable")
    public void deleteLoanWrongTest() {
        String loanId = "ñ";
        assertThrows(Exception.class, () -> {
            loanService.deleteLoanById(Integer.parseInt(loanId));
        });
    }

    @Test
    @DisplayName("Calculate Fees Case One")
    public void calculateFeesCaseOneTest() {
        //Given:
        Integer loanId = 2;
        LocalDate localDateFeeOne = LocalDate.of(2023, 10, 8);
        Loan loan = new Loan();
        Optional<Loan> loanOptional = Optional.of(loan);
        LoanDTO loanDTO = LoanDTO.builder()
                .dueDate(LocalDate.of(2023,10,6))
                .build();
        //When:
        when(loanRepository.findById(Mockito.anyInt())).thenReturn(loanOptional);
        when(loanMapper.loanToLoanDTO(any(Loan.class))).thenReturn(loanDTO);
        //Then:
        String result = loanService.calculateFees(loanId, localDateFeeOne);
        assertEquals(Constants.FEE_FIRST_TRANCHE + 2 + Constants.DELAY_DAYS_RESULT + 0.2,
                result);
    }

    @Test
    @DisplayName("Calculate Fees Case Two")
    public void calculateFeesCaseTwoTest() {
        //Given:
        Integer loanId = 2;
        LocalDate localDateFeeTwo = LocalDate.of(2023, 10, 16);
        Loan loan = new Loan();
        Optional<Loan> loanOptional = Optional.of(loan);
        LoanDTO loanDTO = LoanDTO.builder()
                .dueDate(LocalDate.of(2023,10,6))
                .build();
        //When:
        when(loanRepository.findById(Mockito.anyInt())).thenReturn(loanOptional);
        when(loanMapper.loanToLoanDTO(any(Loan.class))).thenReturn(loanDTO);
        //Then:
        String result = loanService.calculateFees(loanId, localDateFeeTwo);
        assertEquals(Constants.FEE_SECOND_TRANCHE + 10 + Constants.DELAY_DAYS_RESULT + 2.5,
                result);
    }

    @Test
    @DisplayName("Calculate Fees Case Three")
    public void calculateFeesCaseThreeTest() {
        //Given:
        Integer loanId = 2;
        LocalDate localDateFeeThree = LocalDate.of(2023, 10, 26);
        Loan loan = new Loan();
        Optional<Loan> loanOptional = Optional.of(loan);
        LoanDTO loanDTO = LoanDTO.builder()
                .dueDate(LocalDate.of(2023,10,6))
                .build();
        //When:
        when(loanRepository.findById(Mockito.anyInt())).thenReturn(loanOptional);
        when(loanMapper.loanToLoanDTO(any(Loan.class))).thenReturn(loanDTO);
        //Then:
        String result = loanService.calculateFees(loanId, localDateFeeThree);
        assertEquals(Constants.FEE_THIRD_TRANCHE + 20 + Constants.DELAY_DAYS_RESULT + 10.0,
                result);
    }

    @Test
    @DisplayName("Calculate Fees Case Four")
    public void calculateFeesCaseFourTest() {
        //Given:
        Integer loanId = 2;
        LocalDate localDateFeeFour = LocalDate.of(2023, 12, 10);
        Loan loan = new Loan();
        Optional<Loan> loanOptional = Optional.of(loan);
        LoanDTO loanDTO = LoanDTO.builder()
                .dueDate(LocalDate.of(2023,10,6))
                .build();
        //When:
        when(loanRepository.findById(Mockito.anyInt())).thenReturn(loanOptional);
        when(loanMapper.loanToLoanDTO(any(Loan.class))).thenReturn(loanDTO);
        //Then:
        String result = loanService.calculateFees(loanId, localDateFeeFour);
        assertEquals(Constants.FEE_FOURTH_TRANCHE + 65 + Constants.DELAY_DAYS_RESULT + 65.0,
                result);
    }

    @Test
    @DisplayName("Calculate Fees Result Impossible")
    public void calculateResultImpossibleTest() {
        //Given:
        Integer loanId = 2;
        LocalDate localDate = LocalDate.of(2012, 12, 10);
        Loan loan = new Loan();
        Optional<Loan> loanOptional = Optional.of(loan);
        LoanDTO loanDTO = LoanDTO.builder()
                .dueDate(LocalDate.of(2023,10,6))
                .build();
        //When:
        when(loanRepository.findById(Mockito.anyInt())).thenReturn(loanOptional);
        when(loanMapper.loanToLoanDTO(any(Loan.class))).thenReturn(loanDTO);
        //Then:
        String result = loanService.calculateFees(loanId, localDate);
        assertEquals(Constants.RESULT_IMPOSSIBLE, result);
    }

    @Test
    @DisplayName("Calculate Fees Wrong Path Variable")
    public void calculateWrongPathVariableTest() {
        String loanId = "ñ";
        LocalDate localDate = LocalDate.of(2023, 12, 10);
        assertThrows(Exception.class, () -> {
            String result = loanService.calculateFees(Integer.parseInt(loanId), localDate);
        });
    }
}