package com.conanthelibrarian.librarymanagementsystem.service;

import com.conanthelibrarian.librarymanagementsystem.dao.User;
import com.conanthelibrarian.librarymanagementsystem.dto.UserDTO;
import com.conanthelibrarian.librarymanagementsystem.mapper.UserMapper;
import com.conanthelibrarian.librarymanagementsystem.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Log4j2
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    //todo LOGS

    public List<UserDTO> findUser(){
        return userRepository.findAll().stream()
                .map(userMapper::userToUserDTO)
                .collect(Collectors.toList());
    }

    public void newUser (UserDTO userDTO){
        User user = userMapper.userDTOToUser(userDTO);
        userRepository.save(user);
    }

    public List<UserDTO> findUserById(Integer id){
        return userRepository.findById(id).stream()
                .map(userMapper::userToUserDTO)
                .collect(Collectors.toList());
    }

    public UserDTO modifyUser(Integer id, UserDTO userDTO){
        Optional<User> userOptional = userRepository.findById(id);
        if(userOptional.isPresent()){
            User user = userMapper.userDTOToUser(userDTO);
            userRepository.save(user);
        }
        return userDTO;
    }

    public void deleteUserById(Integer id) {
        userRepository.deleteById(id);
    }

}
