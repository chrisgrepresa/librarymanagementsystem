package com.conanthelibrarian.librarymanagementsystem.service;

import com.conanthelibrarian.librarymanagementsystem.dao.Loan;
import com.conanthelibrarian.librarymanagementsystem.dto.LoanDTO;
import com.conanthelibrarian.librarymanagementsystem.mapper.LoanMapper;
import com.conanthelibrarian.librarymanagementsystem.repository.LoanRepository;
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

    //TODO CAMBIAR SINGLETON
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
        Integer loanId = 1;
        LocalDate localDateStart = LocalDate.of(2023, 12, 28);
        LocalDate localDateEnd = LocalDate.of(2023, 12, 30);
        Loan loan = new Loan();
        LoanDTO loanDTO = new LoanDTO(2, 1, 1, localDateStart, localDateEnd);
        Optional<Loan> loanOptional = Optional.of(loan);
        when(loanRepository.findById(loanId)).thenReturn(loanOptional);
        when(loanMapper.loanToLoanDTO(any(Loan.class))).thenReturn(loanDTO);
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
        Loan loan = new Loan();
        LoanDTO loanDTO = LoanDTO.builder()
                .loanId(1).build();
        when(loanMapper.loanDTOToLoan(any(LoanDTO.class))).thenReturn(loan);
        loanService.createNewLoan(loanDTO);
        verify(loanRepository, times(1)).save(loan);
        Mockito.verify(loanMapper).loanDTOToLoan(loanDTO);
    }


    @Test
    @DisplayName("Modify Loan")
    public void modifyLoanTest() {
        //Given:
        Integer loanId = 1;
        LoanDTO loanDTO = new LoanDTO();
        Optional<Loan> optionalloanDTO = Optional.ofNullable(Loan.builder()
                .loanId(1)
                .build());
        //When:
        when(loanRepository.findById(loanId)).thenReturn(optionalloanDTO);
        //Then:
        loanService.modifyLoan(loanId, loanDTO);
        verify(loanRepository, times(1)).save(Mockito.any());
    }

    @Test
    @DisplayName("Modify Loan Not Found")
    public void modifyLoanTestNoFoundTest() {
        //Given:
        Integer loanId = 1;
        LoanDTO loanDTO = new LoanDTO();
        Optional<Loan> optionalLoanDTO = Optional.empty();
        //When:
        when(loanRepository.findById(loanId)).thenReturn(optionalLoanDTO);
        //Then:
        loanService.modifyLoan(loanId, loanDTO);
        Mockito.verify(loanRepository, never()).save(Mockito.any());
    }

    @Test
    @DisplayName("Modify Loan Wrong Path Variable")
    public void modifyLoanTestWrongPathVariable() {
        //Given:
        String loanId = "ñ";
        LoanDTO loanDTO = new LoanDTO();
        assertThrows(Exception.class, () -> {
            loanService.modifyLoan(Integer.parseInt(loanId), loanDTO);
        });
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
        LocalDate localDate = LocalDate.of(2023, 10, 8);
        Loan loan = new Loan();
        Optional<Loan> loanOptional = Optional.of(loan);
        LoanDTO loanDTO = LoanDTO.builder().dueDate(LocalDate.of(2023,10,6)).build();
        //When:
        when(loanRepository.findById(Mockito.anyInt())).thenReturn(loanOptional);
        when(loanMapper.loanToLoanDTO(any(Loan.class))).thenReturn(loanDTO);
        //Then:
        String result = loanService.calculateFees(loanId, localDate);
        assertEquals(Constants.FEE_FIRST_TRANCHE + 2 + Constants.DELAY_DAYS_RESULT + 0.2,
                result);
    }

    @Test
    @DisplayName("Calculate Fees Case Two")
    public void calculateFeesCaseTwoTest() {
        //Given:
        Integer loanId = 2;
        LocalDate localDate = LocalDate.of(2023, 10, 16);
        Loan loan = new Loan();
        Optional<Loan> loanOptional = Optional.of(loan);
        LoanDTO loanDTO = LoanDTO.builder().dueDate(LocalDate.of(2023,10,6)).build();
        //When:
        when(loanRepository.findById(Mockito.anyInt())).thenReturn(loanOptional);
        when(loanMapper.loanToLoanDTO(any(Loan.class))).thenReturn(loanDTO);
        //Then:
        String result = loanService.calculateFees(loanId, localDate);
        assertEquals(Constants.FEE_SECOND_TRANCHE + 10 + Constants.DELAY_DAYS_RESULT + 2.5,
                result);
    }

    @Test
    @DisplayName("Calculate Fees Case Three")
    public void calculateFeesCaseThreeTest() {
        //Given:
        Integer loanId = 2;
        LocalDate localDate = LocalDate.of(2023, 10, 26);
        Loan loan = new Loan();
        Optional<Loan> loanOptional = Optional.of(loan);
        LoanDTO loanDTO = LoanDTO.builder().dueDate(LocalDate.of(2023,10,6)).build();
        //When:
        when(loanRepository.findById(Mockito.anyInt())).thenReturn(loanOptional);
        when(loanMapper.loanToLoanDTO(any(Loan.class))).thenReturn(loanDTO);
        //Then:
        String result = loanService.calculateFees(loanId, localDate);
        assertEquals(Constants.FEE_THIRD_TRANCHE + 20 + Constants.DELAY_DAYS_RESULT + 10.0,
                result);
    }

    @Test
    @DisplayName("Calculate Fees Case Four")
    public void calculateFeesCaseFourTest() {
        //Given:
        Integer loanId = 2;
        LocalDate localDate = LocalDate.of(2023, 12, 10);
        Loan loan = new Loan();
        Optional<Loan> loanOptional = Optional.of(loan);
        LoanDTO loanDTO = LoanDTO.builder().dueDate(LocalDate.of(2023,10,6)).build();
        //When:
        when(loanRepository.findById(Mockito.anyInt())).thenReturn(loanOptional);
        when(loanMapper.loanToLoanDTO(any(Loan.class))).thenReturn(loanDTO);
        //Then:
        String result = loanService.calculateFees(loanId, localDate);
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
        LoanDTO loanDTO = LoanDTO.builder().dueDate(LocalDate.of(2023,10,06)).build();
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