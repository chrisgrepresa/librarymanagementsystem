package com.conanthelibrarian.librarymanagementsystem.service;

import com.conanthelibrarian.librarymanagementsystem.dao.Loan;
import com.conanthelibrarian.librarymanagementsystem.dao.User;
import com.conanthelibrarian.librarymanagementsystem.dto.BookDTO;
import com.conanthelibrarian.librarymanagementsystem.dto.UserDTO;
import com.conanthelibrarian.librarymanagementsystem.mapper.BookMapper;
import com.conanthelibrarian.librarymanagementsystem.mapper.UserMapper;
import com.conanthelibrarian.librarymanagementsystem.repository.BookRepository;
import com.conanthelibrarian.librarymanagementsystem.repository.LoanRepository;
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
    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public List<UserDTO> findAllUser() {
        return userRepository.findAll().stream()
                .map(userMapper::userToUserDTO)
                .collect(Collectors.toList());
    }

    public Optional<UserDTO> findUserById(Integer userId) {
        return userRepository.findById(userId).stream()
                .map(userMapper::userToUserDTO)
                .findAny();
    }

    public void createNewUser(UserDTO userDTO) {
        User user = userMapper.userDTOToUser(userDTO);
        userRepository.save(user);
        log.info("User saved with name: {}", userDTO.getName());
    }

    public void modifyUser(Integer userId, UserDTO userDTO) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userMapper.userDTOToUser(userDTO);
            userRepository.save(user);
            log.info("User modified with name: {}", userDTO.getName());
        }
    }

    public void deleteUserById(Integer userId) {
        if (userId != null) {
            userRepository.deleteById(userId);
            log.info("User deleted with userId: {}", userId);
        }
    }

    public List<UserDTO> findUserInLoan(Integer quantity) {
        return userRepository.findUserInLoanForQuantity(quantity);
    }

    public List<BookDTO> findBookPerUser(Integer userId) {
        for (Loan loans : findLoanPerUser(userId)) {
            List<BookDTO> bookDTOList = bookRepository.findAll().stream()
                    .filter(book -> findLoanPerUser(userId).stream()
                            .anyMatch(loan ->
                                    loan.getUserId().equals(userId) && book.getBookId().equals(loan.getBookId()))
                    )
                    .map(bookMapper::bookToBookDTO)
                    .collect(Collectors.toList());
            log.info("List of books for userId{} : {} ", userId, bookDTOList);
            return bookDTOList;
        }
        log.info("Does not work");
        return null;
    }


    public List<Loan> findLoanPerUser(Integer userId) {
        return loanRepository.findLoanByUserId(userId);
    }

}
