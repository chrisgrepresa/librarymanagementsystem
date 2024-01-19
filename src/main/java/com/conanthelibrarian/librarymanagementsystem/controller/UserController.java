package com.conanthelibrarian.librarymanagementsystem.controller;

import com.conanthelibrarian.librarymanagementsystem.dao.Loan;
import com.conanthelibrarian.librarymanagementsystem.dto.BookDTO;
import com.conanthelibrarian.librarymanagementsystem.dto.UserDTO;
import com.conanthelibrarian.librarymanagementsystem.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
@Log4j2
public class UserController {

    private final UserService userService;

    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> findAllUser() {
        if (userService.findAllUser().isEmpty()) {
            return new ResponseEntity<>(userService.findAllUser(), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(userService.findAllUser(), HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<Optional<UserDTO>> findUserById(@PathVariable String id) {
        if (userService.findUserById(Integer.parseInt(id)).isEmpty()) {
            log.info("User not found with ID:{}", id);
            return new ResponseEntity<>(userService.findUserById(Integer.parseInt(id)), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userService.findUserById(Integer.parseInt(id)), HttpStatus.OK);
    }

    @PostMapping("/new")
    public ResponseEntity<String> createNewUser(@RequestBody UserDTO userDTO) {
        try {
            userService.createNewUser(userDTO);
            log.info("New user saved");
            return ResponseEntity.status(200).body("New user saved");
        } catch (Exception e) {
            log.info("Error when saving user: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error when saving user:" + e.getMessage());
        }
    }

    @PutMapping("/modify/{id}")
    public ResponseEntity<String> modifyUser(@PathVariable String id, @RequestBody UserDTO userDTO) {
        try {
            userService.modifyUser(Integer.parseInt(id), userDTO);
            log.info("New user modified");
            return ResponseEntity.status(200).body("New user modified");
        } catch (Exception e) {
            log.info("Error when saving user: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error when saving user:" + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable String id) {
        try {
            userService.deleteUserById(Integer.parseInt(id));
            log.info("User deleted with Id: {}", id);
            return ResponseEntity.status(200).body("User deleted");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error when deleting user: " +
                    e.getMessage());
        }
    }

    @GetMapping("/loan/quantity/{quantity}")
    public ResponseEntity<List<UserDTO>> findUserInLoanForQuantity(@PathVariable Integer quantity){
        if(userService.findUserInLoan(quantity).isEmpty()){
            log.info("No users found in Loan Database");
            return new ResponseEntity<>(userService.findUserInLoan(quantity), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(userService.findUserInLoan(quantity), HttpStatus.OK);
    }

    @GetMapping("/book/{userId}")
    public ResponseEntity<List<BookDTO>> findBookPerUser(@PathVariable String userId, Integer loanId){
        if(userService.findBookPerUser(Integer.parseInt(userId)).isEmpty()){
            log.info("Not book found for user with id: {}", userId);
            return new ResponseEntity<>(userService.findBookPerUser(Integer.parseInt(userId)), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(userService.findBookPerUser(Integer.parseInt(userId)), HttpStatus.OK);
    }

    @GetMapping("/book/loan/{userId}")
    public ResponseEntity<List<Loan>> findLoanPerUser(@PathVariable String userId){
        if(userService.findLoanPerUser(Integer.parseInt(userId)).isEmpty()){
            log.info("Not loan found for user with id: {}", Integer.parseInt(userId));
            return new ResponseEntity<>(userService.findLoanPerUser(Integer.parseInt(userId)), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(userService.findLoanPerUser(Integer.parseInt(userId)), HttpStatus.OK);
    }
}
