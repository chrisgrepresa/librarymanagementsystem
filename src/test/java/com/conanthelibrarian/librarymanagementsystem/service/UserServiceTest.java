package com.conanthelibrarian.librarymanagementsystem.service;


import com.conanthelibrarian.librarymanagementsystem.dao.Book;
import com.conanthelibrarian.librarymanagementsystem.dao.Loan;
import com.conanthelibrarian.librarymanagementsystem.dao.User;
import com.conanthelibrarian.librarymanagementsystem.dto.BookDTO;
import com.conanthelibrarian.librarymanagementsystem.dto.LoanDTO;
import com.conanthelibrarian.librarymanagementsystem.dto.UserDTO;
import com.conanthelibrarian.librarymanagementsystem.mapper.BookMapper;
import com.conanthelibrarian.librarymanagementsystem.mapper.LoanMapper;
import com.conanthelibrarian.librarymanagementsystem.mapper.UserMapper;
import com.conanthelibrarian.librarymanagementsystem.repository.BookRepository;
import com.conanthelibrarian.librarymanagementsystem.repository.LoanRepository;
import com.conanthelibrarian.librarymanagementsystem.repository.UserRepository;
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
class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Mock
    UserMapper userMapper;

    @Mock
    LoanRepository loanRepository;

    @Mock
    LoanMapper loanMapper;

    @Mock
    BookRepository bookRepository;

    @Mock
    BookMapper bookMapper;

    @Test
    @DisplayName("Find All Users")
    public void findAllUserTest() {
        User user = new User();
        UserDTO userDTO = new UserDTO();
        List<User> userList = Collections.singletonList(user);
        when(userRepository.findAll()).thenReturn(userList);
        when(userMapper.userToUserDTO(any(User.class))).thenReturn(userDTO);
        List<UserDTO> result = userService.findAllUser();
        assertEquals(1, result.size());
        assertEquals(userDTO, result.get(0));
        Mockito.verify(userMapper).userToUserDTO(user);
    }

    @Test
    @DisplayName("Find User By Id")
    public void findUserByIdTest(){
        Integer userId = 1;
        User user = new User();
        UserDTO userDTO = new UserDTO(2, "Joe", "email", "password", "role");
        Optional<User> userOptional = Optional.of(user);
        when(userRepository.findById(userId)).thenReturn(userOptional);
        when(userMapper.userToUserDTO(any(User.class))).thenReturn(userDTO);
        Optional<UserDTO> result = userService.findUserById(userId);
        assertEquals("Joe", result.get().getName());
        Mockito.verify(userMapper).userToUserDTO(user);
    }

    @Test
    @DisplayName("Create New User")
    public void createNewUserTest(){
        User user = new User();
        UserDTO userDTO = new UserDTO(2, "Joe", "email", "password", "role");
        when(userMapper.userDTOToUser(any(UserDTO.class))).thenReturn(user);
        userService.createNewUser(userDTO);
        verify(userRepository,times(1)).save(user);
        Mockito.verify(userMapper).userDTOToUser(userDTO);
    }

    @Test
    @DisplayName("Modify User")
    public void modifyUserTest(){
        Integer id= 1;
        User user = new User();
        UserDTO userDTO = new UserDTO(2, "Joe", "email", "password", "role");
        when(userMapper.userDTOToUser(any(UserDTO.class))).thenReturn(user);
        userService.modifyUser(id, userDTO);
        verify(userRepository,times(1)).save(user);
        Mockito.verify(userMapper).userDTOToUser(userDTO);
    }

//todo ver otras posibles opciones con Modify
    @Test
    @DisplayName("Delete User")
    public void deleteUserTest(){
        Integer userId = 1;
        userService.deleteUserById(userId);
        verify(userRepository,times(1)).deleteById(userId);
    }

    @Test
    @DisplayName("Find User in Loan")
    public void findUserInLoanTest(){
        Integer quantity= 1;
        List<UserDTO> userDTOList = List.of(new UserDTO(1, "name", "email", "password", "role"));
        when(userRepository.findUserInLoanForQuantity(quantity)).thenReturn(userDTOList);
        userService.findUserInLoan(quantity);
        assertEquals("name", userDTOList.get(0).getName());
    }

    @Test
    @DisplayName("Find Book Per User")
    public void findBookPerUserTest(){
        //todo
    }

    @Test
    @DisplayName("Show Loan Per User")
    public void showLoanPerUser(){
        Integer userId = 1;
        Loan loan = new Loan();
        LoanDTO loanDTO = new LoanDTO();
        LocalDate localDateStart = LocalDate.of(2023,12,28);
        LocalDate localDateEnd = LocalDate.of(2023,12,30);
        List<Loan> loanList = List.of(new Loan(2, 1,50, localDateStart, localDateEnd));
        when(loanRepository.findByUserId(userId)).thenReturn(loanList);
        when(loanMapper.loanToLoanDTO(any(Loan.class))).thenReturn(loanDTO);
        List<LoanDTO> result = userService.showLoanPerUser(userId);
        assertEquals(1, result.size());
        assertEquals(loanDTO, result.get(0));
    }
}