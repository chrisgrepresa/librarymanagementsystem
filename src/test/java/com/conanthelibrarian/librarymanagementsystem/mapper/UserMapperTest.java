package com.conanthelibrarian.librarymanagementsystem.mapper;

import com.conanthelibrarian.librarymanagementsystem.dao.User;
import com.conanthelibrarian.librarymanagementsystem.dto.UserDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserMapperTest {

    @InjectMocks
    UserMapper userMapper;

    @Test
    @DisplayName("UserDTO To User")
    public void userDTOToUser(){
        UserDTO userDTO = new UserDTO(2, "Joe", "email", "password", "role");
        User user = userMapper.userDTOToUser(userDTO);
        assertEquals("Joe", userDTO.getName());
    }

    @Test
    @DisplayName("User To UserDTO")
    public void userToUserDTO(){
        User user = new User(2, "Joe", "email", "password", "role");
        UserDTO userDTO = userMapper.userToUserDTO(user);
        assertEquals("Joe", user.getName());
    }

}