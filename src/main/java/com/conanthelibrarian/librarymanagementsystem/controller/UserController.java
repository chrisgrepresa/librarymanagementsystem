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

    @GetMapping("/find/{userId}")
    public ResponseEntity<Optional<UserDTO>> findUserById(@PathVariable String userId) {
        if (userService.findUserById(Integer.parseInt(userId)).isEmpty()) {
            log.info("User not found with ID:{}", userId);
            return new ResponseEntity<>(userService.findUserById(Integer.parseInt(userId)), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userService.findUserById(Integer.parseInt(userId)), HttpStatus.OK);
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

    @PutMapping("/modify/{userId}")
    public ResponseEntity<String> modifyUser(@PathVariable String userId, @RequestBody UserDTO userDTO) {
        try {
            if(userService.findUserById(Integer.parseInt(userId)).isEmpty()){
                log.info("User not found");
                return ResponseEntity.status(404).body("User not found");
            }
            else{
                userService.modifyUser(userDTO.getUserId(), userDTO);
                log.info("User modified");
                return ResponseEntity.status(200).body("User modified");
            }
        } catch (Exception e) {
            log.info("Error when saving user: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error when saving user:" + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable String userId) {
        try {
            userService.deleteUserById(Integer.parseInt(userId));
            log.info("User deleted with Id: {}", userId);
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
