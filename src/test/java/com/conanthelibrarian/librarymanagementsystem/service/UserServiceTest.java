package com.conanthelibrarian.librarymanagementsystem.service;

import com.conanthelibrarian.librarymanagementsystem.dao.Book;
import com.conanthelibrarian.librarymanagementsystem.dao.User;
import com.conanthelibrarian.librarymanagementsystem.dto.BookDTO;
import com.conanthelibrarian.librarymanagementsystem.dto.UserDTO;
import com.conanthelibrarian.librarymanagementsystem.mapper.BookMapper;
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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

}