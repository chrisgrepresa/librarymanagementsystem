package com.conanthelibrarian.librarymanagementsystem.service;

import com.conanthelibrarian.librarymanagementsystem.dao.User;
import com.conanthelibrarian.librarymanagementsystem.dto.BookDTO;
import com.conanthelibrarian.librarymanagementsystem.dto.LoanDTO;
import com.conanthelibrarian.librarymanagementsystem.dto.UserDTO;
import com.conanthelibrarian.librarymanagementsystem.mapper.BookMapper;
import com.conanthelibrarian.librarymanagementsystem.mapper.LoanMapper;
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
    private final LoanMapper loanMapper;
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public List<UserDTO> findAllUser() {
        return userRepository.findAll().stream()
                .map(userMapper::userToUserDTO)
                .collect(Collectors.toList());
    }

    public Optional<UserDTO> findUserById(Integer id) {
        return userRepository.findById(id).stream()
                .map(userMapper::userToUserDTO)
                .findAny();
    }

    public void createNewUser(UserDTO userDTO) {
        User user = userMapper.userDTOToUser(userDTO);
        userRepository.save(user);
        log.info("User saved with name: {}", userDTO.getName());
    }

    //todo a Modify le he cambiado devolver un UserDTO por devolver un void
    public void modifyUser(Integer id, UserDTO userDTO) {
        User user = userMapper.userDTOToUser(userDTO);
        userRepository.save(user);
        log.info("User modified with name: {}", userDTO.getName());
        /*if(findUserById(id).isPresent()){
            User user = userMapper.userDTOToUser(userDTO);
            userRepository.save(user);
            log.info("User modified with name: {}", userDTO.getName());
            //return userDTO;
        }
        //return null;*/
    }

    public void deleteUserById(Integer id) {
        if (id != null) {
            userRepository.deleteById(id);
            log.info("User deleted with id: {}", id);
        }
    }

    public List<UserDTO> findUserInLoan(Integer quantity) {
        return userRepository.findUserInLoanForQuantity(quantity);
    }

    public List<BookDTO> findBookPerUser(Integer userId) {
        for (LoanDTO loans : showLoanPerUser(userId)) {
            List<BookDTO> listOfBooks = bookRepository.findAll().stream()
                    .filter(book -> showLoanPerUser(userId).stream()
                            .anyMatch(loan ->
                                    loan.getUserId().equals(userId) && book.getBookId().equals(loan.getBookId()) )
                    )
                    .map(bookMapper::bookToBookDTO)
                    .collect(Collectors.toList());
            log.info("List of books for userId{} : {} ", userId, listOfBooks);
            return listOfBooks;
        }
        log.info("Does not work");
        return null;
    }


    public List<LoanDTO> showLoanPerUser(Integer userId) {
        return loanRepository.findByUserId(userId).stream()
                .map(loanMapper::loanToLoanDTO)
                .collect(Collectors.toList());
    }

}