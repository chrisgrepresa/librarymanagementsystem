package com.conanthelibrarian.librarymanagementsystem.service;

import com.conanthelibrarian.librarymanagementsystem.dao.Book;
import com.conanthelibrarian.librarymanagementsystem.dto.BookDTO;
import com.conanthelibrarian.librarymanagementsystem.dto.LoanDTO;
import com.conanthelibrarian.librarymanagementsystem.mapper.BookMapper;
import com.conanthelibrarian.librarymanagementsystem.repository.BookRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.glassfish.jaxb.core.v2.TODO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.annotation.CreatedDate;

import java.util.*;
import java.util.stream.Collectors;

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
    @DisplayName("Find All Books")
    public void findAllBookTest() {
        Book book = new Book();
        BookDTO bookDTO = new BookDTO();
        List<Book> bookList = Collections.singletonList(book);
        when(bookRepository.findAll()).thenReturn(bookList);
        when(bookMapper.bookToBookDTO(any(Book.class))).thenReturn(bookDTO);
        List<BookDTO> result = bookService.findAllBook();
        assertEquals(1, result.size());
        assertEquals(bookDTO, result.get(0));
        Mockito.verify(bookMapper).bookToBookDTO(book);
    }

    @Test
    @DisplayName("Find Book By Id")
    public void findBookByIdTest() {
        Integer bookId = 1;
        Book book = new Book();
        BookDTO bookDTO = new BookDTO(2, "title", "author", 1l, "genre", 3);
        Optional<Book> bookOptional = Optional.of(book);
        when(bookRepository.findById(bookId)).thenReturn(bookOptional);
        when(bookMapper.bookToBookDTO(any(Book.class))).thenReturn(bookDTO);
        Optional<BookDTO> result = bookService.findBookById(bookId);
        assertEquals("author", result.get().getAuthor());
        Mockito.verify(bookMapper).bookToBookDTO(book);
    }

    @Test
    @DisplayName("Create New Book")
    public void createNewBookTest() {
        Book book = new Book();
        BookDTO bookDTO = new BookDTO(2, "title", "author", 1l, "genre", 3);
        when(bookMapper.bookDTOToBook(any(BookDTO.class))).thenReturn(book);
        bookService.createNewBook(bookDTO);
        verify(bookRepository, times(1)).save(book);
        Mockito.verify(bookMapper).bookDTOToBook(bookDTO);
    }

    @Test
    @DisplayName("Modify Book")
    public void modifyBookTest() {
        //Given:
        Integer bookId = 1;
        BookDTO bookDTO = new BookDTO();
        Optional<Book> optionalBookDTO = Optional.ofNullable(Book.builder()
                .bookId(1)
                .build());
        //When:
        when(bookRepository.findById(bookId)).thenReturn(optionalBookDTO);
        //Then:
        bookService.modifyBook(bookId, bookDTO);
        verify(bookRepository, times(1)).save(Mockito.any());
    }

    @Test
    @DisplayName("Delete Book")
    public void deleteBookTest() {
        Integer bookId = 1;
        bookService.deleteBookById(bookId);
        verify(bookRepository, times(1)).deleteById(bookId);
    }

    @Test
    @DisplayName("Find Book by Genre")
    public void findBookByGenreTest() {
        String stringGenre = "stringGenre";
        List<BookDTO> bookDTOList = List.of(new BookDTO(1, "title", "author", 1l, "genre", 2));
        when(bookRepository.findBookByGenre(stringGenre)).thenReturn(bookDTOList);
        assertEquals("author", bookService.findBookByGenre(stringGenre).get(0).getAuthor());
    }

    @Test
    @DisplayName("Find Book In Loan")
    public void findBookInLoanTest() {
        List<BookDTO> bookDTOList = List.of(new BookDTO(1, "title", "author", 1l, "genre", 2));
        when(bookRepository.findBooksInLoan()).thenReturn(bookDTOList);
        assertEquals("author", bookService.findBookInLoan().get(0).getAuthor());
    }

    //todo openNewLoanAndReduceStockIfAvailable
    //todo deleteLoanAndIncreaseStockIfAvailable

    //todo filterBook corregir
    /*@Test
    @DisplayName("Filter Book")
    public void filterBookTest() {
        //Given:
        String parameter = "parameter";
        List<Book> bookList = List.of(new Book(1, "title", "author", 1l, "genre", 2));
        //When:
        when(bookRepository.findAll()).thenReturn(bookList);
        when(bookMapper.bookToBookDTO(any(Book.class))).thenReturn(Mockito.any());
        //Then:
        List<BookDTO> result = bookService.filterBook(parameter);
        assertEquals("author", result.get(0).getAuthor());
        //Mockito.verify(bookMapper).bookToBookDTO(book);
    }*/

    @Test
    @DisplayName("Is Book Available")
    public void isBookAvailableTest() {
        Integer bookId = 1;
        boolean result = bookService.isBookAvailable(bookId);
        assertTrue(result);
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
    @DisplayName("Reduce Book Availability")
    public void reduceBookAvailabilityTest() {
        //Given:
        Integer bookId = 1;
        Optional<Book> bookOptional = Optional.ofNullable(Book.builder()
                .bookId(1)
                .availableCopies(2)
                .build());
        //When:
        when(bookRepository.findById(bookId)).thenReturn(bookOptional);
        //Then:
        bookService.reduceBookQuantity(bookId);
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
        bookService.reduceBookQuantity(bookId);
        Mockito.verify(bookRepository, never()).save(Mockito.any());
    }

    @Test
    @DisplayName("Reduce Book Availability But Not Available")
    public void reduceBookAvailabilityNotAvailable() {
        //given:
        Integer bookId = 1;
        Optional<Book> bookOptional = Optional.ofNullable(Book.builder()
                .bookId(1)
                .availableCopies(0)
                .build());
        //when:
        when(bookRepository.findById(bookId)).thenReturn(bookOptional);
        //Then:
        bookService.reduceBookQuantity(bookId);
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
    @DisplayName("Increase Book Availability")
    public void increaseBookAvailabilityTest() {
        //Given:
        Integer bookId = 1;
        Optional<Book> bookOptional = Optional.ofNullable(Book.builder()
                .bookId(1)
                .availableCopies(2)
                .build());
        //When:
        when(bookRepository.findById(bookId)).thenReturn(bookOptional);
        //Then:
        bookService.increaseBookQuantity(bookId);
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
        bookService.increaseBookQuantity(bookId);
        Mockito.verify(bookRepository, never()).save(Mockito.any());
    }

    @Test
    @DisplayName("Increase Book Availability But Not Available")
    public void increaseBookAvailabilityNotAvailable() {
        //given:
        Integer bookId = 1;
        Optional<Book> bookOptional = Optional.ofNullable(Book.builder()
                .bookId(1)
                .availableCopies(0)
                .build());
        //when:
        when(bookRepository.findById(bookId)).thenReturn(bookOptional);
        //Then:
        bookService.increaseBookQuantity(bookId);
        Mockito.verify(bookRepository, never()).save(Mockito.any());
    }

}