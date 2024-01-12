package com.conanthelibrarian.librarymanagementsystem.controller;

import com.conanthelibrarian.librarymanagementsystem.dao.User;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
    public void findAllUserTest(){
        List<UserDTO> userDTOList = List.of(new UserDTO(1, "name", "email", "password", "role"));
        when(userService.findAllUser()).thenReturn(userDTOList);
        ResponseEntity<List<UserDTO>> result = userController.findAllUser();
        assertEquals("name", result.getBody().get(0).getName());
    }

    @Test
    @DisplayName("Find All User NO CONTENT Result")
    public void findAllUserNoContentTest(){
        List<UserDTO> userDTOList = List.of();
        when(userService.findAllUser()).thenReturn(userDTOList);
        ResponseEntity<List<UserDTO>> result = userController.findAllUser();
        assertTrue(result.getBody().isEmpty());
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    }

    @Test
    @DisplayName("Find User By Id")
    public void findUserByIdTest(){
        String id = "1";
        Optional<UserDTO> optionalUserDTO = Optional.of(UserDTO.builder().name("name").build());
        when(userService.findUserById(Mockito.anyInt())).thenReturn(optionalUserDTO);
        ResponseEntity<Optional<UserDTO>> result = userController.findUserById(id);
        assertEquals("name", result.getBody().get().getName());
    }

    @Test
    @DisplayName("Find User By Id Not Found Result")
    public void findUserByIdNotFoundTest(){
        String id= "1";
        Optional<UserDTO> optionalUserDTO = Optional.empty();
        when(userService.findUserById(Mockito.anyInt())).thenReturn(optionalUserDTO);
        ResponseEntity<Optional<UserDTO>> result = userController.findUserById(id);
        assertTrue(result.getBody().isEmpty());
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    @DisplayName("Find User By Id Wrong Path Variable Result")
    public void findUserByIdWrongPathVariableTest(){
        String id= "ñ";
        assertThrows(Exception.class,() -> {
            ResponseEntity<Optional<UserDTO>> result = userController.findUserById(id);
        });
    }

    @Test
    @DisplayName("Create New User")
    public void createNewUserTest(){
        UserDTO userDTO = UserDTO.builder().name("name").build();
        ResponseEntity<String> result = userController.createNewUser(userDTO);
        assertEquals("New user saved", result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    @DisplayName("Create New User Wrong Body Request")
    public void createNewUserWrongBodyRequestTest(){
        UserDTO userDTO = UserDTO.builder().name("name").build();
        doThrow(new RuntimeException("this is an error")).when(userService).createNewUser(userDTO);
        ResponseEntity<String> result = userController.createNewUser(userDTO);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

    @Test
    @DisplayName("Modify User")
    public void modifyUserTest(){
        String id = "1";
        Optional<UserDTO> optionalUserDTO = Optional.of(UserDTO.builder().name("name").build());
        UserDTO userDTO = UserDTO.builder().name("name").build();
        when(userService.findUserById(Mockito.anyInt())).thenReturn(optionalUserDTO);
        ResponseEntity<String> result = userController.modifyUser(id, userDTO);
        assertEquals("User modified", result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }
//todo Modify: not found, wrong path

    @Test
    @DisplayName("Modify User Not Found")
    public void modifyUserNotFoundTest(){
        String id= "1";
        Optional<UserDTO> optionalUserDTO = Optional.empty();
        UserDTO userDTO = new UserDTO();
        when(userService.findUserById(Mockito.anyInt())).thenReturn(optionalUserDTO);
        ResponseEntity<String> result = userController.modifyUser(id,userDTO);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    /*@Test
    @DisplayName("Modify User Wrong Path Variable")
    public void modifyUserWrongPathVariableTest(){
        String id= "ñ";
        Optional<UserDTO> optionalUserDTO = Optional.empty();
        UserDTO userDTO = UserDTO.builder().name("name").build();
        doThrow(new RuntimeException("this is an error")).when(userService.findUserById(Mockito.anyInt()));
        doThrow(new RuntimeException("this is an error")).when(userService).modifyUser(Mockito.anyInt(),userDTO);
        ResponseEntity<String> result = userController.modifyUser(id,userDTO);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }*/


    @Test
    @DisplayName("Delete User")
    public void deleteUserTest(){
        String id= "1";
        UserDTO userDTO = UserDTO.builder().name("name").build();
        ResponseEntity<String> result = userController.deleteUser(id);
        assertEquals("User deleted", result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    //todo DELETE: faltan Id Not FOund y Wrong Path Variable
    //posibilidades: que funcione, que el Id sea Not Found, que sea Wrong Path Variable,
    //posibilidad de que pase lo mismo que en el ejercicio anterior

    @Test
    @DisplayName("Delete User Internal Server Error")
    public void deleteUserInternalServerErrorTest(){
        String id= "1";
        UserDTO userDTO = UserDTO.builder().name("name").build();
        doThrow(new RuntimeException("this is an error")).when(userService).deleteUserById(Integer.parseInt(id));
        ResponseEntity<String> result = userController.deleteUser(id);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }


    @Test
    @DisplayName("Find User In Loan For Quantity")
    public void findUserInLoanForQuantityTest(){
        String quantity= "1";
        List<UserDTO> userDTOList = List.of(new UserDTO(1, "name", "email", "password", "role"));
        when(userService.findUserInLoan(Mockito.anyInt())).thenReturn(userDTOList);
        ResponseEntity<List<UserDTO>> result = userController.findUserInLoanForQuantity(Integer.parseInt(quantity));
        assertEquals("name", result.getBody().get(0).getName());
    }

    @Test
    @DisplayName("Find User In Loan For Quantity NO CONTENT Result")
    public void findUserInLoanForQuantityNoContentTest(){
        String quantity = "1";
        List<UserDTO> userDTOList = List.of();
        when(userService.findUserInLoan(Mockito.anyInt())).thenReturn(userDTOList);
        ResponseEntity<List<UserDTO>> result = userController.findUserInLoanForQuantity(Integer.parseInt(quantity));
        assertTrue(result.getBody().isEmpty());
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    }

    @Test
    @DisplayName("Find User In Loan For Quantity Wrong Path Variable Result")
    public void findUserInLoanForQuantityWrongPathVariableTest(){
        String quantity = "ñ";
        assertThrows(Exception.class,()-> {
            ResponseEntity<List<UserDTO>> result = userController.findUserInLoanForQuantity(Integer.parseInt(quantity));
        });
    }

    @Test
    @DisplayName("Book Per User")
    public void bookPerUserTest(){
        String userId= "2";
        Integer loadId= 3;
        List<BookDTO> bookDTOList = List.of(new BookDTO(1,"title", "author", 1L, "genre", 3));
        when(userService.findBookPerUser(Mockito.anyInt())).thenReturn(bookDTOList);
        ResponseEntity<List<BookDTO>> result = userController.bookPerUser(userId, loadId);
        assertEquals("author", result.getBody().get(0).getAuthor());
    }

    @Test
    @DisplayName("Book Per User No Content")
    public void bookPerUserNoContentTest(){
        String userId= "2";
        Integer loadId= 3;
        List<BookDTO> bookDTOList = List.of();
        when(userService.findBookPerUser(Mockito.anyInt())).thenReturn(bookDTOList);
        ResponseEntity<List<BookDTO>> result = userController.bookPerUser(userId, loadId);
        assertTrue(result.getBody().isEmpty());
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    }

    @Test
    @DisplayName("Book Per User Wrong Path Variable")
    public void bookPerUserWrongPathVariableTest(){
        String userId= "ñ";
        Integer loadId= 3;
        assertThrows(Exception.class,()-> {
            ResponseEntity<List<BookDTO>> result = userController.bookPerUser(userId, loadId);
        });
    }

    @Test
    @DisplayName("Loan Per User")
    public void loanPerUserTest(){
        String userId= "2";
        LocalDate localDateStart = LocalDate.of(2023,12,28);
        LocalDate localDateEnd = LocalDate.of(2023,12,30);
        List<LoanDTO> loanDTOList = List.of(new LoanDTO(2, 1,1, localDateStart, localDateEnd));
        when(userService.showLoanPerUser(Mockito.anyInt())).thenReturn(loanDTOList);
        ResponseEntity<List<LoanDTO>> result = userController.loanPerUser(userId);
        assertEquals(1, result.getBody().get(0).getUserId());
    }

    @Test
    @DisplayName("Loan Per User No Content")
    public void loanPerUserNoContentTest(){
        String userId= "2";
        LocalDate localDateStart = LocalDate.of(2023,12,28);
        LocalDate localDateEnd = LocalDate.of(2023,12,30);
        List<LoanDTO> loanDTOList = List.of();
        when(userService.showLoanPerUser(Mockito.anyInt())).thenReturn(loanDTOList);
        ResponseEntity<List<LoanDTO>> result = userController.loanPerUser(userId);
        assertTrue(result.getBody().isEmpty());
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    }

    @Test
    @DisplayName("Loan Per User Wrong Path Variable")
    public void loanPerUserWrongPathVariableTest(){
        String userId= "ñ";
        assertThrows(Exception.class,() -> {
            ResponseEntity<List<LoanDTO>> result = userController.loanPerUser(userId);
        });
    }


}