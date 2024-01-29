package com.conanthelibrarian.librarymanagementsystem.controller;

import com.conanthelibrarian.librarymanagementsystem.dao.Loan;
import com.conanthelibrarian.librarymanagementsystem.dto.BookDTO;
import com.conanthelibrarian.librarymanagementsystem.dto.LoanDTO;
import com.conanthelibrarian.librarymanagementsystem.dto.UserDTO;
import com.conanthelibrarian.librarymanagementsystem.service.UserService;
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
class UserControllerTest {

    @InjectMocks
    UserController userController;

    @Mock
    UserService userService;

    @Test
    @DisplayName("Find All User")
    public void findAllUserTest() {
        //Given:
        List<UserDTO> userDTOList = List.of(UserDTO.builder()
                .name("name")
                .build());
        //When:
        when(userService.findAllUser()).thenReturn(userDTOList);
        //Then:
        ResponseEntity<List<UserDTO>> result = userController.findAllUser();
        assertEquals("name", result.getBody().get(0).getName());
    }

    @Test
    @DisplayName("Find All User No Content Result")
    public void findAllUserNoContentTest() {
        //Given:
        List<UserDTO> userDTOList = List.of();
        //When:
        when(userService.findAllUser()).thenReturn(userDTOList);
        //Then:
        ResponseEntity<List<UserDTO>> result = userController.findAllUser();
        assertTrue(result.getBody().isEmpty());
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    }

    @Test
    @DisplayName("Find User By Id")
    public void findUserByIdTest() {
        //Given:
        String id = "1";
        Optional<UserDTO> optionalUserDTO = Optional.of(UserDTO.builder()
                .name("name")
                .build());
        //When:
        when(userService.findUserById(Mockito.anyInt())).thenReturn(optionalUserDTO);
        //Then:
        ResponseEntity<Optional<UserDTO>> result = userController.findUserById(id);
        assertEquals("name", result.getBody().get().getName());
    }

    @Test
    @DisplayName("Find User By Id Not Found Result")
    public void findUserByIdNotFoundTest() {
        //Given:
        String id = "1";
        Optional<UserDTO> optionalUserDTO = Optional.empty();
        //When:
        when(userService.findUserById(Mockito.anyInt())).thenReturn(optionalUserDTO);
        //Then:
        ResponseEntity<Optional<UserDTO>> result = userController.findUserById(id);
        assertTrue(result.getBody().isEmpty());
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    @DisplayName("Find User By Id Wrong Path Variable Result")
    public void findUserByIdWrongPathVariableTest() {
        String id = "ñ";
        assertThrows(Exception.class, () -> {
            ResponseEntity<Optional<UserDTO>> result = userController.findUserById(id);
        });
    }

    @Test
    @DisplayName("Create New User")
    public void createNewUserTest() {
        //Given:
        UserDTO userDTO = UserDTO.builder()
                .name("name")
                .build();
        //Then:
        ResponseEntity<String> result = userController.createNewUser(userDTO);
        assertEquals("New user saved", result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    @DisplayName("Create New User Internal Server Error")
    public void createNewUserInternalServerErrorTest() {
        //Given:
        UserDTO userDTO = UserDTO.builder()
                .name("name")
                .build();
        //When:
        doThrow(new RuntimeException("this is an error")).when(userService).createNewUser(userDTO);
        //Then:
        ResponseEntity<String> result = userController.createNewUser(userDTO);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

    @Test
    @DisplayName("Modify User")
    public void modifyUserTest() {
        //Given:
        UserDTO userDTO = UserDTO.builder()
                .userId(1)
                .build();
        Optional<UserDTO> optionalUserDTO = Optional.of(UserDTO.builder()
                .userId(1)
                .build());
        //When:
        when(userService.findUserById(Mockito.anyInt())).thenReturn(optionalUserDTO);
        //Then:
        ResponseEntity<String> result = userController.modifyUser(userDTO);
        assertEquals("User modified", result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    @DisplayName("Modify User Not Found")
    public void modifyUserNotFoundTest() {
        //Given:
        UserDTO userDTO = UserDTO.builder()
                .userId(1)
                .build();
        Optional<UserDTO> optionalUserDTO = Optional.empty();
        //When:
        when(userService.findUserById(Mockito.anyInt())).thenReturn(optionalUserDTO);
        //Then:
        ResponseEntity<String> result = userController.modifyUser(userDTO);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    @DisplayName("Modify User Internal Server Error")
    public void modifyUserInternalServerErrorTest() {
        //Given:
        UserDTO userDTO = UserDTO.builder()
                .userId(1)
                .build();
        Optional<UserDTO> optionalUserDTO = Optional.of(UserDTO.builder()
                .userId(1)
                .build());
        //When:
        when(userService.findUserById(Mockito.anyInt())).thenReturn(optionalUserDTO);
        doThrow(new RuntimeException("this is an error")).when(userService).modifyUser(userDTO);
        //Then:
        ResponseEntity<String> result = userController.modifyUser(userDTO);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

    @Test
    @DisplayName("Delete User")
    public void deleteUserTest() {
        //Given:
        String id = "1";
        UserDTO userDTO = UserDTO.builder()
                .name("name")
                .build();
        //Then:
        ResponseEntity<String> result = userController.deleteUser(id);
        assertEquals("User deleted", result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    @DisplayName("Delete User Internal Server Error")
    public void deleteUserInternalServerErrorTest() {
        //Given:
        String id = "ñ";
        UserDTO userDTO = UserDTO.builder()
                .name("name")
                .build();
        //Then:
        ResponseEntity<String> result = userController.deleteUser(id);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

    @Test
    @DisplayName("Find User In Loan For Quantity")
    public void findUserInLoanForQuantityTest() {
        //Given:
        String quantity = "1";
        List<UserDTO> userDTOList = List.of(UserDTO.builder()
                .name("name")
                .build());
        //When:
        when(userService.findUserInLoan(Mockito.anyInt())).thenReturn(userDTOList);
        //Then:
        ResponseEntity<List<UserDTO>> result = userController.findUserInLoanForQuantity(Integer.parseInt(quantity));
        assertEquals("name", result.getBody().get(0).getName());
    }

    @Test
    @DisplayName("Find User In Loan For Quantity No Content Result")
    public void findUserInLoanForQuantityNoContentTest() {
        //Given:
        String quantity = "1";
        List<UserDTO> userDTOList = List.of();
        //When:
        when(userService.findUserInLoan(Mockito.anyInt())).thenReturn(userDTOList);
        //Then:
        ResponseEntity<List<UserDTO>> result = userController.findUserInLoanForQuantity(Integer.parseInt(quantity));
        assertTrue(result.getBody().isEmpty());
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    }

    @Test
    @DisplayName("Find User In Loan For Quantity Wrong Path Variable Result")
    public void findUserInLoanForQuantityWrongPathVariableTest() {
        String quantity = "ñ";
        assertThrows(Exception.class, () -> {
            ResponseEntity<List<UserDTO>> result = userController.findUserInLoanForQuantity(Integer.parseInt(quantity));
        });
    }

    @Test
    @DisplayName("Find Book Per User")
    public void findBookPerUserTest() {
        //Given:
        String userId = "2";
        Integer loanId = 3;
        List<BookDTO> bookDTOList = List.of(BookDTO.builder()
                .author("author")
                .build());
        //When:
        when(userService.findBookPerUser(Mockito.anyInt())).thenReturn(bookDTOList);
        //Then:
        ResponseEntity<List<BookDTO>> result = userController.findBookPerUser(userId, loanId);
        assertEquals("author", result.getBody().get(0).getAuthor());
    }

    @Test
    @DisplayName("Find Book Per User No Content")
    public void findBookPerUserNoContentTest() {
        //Given:
        String userId = "2";
        Integer loanId = 3;
        List<BookDTO> bookDTOList = List.of();
        //When:
        when(userService.findBookPerUser(Mockito.anyInt())).thenReturn(bookDTOList);
        //Then:
        ResponseEntity<List<BookDTO>> result = userController.findBookPerUser(userId, loanId);
        assertTrue(result.getBody().isEmpty());
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    }

    @Test
    @DisplayName("Find Book Per User Wrong Path Variable")
    public void findBookPerUserWrongPathVariableTest() {
        String userId = "ñ";
        Integer loanId = 3;
        assertThrows(Exception.class, () -> {
            ResponseEntity<List<BookDTO>> result = userController.findBookPerUser(userId, loanId);
        });
    }

    @Test
    @DisplayName("Find Loan Per User")
    public void findLoanPerUserTest() {
        //Given:
        String userId = "2";
        List<Loan> loanList = List.of(Loan.builder()
                .userId(1)
                .build());
        //When:
        when(userService.findLoanPerUser(Mockito.anyInt())).thenReturn(loanList);
        //Then:
        ResponseEntity<List<Loan>> result = userController.findLoanPerUser(userId);
        assertEquals(1, result.getBody().get(0).getUserId());
    }

    @Test
    @DisplayName("Find Loan Per User No Content")
    public void findLoanPerUserNoContentTest() {
        //Given:
        String userId = "2";
        List<Loan> loanList = List.of();
        //When:
        when(userService.findLoanPerUser(Mockito.anyInt())).thenReturn(loanList);
        //Then:
        ResponseEntity<List<Loan>> result = userController.findLoanPerUser(userId);
        assertTrue(result.getBody().isEmpty());
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    }

    @Test
    @DisplayName("Find Loan Per User Wrong Path Variable")
    public void findLoanPerUserWrongPathVariableTest() {
        String userId = "ñ";
        assertThrows(Exception.class, () -> {
            ResponseEntity<List<Loan>> result = userController.findLoanPerUser(userId);
        });
    }
}