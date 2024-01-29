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
    public void userDTOToUserTest(){
        UserDTO userDTO = UserDTO.builder()
                .name("name")
                .build();
        User user = userMapper.userDTOToUser(userDTO);
        assertEquals("name", userDTO.getName());
    }

    @Test
    @DisplayName("UserDTO To User Null")
    public void userDTOToUserNullTest(){
        UserDTO userDTO = null;
        User user = userMapper.userDTOToUser(userDTO);
        assertNull(user);
    }

    @Test
    @DisplayName("User To UserDTO")
    public void userToUserDTOTest(){
        User user = User.builder()
                .name("name")
                .build();
        UserDTO userDTO = userMapper.userToUserDTO(user);
        assertEquals("name", user.getName());
    }

    @Test
    @DisplayName("User To UserDTO Null")
    public void userToUserDTONullTest(){
        User user = null;
        UserDTO userDTO = userMapper.userToUserDTO(user);
        assertNull(userDTO);
    }

}