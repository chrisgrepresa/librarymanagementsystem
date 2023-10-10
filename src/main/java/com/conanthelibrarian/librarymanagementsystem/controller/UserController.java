package com.conanthelibrarian.librarymanagementsystem.controller;

import com.conanthelibrarian.librarymanagementsystem.dao.User;
import com.conanthelibrarian.librarymanagementsystem.dto.UserDTO;
import com.conanthelibrarian.librarymanagementsystem.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
@Log4j2
public class UserController {

    private final UserService userService;

    //todo LOGs y ResponseEntity
    @GetMapping("/all")
    public List<UserDTO> findAllUser(){
        return userService.findUser();
    }

    @PostMapping("/new")
    public void newUser(@RequestBody UserDTO userDTO){
        userService.newUser(userDTO);
    }

    @GetMapping("/find/{id}")
    public List<UserDTO> findUserById(@PathVariable String id){
        return userService.findUserById(Integer.parseInt(id));
    }

    @PutMapping("/modify/{id}")
    public UserDTO modifyUser (@PathVariable String id, @RequestBody UserDTO userDTO)
    {
        userService.modifyUser(Integer.parseInt(id), userDTO);
        return userDTO;
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable String id){
        try{
            userService.deleteUserById(Integer.parseInt(id));
            log.info("User deleted with Id: {}", id);
            return ResponseEntity.status(200).body("User deleted");
        }catch(Exception e){
            return ResponseEntity.status(500).body("Error when deleting user: " +
                    e.getMessage());
        }
    }

}
