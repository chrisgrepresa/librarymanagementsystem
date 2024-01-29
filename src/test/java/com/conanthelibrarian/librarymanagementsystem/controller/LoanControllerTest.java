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
        //Given:
        List<LoanDTO> loanDTOList = List.of(LoanDTO.builder()
                .bookId(1)
                .build());
        //When:
        when(loanService.findAllLoan()).thenReturn(loanDTOList);
        //Then:
        ResponseEntity<List<LoanDTO>> result = loanController.findAllLoan();
        assertEquals(1, result.getBody().get(0).getBookId());
    }

    @Test
    @DisplayName("Find All Loan No Content Result")
    public void findAllLoanNoContentTest(){
        //Given:
        List<LoanDTO> loanDTOList = List.of();
        //When:
        when(loanService.findAllLoan()).thenReturn(loanDTOList);
        //Then:
        ResponseEntity<List<LoanDTO>> result = loanController.findAllLoan();
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    }

    @Test
    @DisplayName("Find Loan By Id")
    public void findLoanByIdTest(){
        //Given:
        String id = "1";
        Optional<LoanDTO> optionalLoanDTO = Optional.of(LoanDTO.builder()
                .userId(1)
                .build());
        //When:
        when(loanService.findLoanById(Mockito.anyInt())).thenReturn(optionalLoanDTO);
        //Then:
        ResponseEntity<Optional<LoanDTO>> result = loanController.findLoanById(id);
        assertEquals(1, result.getBody().get().getUserId());
    }

    @Test
    @DisplayName("Find Loan By Id No Found Result")
    public void findLoanByIdNotFoundTest(){
        //Given:
        String id= "1";
        Optional<LoanDTO> optionalLoanDTO = Optional.empty();
        //When:
        when(loanService.findLoanById(Mockito.anyInt())).thenReturn(optionalLoanDTO);
        //Then:
        ResponseEntity<Optional<LoanDTO>> result = loanController.findLoanById(id);
        assertTrue(result.getBody().isEmpty());
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    @DisplayName("Find Loan By Id Wrong Path Variable Result")
    public void findLoanByIdWrongPathVariableTest(){
        String id= "ñ";
        assertThrows(Exception.class,() -> {
            ResponseEntity<Optional<LoanDTO>> result = loanController.findLoanById(id);
        });
    }

    @Test
    @DisplayName("Create New Loan")
    public void createNewLoanTest(){
        //Given:
        LoanDTO loanDTO = LoanDTO.builder()
                .userId(1)
                .build();
        //Then:
        ResponseEntity<String> result = loanController.createNewLoan(loanDTO);
        assertEquals("New loan saved", result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    @DisplayName("Create New Loan Internal Server Error")
    public void createNewLoanInternalServerErrorTest(){
        //Given:
        LoanDTO loanDTO = LoanDTO.builder()
                .userId(1)
                .build();
        //When:
        doThrow(new RuntimeException("this is an error")).when(loanService).createNewLoan(loanDTO);
        //Then:
        ResponseEntity<String> result = loanController.createNewLoan(loanDTO);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

    @Test
    @DisplayName("Modify Loan")
    public void modifyLoanTest(){
        //Given:
        LoanDTO loanDTO = LoanDTO.builder()
                .loanId(1)
                .build();
        Optional<LoanDTO> optionalLoanDTO = Optional.of(LoanDTO.builder()
                .loanId(1)
                .build());
        //When:
        when(loanService.findLoanById(Mockito.anyInt())).thenReturn(optionalLoanDTO);
        //Then:
        ResponseEntity<String> result = loanController.modifyLoan(loanDTO);
        assertEquals("Loan modified", result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    @DisplayName("Modify Loan Not Found")
    public void modifyLoanNotFoundTest(){
        //Given:
        LoanDTO loanDTO = LoanDTO.builder()
                .loanId(1)
                .build();
        Optional<LoanDTO> optionalLoanDTO = Optional.empty();
        //When:
        when(loanService.findLoanById(Mockito.anyInt())).thenReturn(optionalLoanDTO);
        //Then:
        ResponseEntity<String> result = loanController.modifyLoan(loanDTO);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    @DisplayName("Modify Loan Internal Server Error")
    public void modifyLoanInternalServerErrorTest(){
        //Given:
        LoanDTO loanDTO = LoanDTO.builder()
                .loanId(1)
                .build();
        Optional<LoanDTO> optionalLoanDTO = Optional.of(LoanDTO.builder()
                .loanId(1)
                .build());
        //When:
        when(loanService.findLoanById(Mockito.anyInt())).thenReturn(optionalLoanDTO);
        doThrow(new RuntimeException("this is an error")).when(loanService).modifyLoan(loanDTO);
        //Then:
        ResponseEntity<String> result = loanController.modifyLoan(loanDTO);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

    @Test
    @DisplayName("Delete Loan")
    public void deleteLoanTest(){
        //Given:
        String id= "1";
        LoanDTO loanDTO = LoanDTO.builder()
                .userId(1)
                .build();
        //Then:
        ResponseEntity<String> result = loanController.deleteLoan(id);
        assertEquals("Loan deleted", result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    @DisplayName("Delete Loan Internal Server Error")
    public void deleteLoanInternalServerErrorTest(){
        //Given:
        String id= "ñ";
        LoanDTO loanDTO = LoanDTO.builder()
                .userId(1)
                .build();
        //Then:
        ResponseEntity<String> result = loanController.deleteLoan(id);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

    @Test
    @DisplayName("Calculate Fee")
    public void calculateFeeTest(){
        //Given:
        String id= "1";
        String localDate= "2023-12-10";
        String fee = "fee";
        LoanDTO loanDTO = LoanDTO.builder()
                .userId(1)
                .build();
        //When:
        when(loanService.calculateFees(Integer.parseInt(id), LocalDate.parse(localDate))).thenReturn(fee);
        //Then:
        ResponseEntity<String> result = loanController.calculateFee(id,localDate);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    @DisplayName("Calculate Fee No Content")
    public void calculateFeeNoContentTest(){
        //Given:
        String id= "1";
        String localDate= "2023-12-10";
        String fee = null;
        LoanDTO loanDTO = LoanDTO.builder().userId(1).build();
        //When:
        when(loanService.calculateFees(Integer.parseInt(id), LocalDate.parse(localDate))).thenReturn(fee);
        //Then:
        ResponseEntity<String> result = loanController.calculateFee(id,localDate);
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    }
}