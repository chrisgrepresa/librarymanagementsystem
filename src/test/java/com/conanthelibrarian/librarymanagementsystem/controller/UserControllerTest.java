package com.conanthelibrarian.librarymanagementsystem.controller;

import com.conanthelibrarian.librarymanagementsystem.dao.User;
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
    @DisplayName("Find User By Id NOT FOUND Result")
    public void findUserByIdNotFoundTest(){
        String id= "1";
        Optional<UserDTO> optionalUserDTO = Optional.empty();
        when(userService.findUserById(Mockito.anyInt())).thenReturn(optionalUserDTO);
        ResponseEntity<Optional<UserDTO>> result = userController.findUserById(id);
        assertTrue(result.getBody().isEmpty());
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    @DisplayName("Find User By Id WRONG PATH Result")
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

    //todo Aquí hay un error, en el @PutMapping ModifyUser
    @Test
    @DisplayName("Modify User")
    public void modifyUserTest(){
        String id = "1";
        UserDTO userDTO = UserDTO.builder().name("name").build();
        ResponseEntity<String> result = userController.modifyUser(id, userDTO);
        assertEquals("User modified", result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }
//todo Modify: not found, wrong path

    /*@Test
    @DisplayName("Modify User NOT FOUND")
    public void modifyUserNotFound(){
        //todo AQUÍ ESTÁ el problema: no está diferenciado en UserController.
        // Dentro del try habría que meter un if else
    }

    @Test
    @DisplayName("Modify User Wrong Path Variable")
    public void modifyUserWrongPathVariableTest(){
        String id = "ñ";
        UserDTO userDTO = UserDTO.builder().name("name").build();
        assertThrows(Exception.class,() -> {
            ResponseEntity<String> result = userController.modifyUser(id,userDTO);
        });
    }*/

    @Test
    @DisplayName("Modify User Wrong Request Body")
    public void modifyUserWrongRequestBodyTest(){
        String id= "1";
        UserDTO userDTO = UserDTO.builder().name("name").build();
        doThrow(new RuntimeException("this is an error"))
                .when(userService).modifyUser(Integer.parseInt(id), userDTO);
        ResponseEntity<String> result = userController.modifyUser(id, userDTO);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

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
    @DisplayName("Find User In Loan For Quantity Wrong Path Result")
    public void findUserInLoanForQuantityWrongPathVariableTest(){
        String quantity = "ñ";
        assertThrows(Exception.class,()-> {
            ResponseEntity<List<UserDTO>> result = userController.findUserInLoanForQuantity(Integer.parseInt(quantity));
        });
    }

    //todo bookPerUser (arreglar que no sea ResponseEntity)
    //todo LoanPerUser (lo mismo)


}