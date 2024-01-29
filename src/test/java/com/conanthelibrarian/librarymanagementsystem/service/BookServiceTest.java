package com.conanthelibrarian.librarymanagementsystem.service;

import com.conanthelibrarian.librarymanagementsystem.dao.Book;
import com.conanthelibrarian.librarymanagementsystem.dto.BookDTO;
import com.conanthelibrarian.librarymanagementsystem.dto.LoanDTO;
import com.conanthelibrarian.librarymanagementsystem.mapper.BookMapper;
import com.conanthelibrarian.librarymanagementsystem.repository.BookRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @InjectMocks
    BookService bookService;

    @Mock
    BookRepository bookRepository;

    @Mock
    BookMapper bookMapper;

    @Mock
    LoanService loanService;


    @Test
    @DisplayName("Find All Book")
    public void findAllBookTest() {
        //Given:
        List<Book> bookList = List.of(Book.builder()
                .bookId(1)
                .build());
        //When:
        when(bookRepository.findAll()).thenReturn(bookList);
        //Then:
        List<BookDTO> result = bookService.findAllBook();
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Find All Book No Content")
    public void findAllBookNoContentTest() {
        //Given:
        List<Book> bookList = List.of();
        //When:
        when(bookRepository.findAll()).thenReturn(bookList);
        //Then:
        List<BookDTO> result = bookService.findAllBook();
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Find Book By Id")
    public void findBookByIdTest() {
        //Given:
        Integer bookId = 1;
        Book book = new Book();
        BookDTO bookDTO = BookDTO.builder()
                .bookId(1)
                .author("author")
                .build();
        Optional<Book> bookOptional = Optional.of(book);
        //When:
        when(bookRepository.findById(bookId)).thenReturn(bookOptional);
        when(bookMapper.bookToBookDTO(any(Book.class))).thenReturn(bookDTO);
        //Then:
        Optional<BookDTO> result = bookService.findBookById(bookId);
        assertEquals("author", result.get().getAuthor());
        Mockito.verify(bookMapper).bookToBookDTO(book);
    }

    @Test
    @DisplayName("Find Book By Id Not Found")
    public void findBookByIdNotFoundTest() {
        //When:
        Integer bookId = 1;
        Optional<Book> bookOptional = Optional.empty();
        //Given:
        when(bookRepository.findById(bookId)).thenReturn(bookOptional);
        //Then:
        Optional<BookDTO> result = bookService.findBookById(bookId);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Find Book By Id Wrong Path Variable")
    public void findBookByIdWrongPathVariableTest() {
        String bookId = "ñ";
        assertThrows(Exception.class, () -> {
            Optional<BookDTO> result = bookService.findBookById(Integer.parseInt(bookId));
        });
    }

    @Test
    @DisplayName("Create New Book")
    public void createNewBookTest() {
        //Given:
        Book book = new Book();
        BookDTO bookDTO = BookDTO.builder()
                .bookId(1).build();
        //When:
        when(bookMapper.bookDTOToBook(any(BookDTO.class))).thenReturn(book);
        //Then:
        bookService.createNewBook(bookDTO);
        verify(bookRepository, times(1)).save(book);
        Mockito.verify(bookMapper).bookDTOToBook(bookDTO);
    }

    @Test
    @DisplayName("Modify Book")
    public void modifyBookTest() {
        //Given:
        Integer bookId = 1;
        BookDTO bookDTO = BookDTO.builder()
                .bookId(1)
                .build();
        Optional<Book> optionalBook = Optional.of(Book.builder()
                .bookId(1)
                .build());
        //When:
        when(bookRepository.findById(bookId)).thenReturn(optionalBook);
        //Then:
        bookService.modifyBook(bookDTO);
        verify(bookRepository, times(1)).save(Mockito.any());
    }

    @Test
    @DisplayName("Modify Book Not Found")
    public void modifyBookTestNoFoundTest() {
        //Given:
        BookDTO bookDTO = BookDTO.builder()
                .bookId(1)
                .build();
        Optional<Book> optionalBookDTO = Optional.empty();
        //When:
        when(bookRepository.findById(Mockito.anyInt())).thenReturn(optionalBookDTO);
        //Then:
        bookService.modifyBook(bookDTO);
        Mockito.verify(bookRepository, never()).save(Mockito.any());
    }

    @Test
    @DisplayName("Delete Book")
    public void deleteBookTest() {
        Integer bookId = 1;
        bookService.deleteBookById(bookId);
        verify(bookRepository, times(1)).deleteById(bookId);
    }

    @Test
    @DisplayName("Delete Book Wrong Path Variable")
    public void deleteBookWrongPathVariableTest() {
        String bookId = "ñ";
        assertThrows(Exception.class, () -> {
            bookService.deleteBookById(Integer.parseInt(bookId));
        });
    }

    @Test
    @DisplayName("Find Book by Genre")
    public void findBookByGenreTest() {
        //Given:
        String stringGenre = "stringGenre";
        List<BookDTO> bookDTOList = List.of(BookDTO.builder()
                .bookId(1)
                .author("author")
                .build());
        //When:
        when(bookRepository.findBookByGenre(stringGenre)).thenReturn(bookDTOList);
        //Then:
        List<BookDTO> result = bookService.findBookByGenre(stringGenre);
        assertEquals("author", result.get(0).getAuthor());
    }

    @Test
    @DisplayName("Find Book by Genre No Content")
    public void findBookByGenreNoContestTest() {
        String stringGenre = "stringGenre";
        List<BookDTO> bookDTOList = List.of();
        List<BookDTO> result = bookService.filterBook(stringGenre);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Find Book In Loan")
    public void findBookInLoanTest() {
        //Given:
        List<BookDTO> bookDTOList = List.of(BookDTO.builder()
                .bookId(1)
                .author("author")
                .build());
        //When:
        when(bookRepository.findBooksInLoan()).thenReturn(bookDTOList);
        //Then:
        List<BookDTO> result = bookService.findBookInLoan();
        assertEquals("author", result.get(0).getAuthor());
    }

    @Test
    @DisplayName("Find Book In Loan No Content")
    public void findBookInLoanNoContentTest() {
        //Given:
        List<BookDTO> bookDTOList = List.of();
        //When:
        when(bookRepository.findBooksInLoan()).thenReturn(bookDTOList);
        //Then:
        List<BookDTO> result = bookService.findBookInLoan();
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Open New Loan And Reduce Stock If Available")
    public void openNewLoanAndReduceStockIfAvailableTest() {
        //Given:
        Integer bookId = 1;
        Integer userId = 2;
        Optional<Book> bookOptional = Optional.of(Book.builder()
                .bookId(1)
                .availableCopies(3)
                .build());
        List<LoanDTO> loanDTOList = List.of(LoanDTO.builder()
                .loanId(3)
                .bookId(1)
                .userId(2)
                .build());
        LoanDTO loanDTO = LoanDTO.builder()
                .loanId(3)
                .userId(2)
                .bookId(1)
                .build();
        //When:
        when(bookRepository.findById(Mockito.anyInt())).thenReturn(bookOptional);
        when(loanService.findAllLoan()).thenReturn(loanDTOList);
        //Then:
        bookService.openNewLoanAndReduceStockIfAvailable(bookId, userId, loanDTO);
        assertTrue(bookService.isBookAvailable(bookId));
        assertEquals(3, bookService.findLoanId(bookId, userId));
        Mockito.verify(loanService).createNewLoan(loanDTO);
        Mockito.verify(bookRepository).save(bookOptional.get());
    }

    @Test
    @DisplayName("Open New Loan And Reduce Stock If Available No Book Available")
    public void openNewLoanAndReduceStockIfAvailableNoBookAvailableTest() {
        //Given:
        Integer bookId = 1;
        Integer userId = 2;
        Optional<Book> bookOptional = Optional.of(Book.builder()
                .bookId(1)
                .availableCopies(0)
                .build());
        List<LoanDTO> loanDTOList = List.of(LoanDTO.builder()
                .loanId(3)
                .bookId(1)
                .userId(2)
                .build());
        LoanDTO loanDTO = LoanDTO.builder()
                .loanId(3)
                .userId(2)
                .bookId(1)
                .build();
        //When:
        when(bookRepository.findById(Mockito.anyInt())).thenReturn(bookOptional);
        //Then:
        bookService.openNewLoanAndReduceStockIfAvailable(bookId, userId, loanDTO);
        assertFalse(bookService.isBookAvailable(bookId));
        Mockito.verify(loanService, never()).createNewLoan(loanDTO);
        Mockito.verify(bookRepository, never()).save(bookOptional.get());
    }

    @Test
    @DisplayName("Open New Loan And Reduce Stock If Available Wrong Path Variable")
    public void openNewLoanAndReduceStockIfAvailableWrongPathVariableTest() {
        String bookId = "ñ";
        Integer userId = 2;
        LoanDTO loanDTO = new LoanDTO();
        assertThrows(Exception.class, () -> {
            bookService.openNewLoanAndReduceStockIfAvailable(Integer.parseInt(bookId), userId, loanDTO);
        });
    }

    @Test
    @DisplayName("Open New Loan And Reduce Stock If Available Not Found")
    public void openNewLoanAndReduceStockIfAvailableNotFoundTest() {
        //Given:
        Integer bookId = 1;
        Integer userId = 2;
        Optional<Book> bookOptional = Optional.empty();
        LoanDTO loanDTO = LoanDTO.builder()
                .loanId(3)
                .userId(2)
                .bookId(1)
                .build();
        //When:
        when(bookRepository.findById(Mockito.anyInt())).thenReturn(bookOptional);
        //Then:
        bookService.openNewLoanAndReduceStockIfAvailable(bookId, userId, loanDTO);
        assertFalse(bookService.isBookAvailable(bookId));
    }


    @Test
    @DisplayName("Delete Loan And Increase Stock If Available")
    public void deleteLoanAndIncreaseStockIfAvailableTest() {
        //Given:
        Integer bookId = 1;
        Integer userId = 2;
        Optional<Book> bookOptional = Optional.of(Book.builder()
                .bookId(1)
                .availableCopies(3)
                .build());
        List<LoanDTO> loanDTOList = List.of(LoanDTO.builder()
                .loanId(3)
                .bookId(1)
                .userId(2)
                .build());
        //When:
        when(bookRepository.findById(Mockito.anyInt())).thenReturn(bookOptional);
        when(loanService.findAllLoan()).thenReturn(loanDTOList);
        //Then:
        bookService.deleteLoanAndIncreaseStockIfAvailable(bookId, userId);
        assertTrue(bookService.isBookAvailable(bookId));
        assertEquals(3, bookService.findLoanId(bookId, userId));
        Mockito.verify(loanService).deleteLoanById(Mockito.anyInt());
        Mockito.verify(bookRepository).save(bookOptional.get());
    }

    @Test
    @DisplayName("Delete Loan And Increase Stock If Available No Book Available")
    public void deleteLoanAndIncreaseStockIfAvailableNoBookAvailableTest() {
        //Given:
        Integer bookId = 1;
        Integer userId = 2;
        Optional<Book> bookOptional = Optional.of(Book.builder()
                .bookId(1)
                .availableCopies(0)
                .build());
        List<LoanDTO> loanDTOList = List.of(LoanDTO.builder()
                .loanId(3)
                .bookId(1)
                .userId(2)
                .build());
        //When:
        when(bookRepository.findById(Mockito.anyInt())).thenReturn(bookOptional);
        //Then:
        bookService.deleteLoanAndIncreaseStockIfAvailable(bookId, userId);
        assertFalse(bookService.isBookAvailable(bookId));
        Mockito.verify(loanService, never()).deleteLoanById(bookId);
        Mockito.verify(bookRepository, never()).save(bookOptional.get());
    }

    @Test
    @DisplayName("Delete Loan And Increase Stock If Available Wrong Path Variable")
    public void deleteLoanAndIncreaseStockIfAvailableWrongPathVariableTest() {
        String bookId = "ñ";
        Integer userId = 2;
        LoanDTO loanDTO = new LoanDTO();
        assertThrows(Exception.class, () -> {
            bookService.deleteLoanAndIncreaseStockIfAvailable(Integer.parseInt(bookId), userId);
        });
    }

    @Test
    @DisplayName("Delete Loan And Increase Stock If  Not Found")
    public void deleteLoanAndIncreaseStockIfAvailableNotFoundTest() {
        //Given:
        Integer bookId = 1;
        Integer userId = 2;
        Optional<Book> bookOptional = Optional.empty();
        //When:
        when(bookRepository.findById(Mockito.anyInt())).thenReturn(bookOptional);
        //Then:
        bookService.deleteLoanAndIncreaseStockIfAvailable(bookId, userId);
        assertFalse(bookService.isBookAvailable(bookId));
    }

    @Test
    @DisplayName("Filter Book")
    public void filterBookTest() {
        //Given:
        String parameter = "parameter";
        Book book = Book.builder()
                .bookId(1)
                .author("parameter")
                .title("parameter")
                .genre("parameter")
                .build();
        BookDTO bookDTO = BookDTO.builder()
                .bookId(1)
                .author("parameter")
                .build();
        List<Book> bookList = List.of(book);
        List<BookDTO> bookDTOList = List.of(bookDTO);
        //When:
        when(bookRepository.findAll()).thenReturn(bookList);
        when(bookMapper.bookToBookDTO(any(Book.class))).thenReturn(bookDTO);
        //Then:
        List<BookDTO> result = bookService.filterBook(parameter);
        assertEquals(1, result.size());
        assertEquals(parameter, result.get(0).getAuthor());
    }

    @Test
    @DisplayName("Filter Book No Content")
    public void filterBookNoContentTest() {
        //Given:
        String parameter = "parameter";
        Book book = new Book();
        BookDTO bookDTO = new BookDTO();
        List<Book> bookList = List.of(book);
        //When:
        when(bookRepository.findAll()).thenReturn(bookList);
        //Then:
        List<BookDTO> result = bookService.filterBook(parameter);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Filter Book No Parameter")
    public void filterBookNoParameterTest() {
        //Given:
        String parameter = "parameter";
        Book book = Book.builder()
                .bookId(1)
                .author("author")
                .build();
        BookDTO bookDTO = BookDTO.builder()
                .bookId(1)
                .author("author")
                .build();
        List<Book> bookList = List.of(book);
        List<BookDTO> bookDTOList = List.of(bookDTO);
        //When:
        when(bookRepository.findAll()).thenReturn(bookList);
        //Then:
        List<BookDTO> result = bookService.filterBook(parameter);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Is Book Available")
    public void isBookAvailableTest() {
        //Given:
        Integer bookId = 1;
        Optional<Book> bookOptional = Optional.of(Book.builder()
                .bookId(1)
                .availableCopies(3)
                .build());
        //When:
        when(bookRepository.findById(Mockito.anyInt())).thenReturn(bookOptional);
        //Then:
        boolean result = bookService.isBookAvailable(bookId);
        assertTrue(result);
    }

    @Test
    @DisplayName("Is Book Available Not Available")
    public void isBookAvailableNotAvailableTest() {
        //Given:
        Integer bookId = 1;
        Optional<Book> bookOptional = Optional.of(Book.builder()
                .bookId(1)
                .availableCopies(0)
                .build());
        //When:
        when(bookRepository.findById(bookId)).thenReturn(bookOptional);
        //Then:
        boolean result = bookService.isBookAvailable(bookId);
        assertFalse(result);
    }

    @Test
    @DisplayName("Is Book Available Wrong Path Variable")
    public void isBookAvailableWrongPathVariableTest() {
        String bookId = "ñ";
        assertThrows(Exception.class, () -> {
            Optional<BookDTO> result = bookService.findBookById(Integer.parseInt(bookId));
        });
    }

    @Test
    @DisplayName("Is Book Available Not Found")
    public void isBookAvailableNotFoundTest() {
        //Given:
        Integer bookId = 1;
        Optional<Book> bookOptional = Optional.empty();
        //When:
        when(bookRepository.findById(Mockito.anyInt())).thenReturn(bookOptional);
        //Then:
        boolean result = bookService.isBookAvailable(bookId);
        assertFalse(result);
    }


    @Test
    @DisplayName("Create New Loan If Available")
    public void createNewLoanIfAvailable() {
        //Given
        Integer bookId = 1;
        Integer userId = 2;
        LoanDTO loanDTO = LoanDTO.builder()
                .loanId(3)
                .userId(2)
                .bookId(1)
                .build();
        //Then
        bookService.createNewLoanIfAvailable(bookId, userId, loanDTO);
        Mockito.verify(loanService).createNewLoan(loanDTO);
    }


    @Test
    @DisplayName("Create New Loan If Available Wrong Path Variable")
    public void createNewLoanIfAvailableWrongPathVariableBookIdTest() {
        //Given
        String bookId = "ñ";
        Integer userId = 2;
        LoanDTO loanDTO = LoanDTO.builder()
                .loanId(3)
                .userId(2)
                .bookId(1)
                .build();
        //Then:
        assertThrows(Exception.class, () -> {
            bookService.createNewLoanIfAvailable(Integer.parseInt(bookId), userId, loanDTO);
        });
    }

    @Test
    @DisplayName("Reduce Book Availability")
    public void reduceBookAvailabilityTest() {
        //Given:
        Integer bookId = 1;
        Optional<Book> bookOptional = Optional.of(Book.builder()
                .bookId(1)
                .availableCopies(2)
                .build());
        //When:
        when(bookRepository.findById(bookId)).thenReturn(bookOptional);
        //Then:
        bookService.reduceAvailableStock(bookId);
        Mockito.verify(bookRepository).save(bookOptional.get());
    }

    @Test
    @DisplayName("Reduce Book Availability Not Found")
    public void reduceBookAvailabilityTestNotFound() {
        //given:
        Integer bookId = 160;
        Optional<Book> bookOptional = Optional.empty();
        //When:
        when(bookRepository.findById(bookId)).thenReturn(bookOptional);
        //Then:
        bookService.reduceAvailableStock(bookId);
        Mockito.verify(bookRepository, never()).save(Mockito.any());
    }

    @Test
    @DisplayName("Reduce Book Availability But Not Available")
    public void reduceBookAvailabilityNotAvailable() {
        //Given:
        Integer bookId = 1;
        Optional<Book> bookOptional = Optional.of(Book.builder()
                .bookId(1)
                .availableCopies(0)
                .build());
        //When:
        when(bookRepository.findById(bookId)).thenReturn(bookOptional);
        //Then:
        bookService.reduceAvailableStock(bookId);
        Mockito.verify(bookRepository, never()).save(Mockito.any());
    }

    @Test
    @DisplayName("find LoanId")
    public void findLoanIdTest() {
        //Given:
        Integer bookId = 1;
        Integer userId = 2;
        List<LoanDTO> loanDTOList = List.of(LoanDTO.builder()
                .loanId(3)
                .bookId(1)
                .userId(2)
                .build());
        //When:
        when(loanService.findAllLoan()).thenReturn(loanDTOList);
        //Then:
        Integer result = bookService.findLoanId(bookId, userId);
        assertEquals(3, result);
    }

    @Test
    @DisplayName("find LoanId No Content")
    public void findLoanIdNoContentTest() {
        //Given:
        Integer bookId = 1;
        Integer userId = 2;
        List<LoanDTO> loanDTOList = List.of();
        //When:
        when(loanService.findAllLoan()).thenReturn(loanDTOList);
        //Then:
        Integer result = bookService.findLoanId(bookId, userId);
        assertNull(result);
    }

    @Test
    @DisplayName("find LoanId Wrong Path Variable bookId")
    public void findLoanIdWrongPathVariableBookIdTest() {
        //Given:
        Integer bookId = 1;
        Integer userId = 2;
        List<LoanDTO> loanDTOList = List.of(LoanDTO.builder()
                .loanId(3)
                .bookId(2)
                .userId(2)
                .build());
        //When:
        when(loanService.findAllLoan()).thenReturn(loanDTOList);
        //Then:
        Integer result = bookService.findLoanId(bookId, userId);
        assertNull(result);
    }

    @Test
    @DisplayName("find LoanId Wrong Path Variable userId")
    public void findLoanIdWrongPathVariableUserIdTest() {
        //Given:
        Integer bookId = 1;
        Integer userId = 2;
        List<LoanDTO> loanDTOList = List.of(LoanDTO.builder()
                .loanId(3)
                .bookId(1)
                .userId(3)
                .build());
        //When:
        when(loanService.findAllLoan()).thenReturn(loanDTOList);
        //Then:
        Integer result = bookService.findLoanId(bookId, userId);
        assertNull(result);
    }

    @Test
    @DisplayName("Increase Book Availability")
    public void increaseBookAvailabilityTest() {
        //Given:
        Integer bookId = 1;
        Optional<Book> bookOptional = Optional.of(Book.builder()
                .bookId(1)
                .availableCopies(2)
                .build());
        //When:
        when(bookRepository.findById(bookId)).thenReturn(bookOptional);
        //Then:
        bookService.increaseAvailableStock(bookId);
        Mockito.verify(bookRepository).save(bookOptional.get());
    }

    @Test
    @DisplayName("Increase Book Availability Not Found")
    public void increaseBookAvailabilityTestNotFound() {
        //Given:
        Integer bookId = 160;
        Optional<Book> bookOptional = Optional.empty();
        //When:
        when(bookRepository.findById(bookId)).thenReturn(bookOptional);
        //Then:
        bookService.increaseAvailableStock(bookId);
        Mockito.verify(bookRepository, never()).save(Mockito.any());
    }

    @Test
    @DisplayName("Increase Book Availability But Not Available")
    public void increaseBookAvailabilityNotAvailable() {
        //Given:
        Integer bookId = 1;
        Optional<Book> bookOptional = Optional.of(Book.builder()
                .bookId(1)
                .availableCopies(0)
                .build());
        //When:
        when(bookRepository.findById(bookId)).thenReturn(bookOptional);
        //Then:
        bookService.increaseAvailableStock(bookId);
        Mockito.verify(bookRepository, never()).save(Mockito.any());
    }
}