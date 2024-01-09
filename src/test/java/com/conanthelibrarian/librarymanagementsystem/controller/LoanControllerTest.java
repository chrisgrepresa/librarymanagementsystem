package com.conanthelibrarian.librarymanagementsystem.controller;

import com.conanthelibrarian.librarymanagementsystem.dto.LoanDTO;
import com.conanthelibrarian.librarymanagementsystem.service.LoanService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoanControllerTest {

    @InjectMocks
    LoanController loanController;

    @Mock
    LoanService loanService;

    @Test
    @DisplayName("Find All Loan")
    public void findAllLoanTest(){
        LocalDate localDateStart = LocalDate.of(2023,12,28);
        LocalDate localDateEnd = LocalDate.of(2023,12,30);
        List<LoanDTO> loanDTOList = List.of(new LoanDTO(2, 1,1, localDateStart, localDateEnd));
        when(loanService.findAllLoan()).thenReturn(loanDTOList);
        ResponseEntity<List<LoanDTO>> result = loanController.findAllLoan();
        assertEquals(1, result.getBody().get(0).getBookId());
    }

    @Test
    @DisplayName("Find All Loan NO CONTENT Result")
    public void findAllLoanNoContentTest(){
        List<LoanDTO> loanDTOList = List.of();
        when(loanService.findAllLoan()).thenReturn(loanDTOList);
        ResponseEntity<List<LoanDTO>> result = loanController.findAllLoan();
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    }

    @Test
    @DisplayName("Find Loan By Id")
    public void findLoanByIdTest(){
        String id = "1";
        Optional<LoanDTO> optionalLoanDTO = Optional.of(LoanDTO.builder().userId(1).build());
        when(loanService.findLoanById(Mockito.anyInt())).thenReturn(optionalLoanDTO);
        ResponseEntity<Optional<LoanDTO>> result = loanController.findLoanById(id);
        assertEquals(1, result.getBody().get().getUserId());
    }

    @Test
    @DisplayName("Find Loan By Id NOT FOUND Result")
    public void findLoanByIdNotFoundTest(){
        String id= "1";
        Optional<LoanDTO> optionalLoanDTO = Optional.empty();
        when(loanService.findLoanById(Mockito.anyInt())).thenReturn(optionalLoanDTO);
        ResponseEntity<Optional<LoanDTO>> result = loanController.findLoanById(id);
        assertTrue(result.getBody().isEmpty());
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    @DisplayName("Find Loan By Id WRONG PATH Result")
    public void findLoanByIdWrongPathVariableTest(){
        String id= "ñ";
        assertThrows(Exception.class,() -> {
            ResponseEntity<Optional<LoanDTO>> result = loanController.findLoanById(id);
        });
    }

    @Test
    @DisplayName("Create New Loan")
    public void createNewLoanTest(){
        LoanDTO loanDTO = LoanDTO.builder().userId(1).build();
        ResponseEntity<String> result = loanController.createNewLoan(loanDTO);
        assertEquals("New loan saved", result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    @DisplayName("Create New Loan Wrong Body Request")
    public void createNewLoanWrongBodyRequestTest(){
        LoanDTO loanDTO = LoanDTO.builder().userId(1).build();
        doThrow(new RuntimeException("this is an error")).when(loanService).createNewLoan(loanDTO);
        ResponseEntity<String> result = loanController.createNewLoan(loanDTO);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }
    //todo Modify

    //todo Delete

    @Test
    @DisplayName("Calculate Fee")
    public void calculateFeeTest(){
        String id= "1";
        String localDate= "2023-12-10";
        String fee = "fee";
        LoanDTO loanDTO = LoanDTO.builder().userId(1).build();
        when(loanService.calculateFees(Integer.parseInt(id), LocalDate.parse(localDate))).thenReturn(fee);
        ResponseEntity<String> result = loanController.calculateFee(id,localDate);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

}