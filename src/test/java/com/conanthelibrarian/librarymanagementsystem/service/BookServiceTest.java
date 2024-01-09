package com.conanthelibrarian.librarymanagementsystem.service;

import com.conanthelibrarian.librarymanagementsystem.dao.Book;
import com.conanthelibrarian.librarymanagementsystem.dao.Loan;
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

import java.time.LocalDate;
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

    //todo versión negativa de todo: pasar la cobertura
    //todo List of en lugar del patrón singleton, con builder

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
    public void findBookByIdTest(){
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
    public void createNewBookTest(){
        Book book = new Book();
        BookDTO bookDTO = new BookDTO(2, "title", "author", 1l, "genre", 3);
        when(bookMapper.bookDTOToBook(any(BookDTO.class))).thenReturn(book);
        bookService.createNewBook(bookDTO);
        verify(bookRepository,times(1)).save(book);
        Mockito.verify(bookMapper).bookDTOToBook(bookDTO);
    }

    @Test
    @DisplayName("Modify Book If Optional Is Present")
    public void modifyBookTest(){
        Integer id= 1;
        Book book = new Book();
        BookDTO bookDTO = new BookDTO(2, "title", "author", 1l, "genre", 3);
        when(bookMapper.bookDTOToBook(any(BookDTO.class))).thenReturn(book);
        bookService.modifyBook(id, bookDTO);
        verify(bookRepository,times(1)).save(book);
        Mockito.verify(bookMapper).bookDTOToBook(bookDTO);
    }

    @Test
    @DisplayName("Delete Book")
    public void deleteBookTest(){
        Integer bookId = 1;
        bookService.deleteBookById(bookId);
        verify(bookRepository,times(1)).deleteById(bookId);
    }

    @Test
    @DisplayName("Find Book by Genre")
    public void findBookByGenreTest(){
        String stringGenre = "stringGenre";
        List<BookDTO> bookDTOList = List.of(new BookDTO(1, "title", "author", 1l, "genre", 2));
        when(bookRepository.findBookByGenre(stringGenre)).thenReturn(bookDTOList);
        assertEquals("author", bookService.findBookByGenre(stringGenre).get(0).getAuthor());
    }

    @Test
    @DisplayName("Find Book In Loan")
    public void findBookInLoanTest(){
        List<BookDTO> bookDTOList = List.of(new BookDTO(1, "title", "author", 1l, "genre", 2));
        when(bookRepository.findBooksInLoan()).thenReturn(bookDTOList);
        assertEquals("author", bookService.findBookInLoan().get(0).getAuthor());
    }

    //todo por aquí me he quedado


    //todo  CORREGIR!
    @Test
    @DisplayName("Filter Book")
    public void filterBookTest(){
        String parameter = "parameter";
        Book book = new Book();
        BookDTO bookDTO = new BookDTO();
        List<Book> bookList = List.of(new Book(71, "title", "author", 1l, "genre", 2));
        when(bookRepository.findAll()).thenReturn(bookList);
        when(bookMapper.bookToBookDTO(any(Book.class))).thenReturn(bookDTO);
        List<BookDTO> result = bookService.filterBook(parameter);
        assertEquals("author", bookService.filterBook(parameter).get(0).getAuthor());
        Mockito.verify(bookMapper).bookToBookDTO(book);
    }

    @Test
    @DisplayName("Open New Loan and Reduce Stock If Available")
    public void openNewLoanAndReduceStockIfAvailableTest(){
        //todo
    }

    @Test
    @DisplayName("Delete Loan And Increase Stock If Available")
    public void deleteLoanAndIncreaseStockIfAvailableTest(){
        //todo
    }

    @Test
    @DisplayName("Is Book Available")
    public void isBookAvailableTest(){
        Integer bookId = 1;
        assertTrue(bookService.isBookAvailable(bookId));
    }

    @Test
    @DisplayName("Create New Loan If Available")
    public void createNewLoanIfAvailableTest(){
        //todo
    }

    @Test
    @DisplayName("Reduce Book Quantity")
    public void reduceBookQuantityTest(){
        /*Integer bookId = 1;
        Book book = new Book();
        BookDTO bookDTO = new BookDTO(2, "title", "author", 1l, "genre", 3);
        Optional<Book> bookOptional = Optional.of(book);
        when(bookRepository.findById(bookId)).thenReturn(bookOptional);
        when(bookMapper.bookToBookDTO(any(Book.class))).thenReturn(bookDTO);
        Optional<BookDTO> result = bookService.findBookById(bookId);
        bookService.reduceBookQuantity(bookId);
        assertTrue(result.isPresent());*/

        /*Integer bookId = 1;
        Book book = new Book();
        BookDTO bookDTO = new BookDTO(2, "title", "author", 1l, "genre", 3);
        Optional<Book> bookOptional = Optional.of(book);
        when(bookRepository.findById(bookId)).thenReturn(bookOptional);
        when(bookMapper.bookToBookDTO(any(Book.class))).thenReturn(bookDTO);
        bookService.reduceBookQuantity(bookId);
        assertEquals("author", bookOptional.get().getAuthor());
        Mockito.verify(bookMapper).bookToBookDTO(book);*/
    }


}