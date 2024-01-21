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
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
//@MockitoSettings(strictness = Strictness.LENIENT)
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
        Integer bookId = 1;
        Book book = new Book();
        BookDTO bookDTO = new BookDTO(1, "title", "author", 1l, "genre", 3);
        Optional<Book> bookOptional = Optional.of(book);
        when(bookRepository.findById(bookId)).thenReturn(bookOptional);
        when(bookMapper.bookToBookDTO(any(Book.class))).thenReturn(bookDTO);
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
        Book book = new Book();
        BookDTO bookDTO = BookDTO.builder()
                .bookId(1).build();
        when(bookMapper.bookDTOToBook(any(BookDTO.class))).thenReturn(book);
        bookService.createNewBook(bookDTO);
        verify(bookRepository, times(1)).save(book);
        Mockito.verify(bookMapper).bookDTOToBook(bookDTO);
    }

    //todo CORREGIR
    @Test
    @DisplayName("Create New Book Internal Server Error")
    public void createNewBookInternalServerErrorTest() {
        /*Book book = Book.builder().build();
        BookDTO bookDTO = BookDTO.builder()
                .bookId(1).build();
        when(bookMapper.bookToBookDTO(any(Book.class))).thenReturn(bookDTO);
        doThrow(new RuntimeException("this is an error")).when(bookRepository).save(book);
        bookService.createNewBook(bookDTO);
        Mockito.verify(bookRepository, never()).save(Mockito.any());
        */
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
    @DisplayName("Modify Book Not Found")
    public void modifyBookTestNoFoundTest() {
        //Given:
        Integer bookId = 1;
        BookDTO bookDTO = new BookDTO();
        Optional<Book> optionalBookDTO = Optional.empty();
        //When:
        when(bookRepository.findById(bookId)).thenReturn(optionalBookDTO);
        //Then:
        bookService.modifyBook(bookId, bookDTO);
        Mockito.verify(bookRepository, never()).save(Mockito.any());
    }

    @Test
    @DisplayName("Modify Book Wrong Path Variable")
    public void modifyBookTestWrongPathVariable() {
        //Given:
        String bookId = "ñ";
        BookDTO bookDTO = new BookDTO();
        assertThrows(Exception.class, () -> {
            bookService.modifyBook(Integer.parseInt(bookId), bookDTO);
        });
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
        String stringGenre = "stringGenre";
        List<BookDTO> bookDTOList = List.of(new BookDTO(1, "title", "author", 1l, "genre", 2));
        when(bookRepository.findBookByGenre(stringGenre)).thenReturn(bookDTOList);
        assertEquals("author", bookService.findBookByGenre(stringGenre).get(0).getAuthor());
    }

    //todo CORREGIR
    @Test
    @DisplayName("Find Book by Genre No Content")
    public void findBookByGenreNoContestTest(){
        String stringGenre = "stringGenre";
        List<BookDTO> bookDTOList = List.of();
        when(bookRepository.findBookByGenre(stringGenre)).thenReturn(bookDTOList);
        bookService.filterBook(stringGenre);
        assertTrue(bookService.filterBook(stringGenre).isEmpty());
    }

    @Test
    @DisplayName("Find Book In Loan")
    public void findBookInLoanTest() {
        List<BookDTO> bookDTOList = List.of(new BookDTO(1, "title", "author", 1l, "genre", 2));
        when(bookRepository.findBooksInLoan()).thenReturn(bookDTOList);
        assertEquals("author", bookService.findBookInLoan().get(0).getAuthor());
    }

    @Test
    @DisplayName("Find Book In Loan No Content")
    public void findBookInLoanNoContentTest() {
        List<BookDTO> bookDTOList = List.of();
        when(bookRepository.findBooksInLoan()).thenReturn(bookDTOList);
        assertTrue(bookService.findBookInLoan().isEmpty());
    }

    @Test
    @DisplayName("Open New Loan And Reduce Stock If Available")
    public void openNewLoanAndReduceStockIfAvailableTest() {
        //Given:
        Integer bookId = 1;
        Integer userId = 2;
        LoanDTO loanDTO = LoanDTO.builder().loanId(3).build();
        boolean resultAvailable = true;
        Book book = Book.builder().author("author").build();
        BookDTO bookDTO = BookDTO.builder().author("author").build();
        Optional<Book> bookOptional = Optional.of(Book.builder().author("author").build());
        //When:
        when(bookRepository.findById(Mockito.anyInt())).thenReturn(bookOptional);
        when(bookMapper.bookToBookDTO(any(Book.class))).thenReturn(bookDTO);
        when(bookService.isBookAvailable(Mockito.anyInt())).thenReturn(resultAvailable);
        //Then:
        bookService.createNewLoanIfAvailable(bookId, userId, loanDTO);
        bookService.openNewLoanAndReduceStockIfAvailable(bookId, userId, loanDTO);
        Mockito.verify(loanService).createNewLoan(Mockito.any());

    }

    //todo deleteLoanAndIncreaseStockIfAvailable

    @Test
    @DisplayName("Delete Loan And Increase Stock If Available")
    public void deleteLoanAndIncreaseStockIfAvailable(){
        //Given:
        Integer bookId = 1;
        Integer userId = 2;
        //When:
        //Then:
        bookService.deleteLoanAndIncreaseStockIfAvailable(bookId, userId);
        Mockito.verify(bookService, never()).deleteBookById(bookId);
    }

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
        assertTrue(bookService.isBookAvailable(bookId));
    }

    @Test
    @DisplayName("Is Book Available Not Available")
    public void isBookAvailableNotAvailableTest() {
        Integer bookId = 1;
        Optional<Book> bookOptional = Optional.ofNullable(Book.builder()
                .bookId(1)
                .availableCopies(0)
                .build());
        //when:
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
    @DisplayName("Create New Loan If Available No Content bookId")
    public void createNewLoanIfAvailableNoContentTest(){
        //Given
        Integer bookId = 1;
        Integer userId = 2;
        LoanDTO loanDTO = new LoanDTO();
        //Then:
        bookService.createNewLoanIfAvailable(bookId, userId, loanDTO);
        Mockito.verify(loanService, never()).createNewLoan(Mockito.any());
    }
    @Test
    @DisplayName("Create New Loan If Available Wrong Path Variable bookId")
    public void createNewLoanIfAvailableWrongPathVariableBookIdTest(){
        //Given
        Integer bookId = 1;
        Integer userId = 2;
        LoanDTO loanDTO = LoanDTO.builder()
                .loanId(3)
                .userId(2)
                .bookId(4)
                .build();
        //Then:
        bookService.createNewLoanIfAvailable(bookId, userId, loanDTO);
        Mockito.verify(loanService, never()).createNewLoan(Mockito.any());
    }

    @Test
    @DisplayName("Create New Loan If Available Wrong Path Variable userId")
    public void createNewLoanIfAvailableWrongPathVariableUserIdTest(){
        //Given
        Integer bookId = 1;
        Integer userId = 2;
        LoanDTO loanDTO = LoanDTO.builder()
                .loanId(3)
                .userId(4)
                .bookId(1)
                .build();
        //Then:
        bookService.createNewLoanIfAvailable(bookId, userId, loanDTO);
        Mockito.verify(loanService, never()).createNewLoan(Mockito.any());
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
        //given:
        Integer bookId = 1;
        Optional<Book> bookOptional = Optional.ofNullable(Book.builder()
                .bookId(1)
                .availableCopies(0)
                .build());
        //when:
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