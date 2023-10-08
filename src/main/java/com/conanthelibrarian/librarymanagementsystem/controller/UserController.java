package com.conanthelibrarian.librarymanagementsystem.controller;

import com.conanthelibrarian.librarymanagementsystem.dto.UserDTO;
import com.conanthelibrarian.librarymanagementsystem.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
@Log4j2
public class UserController {

    private final UserService userService;

    @GetMapping("/all")
    public List<UserDTO> findAllUser(){
        return userService.findUser();
    }

    @PostMapping("/new")
    public void newUser(@RequestBody UserDTO userDTO){
        userService.newUser(userDTO);
    }




}
