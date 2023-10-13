package com.conanthelibrarian.librarymanagementsystem.service;

import com.conanthelibrarian.librarymanagementsystem.dao.User;
import com.conanthelibrarian.librarymanagementsystem.dto.UserDTO;
import com.conanthelibrarian.librarymanagementsystem.mapper.UserMapper;
import com.conanthelibrarian.librarymanagementsystem.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Log4j2
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<UserDTO> findUser(){
        return userRepository.findAll().stream()
                .map(userMapper::userToUserDTO)
                .collect(Collectors.toList());
    }

    public Optional<UserDTO> findUserById(Integer id){
        return userRepository.findById(id).stream()
                .map(userMapper::userToUserDTO)
                .findAny();
    }

    public void newUser (UserDTO userDTO){
        User user = userMapper.userDTOToUser(userDTO);
        userRepository.save(user);
        log.info("User saved with name: {}", userDTO.getName());
    }

    public UserDTO modifyUser(Integer id, UserDTO userDTO){
        Optional<User> userOptional = userRepository.findById(id);
        if(userOptional.isPresent()){
            User user = userMapper.userDTOToUser(userDTO);
            userRepository.save(user);
            log.info("User modified with name: {}", userDTO.getName());
        }
        return userDTO;
    }

    public void deleteUserById(Integer id) {
        if(id != null){
            UserDTO userDTO = UserDTO.builder().build();
            userMapper.userDTOToUser(userDTO);
            userRepository.deleteById(id);
            log.info("User deleted with id: {}", userDTO.getUserId());
        }
        /*Error:
        Error when deleting user: could not execute statement
        [Cannot delete or update a parent row: a foreign key constraint fails
        (`library`.`loans`, CONSTRAINT `loans_ibfk_1` FOREIGN KEY (`user_id`)
        REFERENCES `users` (`user_id`))]
        [delete from users where user_id=?];
        SQL [delete from users where user_id=?]; constraint [null]
         */
    }


}
