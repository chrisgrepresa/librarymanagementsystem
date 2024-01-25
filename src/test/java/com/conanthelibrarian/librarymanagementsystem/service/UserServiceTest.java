package com.conanthelibrarian.librarymanagementsystem.service;

import com.conanthelibrarian.librarymanagementsystem.dao.Book;
import com.conanthelibrarian.librarymanagementsystem.dao.Loan;
import com.conanthelibrarian.librarymanagementsystem.dao.User;
import com.conanthelibrarian.librarymanagementsystem.dto.BookDTO;
import com.conanthelibrarian.librarymanagementsystem.dto.UserDTO;
import com.conanthelibrarian.librarymanagementsystem.mapper.BookMapper;
import com.conanthelibrarian.librarymanagementsystem.mapper.UserMapper;
import com.conanthelibrarian.librarymanagementsystem.repository.BookRepository;
import com.conanthelibrarian.librarymanagementsystem.repository.LoanRepository;
import com.conanthelibrarian.librarymanagementsystem.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Mock
    UserMapper userMapper;

    @Mock
    LoanRepository loanRepository;

    @Mock
    BookRepository bookRepository;

    @Mock
    BookMapper bookMapper;


    @Test
    @DisplayName("Find All Users")
    public void findAllUserTest() {
        User user = User.builder()
                .name("name")
                .build();
        UserDTO userDTO = new UserDTO();
        List<User> userList = List.of(user);
        when(userRepository.findAll()).thenReturn(userList);
        when(userMapper.userToUserDTO(any(User.class))).thenReturn(userDTO);
        List<UserDTO> result = userService.findAllUser();
        assertEquals(1, result.size());
        assertEquals(userDTO, result.get(0));
        Mockito.verify(userMapper).userToUserDTO(user);
    }

    @Test
    @DisplayName("Find All User No Content")
    public void findAllUserNoContentTest() {
        //Given:
        List<User> userList = List.of();
        //When:
        when(userRepository.findAll()).thenReturn(userList);
        //Then:
        List<UserDTO> result = userService.findAllUser();
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Find User By Id")
    public void findUserByIdTest() {
        Integer userId = 1;
        User user = new User();
        UserDTO userDTO = new UserDTO(2, "Joe", "email", "password", "role");
        Optional<User> userOptional = Optional.of(user);
        when(userRepository.findById(userId)).thenReturn(userOptional);
        when(userMapper.userToUserDTO(any(User.class))).thenReturn(userDTO);
        Optional<UserDTO> result = userService.findUserById(userId);
        assertEquals("Joe", result.get().getName());
        Mockito.verify(userMapper).userToUserDTO(user);
    }

    @Test
    @DisplayName("Find User By Id Not Found")
    public void findUserByIdNotFoundTest() {
        //When:
        Integer userId = 1;
        Optional<User> userOptional = Optional.empty();
        //Given:
        when(userRepository.findById(userId)).thenReturn(userOptional);
        //Then:
        Optional<UserDTO> result = userService.findUserById(userId);
        assertTrue(result.isEmpty());

    }

    @Test
    @DisplayName("Find User By Id Wrong Path Variable")
    public void findUserByIdWrongPathVariableTest() {
        String userId = "ñ";
        assertThrows(Exception.class, () -> {
            Optional<UserDTO> result = userService.findUserById(Integer.parseInt(userId));
        });
    }

    @Test
    @DisplayName("Create New User")
    public void createNewUserTest() {
        User user = new User();
        UserDTO userDTO = UserDTO.builder()
                .userId(1).build();
        when(userMapper.userDTOToUser(any(UserDTO.class))).thenReturn(user);
        userService.createNewUser(userDTO);
        verify(userRepository, times(1)).save(user);
        Mockito.verify(userMapper).userDTOToUser(userDTO);
    }

    @Test
    @DisplayName("Modify User")
    public void modifyUserTest() {
        //Given:
        Integer userId = 1;
        UserDTO userDTO = new UserDTO();
        Optional<User> optionalUserDTO = Optional.ofNullable(User.builder()
                .userId(1)
                .build());
        //When:
        when(userRepository.findById(userId)).thenReturn(optionalUserDTO);
        //Then:
        userService.modifyUser(userId, userDTO);
        verify(userRepository, times(1)).save(Mockito.any());
    }

    @Test
    @DisplayName("Modify User Not Found")
    public void modifyUserTestNoFoundTest() {
        //Given:
        Integer userId = 1;
        UserDTO userDTO = new UserDTO();
        Optional<User> optionalUserDTO = Optional.empty();
        //When:
        when(userRepository.findById(userId)).thenReturn(optionalUserDTO);
        //Then:
        userService.modifyUser(userId, userDTO);
        Mockito.verify(userRepository, never()).save(Mockito.any());
    }

    @Test
    @DisplayName("Modify User Wrong Path Variable")
    public void modifyUserTestWrongPathVariable() {
        //Given:
        String userId = "ñ";
        UserDTO userDTO = new UserDTO();
        assertThrows(Exception.class, () -> {
            userService.modifyUser(Integer.parseInt(userId), userDTO);
        });
    }

    @Test
    @DisplayName("Delete user")
    public void deleteUserTest() {
        Integer userId = 1;
        userService.deleteUserById(userId);
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    @DisplayName("Delete User Wrong Path Variable")
    public void deleteUserWrongTest() {
        String userId = "ñ";
        assertThrows(Exception.class, () -> {
            userService.deleteUserById(Integer.parseInt(userId));
        });
    }

    @Test
    @DisplayName("Find User in Loan")
    public void findUserInLoanTest() {
        Integer quantity = 1;
        List<UserDTO> userDTOList = List.of(new UserDTO(1, "name", "email", "password", "role"));
        when(userRepository.findUserInLoanForQuantity(quantity)).thenReturn(userDTOList);
        userService.findUserInLoan(quantity);
        assertEquals("name", userDTOList.get(0).getName());
    }

    @Test
    @DisplayName("Find User in Loan No Content")
    public void findUserInLoanNoContentTest() {
        Integer quantity = 1;
        List<UserDTO> userDTOList = List.of();
        when(userRepository.findUserInLoanForQuantity(quantity)).thenReturn(userDTOList);
        List<UserDTO> result = userService.findUserInLoan(quantity);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Find User in Loan Wrong Path Variable")
    public void findUserInLoanWrongPathVariableTest() {
        String userId = "ñ";
        assertThrows(Exception.class, () -> {
            Optional<UserDTO> result = userService.findUserById(Integer.parseInt(userId));
        });
    }

    //todo findBookByUser

    @Test
    @DisplayName("Find Book Per User")
    public void findBookPerUserTest() {
        //Given:
        Integer userId = 1;
        Book book = Book.builder()
                .bookId(2)
                .author("author").build();
        BookDTO bookDTO = BookDTO.builder()
                .bookId(2)
                .author("author").build();
        List<Loan> loanList = List.of(Loan.builder()
                .userId(1)
                .bookId(2)
                .build());
        List<Book> bookList = List.of(book);
        List<BookDTO> bookDTOList = List.of(bookDTO);
        //When:
        when(userService.findLoanPerUser(userId)).thenReturn(loanList);
        when(bookRepository.findAll()).thenReturn(bookList);
        when(bookMapper.bookToBookDTO(any(Book.class))).thenReturn(bookDTO);
        //Then:
        List<BookDTO> result = userService.findBookPerUser(userId);
        assertEquals("author", result.get(0).getAuthor());
    }

    @Test
    @DisplayName("Find Book Per User Loan Not Found")
    public void findBookPerUserLoanNotFoundTest() {
        //Given:
        Integer userId = 1;
        List<Loan> loanList = List.of();
        //When:
        when(userService.findLoanPerUser(userId)).thenReturn(loanList);
        //Then:
        List<BookDTO> result = userService.findBookPerUser(userId);
        assertNull(result);
    }

    @Test
    @DisplayName("Find Book Per User Book Not Found")
    public void findBookPerUserBookNotFoundTest() {
        //Given:
        Integer userId = 1;
        List<Loan> loanList = List.of(Loan.builder()
                .userId(1)
                .bookId(2)
                .build());
        List<Book> bookList = List.of();
        List<BookDTO> bookDTOList = List.of();
        //When:
        when(userService.findLoanPerUser(userId)).thenReturn(loanList);
        when(bookRepository.findAll()).thenReturn(bookList);
        //Then:
        List<BookDTO> result = userService.findBookPerUser(userId);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Find Book Per User Wrong Path Variable")
    public void findBookPerUserWrongPathVariableTest() {
        String userId = "ñ";
        assertThrows(Exception.class, () -> {
            List<BookDTO> result = userService.findBookPerUser(Integer.parseInt(userId));
        });
    }

    @Test
    @DisplayName("Find Loan Per User")
    public void findLoanPerUserTest() {
        //Given:
        Integer userId = 1;
        List<Loan> loanList = List.of(Loan.builder()
                .userId(1)
                .build());
        //When:
        when(loanRepository.findLoanByUserId(Mockito.anyInt())).thenReturn(loanList);
        //Then:
        List<Loan> result = userService.findLoanPerUser(userId);
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Find Loan Per User No Content")
    public void findLoanPerUserNoContentTest() {
        //Given:
        Integer userId = 1;
        List<Loan> loanList = List.of();
        //When:
        when(loanRepository.findLoanByUserId(Mockito.anyInt())).thenReturn(loanList);
        //Then:
        List<Loan> result = userService.findLoanPerUser(userId);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Find Loan Per User Wrong Path Variable")
    public void findLoanPerUserWrongPathVariable() {
        String userId = "ñ";
        assertThrows(Exception.class, () -> {
            List<Loan> result = userService.findLoanPerUser(Integer.parseInt(userId));
        });
    }
}