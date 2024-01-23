package com.conanthelibrarian.librarymanagementsystem.controller;

import com.conanthelibrarian.librarymanagementsystem.dao.Book;
import com.conanthelibrarian.librarymanagementsystem.dto.BookDTO;
import com.conanthelibrarian.librarymanagementsystem.dto.LoanDTO;
import com.conanthelibrarian.librarymanagementsystem.service.BookService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    @InjectMocks
    BookController bookController;

    @Mock
    BookService bookService;

    @Test
    @DisplayName("Find All Book")
    public void findAllBookTest(){
        List<BookDTO> bookDTOList = List.of(new BookDTO(2, "title", "author", 1l, "genre", 3));
        when(bookService.findAllBook()).thenReturn(bookDTOList);
        ResponseEntity<List<BookDTO>> result = bookController.findAllBook();
        assertEquals("author", result.getBody().get(0).getAuthor());
    }

    @Test
    @DisplayName("Find All Book No Content Result")
    public void findAllBookNoContentTest(){
        List<BookDTO> bookDTOList = List.of();
        when(bookService.findAllBook()).thenReturn(bookDTOList);
        ResponseEntity<List<BookDTO>> result = bookController.findAllBook();
        assertTrue(result.getBody().isEmpty());
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    }

    @Test
    @DisplayName("Find Book By Id")
    public void findBookByIdTest(){
        String id = "1";
        Optional<BookDTO> optionalBookDTO = Optional.of(BookDTO.builder().author("author").build());
        when(bookService.findBookById(Mockito.anyInt())).thenReturn(optionalBookDTO);
        ResponseEntity<Optional<BookDTO>> result = bookController.findBookById(id);
        assertEquals("author", result.getBody().get().getAuthor());
    }

    @Test
    @DisplayName("Find Book By Id Not Found Result")
    public void findBookByIdNotFoundTest(){
        String id= "1";
        Optional<BookDTO> optionalBookDTO = Optional.empty();
        when(bookService.findBookById(Mockito.anyInt())).thenReturn(optionalBookDTO);
        ResponseEntity<Optional<BookDTO>> result = bookController.findBookById(id);
        assertTrue(result.getBody().isEmpty());
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    @DisplayName("Find Book By Id Wrong Path Variable")
    public void findBookByIdWrongPathVariableTest(){
        String id= "ñ";
        assertThrows(Exception.class,() -> {
            ResponseEntity<Optional<BookDTO>> result = bookController.findBookById(id);
        });
    }

    @Test
    @DisplayName("Create New Book")
    public void createNewBookTest(){
        BookDTO bookDTO = BookDTO.builder().author("author").build();
        ResponseEntity<String> result = bookController.createNewBook(bookDTO);
        assertEquals("New book saved", result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    @DisplayName("Create New Book Internal Server Error")
    public void createNewBookInternalServerErrorTest(){
        BookDTO bookDTO = BookDTO.builder().author("author").build();
        doThrow(new RuntimeException("this is an error")).when(bookService).createNewBook(bookDTO);
        ResponseEntity<String> result = bookController.createNewBook(bookDTO);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

    //TODO CORREGIR
    /*@Test
    @DisplayName("Modify Book")
    public void modifyBookTest(){
        String id = "1";
        Optional<BookDTO> optionalBookDTO = Optional.of(BookDTO.builder().author("author").build());
        BookDTO bookDTO = BookDTO.builder().author("author").build();
        when(bookService.findBookById(Mockito.anyInt())).thenReturn(optionalBookDTO);
        ResponseEntity<String> result = bookController.modifyBook(bookDTO);
        assertEquals("Book modified", result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }*/

    /*@Test
    @DisplayName("Modify Book Not Found")
    public void modifyBookNotFoundTest(){
        String id= "1";
        Optional<BookDTO> optionalBookDTO = Optional.empty();
        BookDTO bookDTO = new BookDTO();
        when(bookService.findBookById(Mockito.anyInt())).thenReturn(optionalBookDTO);
        ResponseEntity<String> result = bookController.modifyBook(bookDTO);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }*/

    /*@Test
    @DisplayName("Modify Book Internal Server Error")
    public void modifyBookInternalServerErrorTest(){
        String id= "ñ";
        BookDTO bookDTO = BookDTO.builder().author("author").build();
        ResponseEntity<String> result = bookController.modifyBook(bookDTO);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }*/

    @Test
    @DisplayName("Delete Book")
    public void deleteBookTest(){
        String id= "1";
        BookDTO bookDTO = BookDTO.builder().author("author").build();
        ResponseEntity<String> result = bookController.deleteBook(id);
        assertEquals("Book deleted", result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    @DisplayName("Delete Book Internal Server Error")
    public void deleteBookInternalServerErrorTest(){
        String id= "ñ";
        BookDTO bookDTO = BookDTO.builder().author("author").build();
        ResponseEntity<String> result = bookController.deleteBook(id);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

    @Test
    @DisplayName("Find Book By Genre")
    public void findBookByGenreTest(){
        String genre = "genre";
        List<BookDTO> bookDTOList = List.of(BookDTO.builder().author("author").build());
        when(bookService.findBookByGenre(genre)).thenReturn(bookDTOList);
        ResponseEntity<List<BookDTO>> result = bookController.findBookByGenre(genre);
        assertEquals("author", result.getBody().get(0).getAuthor());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    @DisplayName("Find Book By Genre No Content")
    public void findBookByGenreNotFoundTest(){
        String genre = "genre";
        List<BookDTO> bookDTOList = List.of();
        when(bookService.findBookByGenre(genre)).thenReturn(bookDTOList);
        ResponseEntity<List<BookDTO>> result = bookController.findBookByGenre(genre);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    @DisplayName("Find Book By Genre Wrong Path Variable")
    public void findBookByGenreWrongPathVariableTest(){
        String genre= "ñ";
        List<BookDTO> bookDTOList = List.of();
        when(bookService.findBookByGenre(genre)).thenReturn(bookDTOList);
        ResponseEntity<List<BookDTO>> result = bookController.findBookByGenre(genre);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    @DisplayName("Find Book In Loan")
    public void findBookInLoanTest(){
    //Given:
        List<BookDTO> bookDTOList = List.of(BookDTO.builder().author("author").build());
    //When:
        when(bookService.findBookInLoan()).thenReturn(bookDTOList);
    //Then:
        ResponseEntity<List<BookDTO>> result = bookController.findBookInLoan();
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("author", result.getBody().get(0).getAuthor());
    }

    @Test
    @DisplayName("Find Book In Loan No Content")
    public void findBookInLoanNoContentTest(){
        //Given:
        List<BookDTO> bookDTOList = List.of();
        //When:
        when(bookService.findBookInLoan()).thenReturn(bookDTOList);
        //Then:
        ResponseEntity<List<BookDTO>> result = bookController.findBookInLoan();
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    }

    @Test
    @DisplayName("Find Available Book")
    public void findAvailableBookTest(){
        //Given:
        String bookId = "1";
        //When:
        when(bookService.isBookAvailable(Mockito.anyInt())).thenReturn(true);
        //Then:
        ResponseEntity<Boolean> result = bookController.findAvailableBook(bookId);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    @DisplayName("Find Available Book Not Found")
    public void findAvailableBookNotFoundTest(){
        //Given:
        String bookId = "1";
        //When:
        when(bookService.isBookAvailable(Mockito.anyInt())).thenReturn(false);
        //Then:
        ResponseEntity<Boolean> result = bookController.findAvailableBook(bookId);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    @DisplayName("Find Available Book Wrong Path Variable")
    public void findAvailableBookWrongPathVariableTest(){
        String bookId = "ñ";
        assertThrows(Exception.class,() -> {
            ResponseEntity<Boolean> result = bookController.findAvailableBook(bookId);
        });
    }

    @Test
    @DisplayName("Filter Book")
    public void filterBookTest(){
        //Given:
        String parameter = "parameter";
        List<BookDTO> bookDTOList = List.of(BookDTO.builder().author("author").build());
        //When:
        when(bookService.filterBook(Mockito.any())).thenReturn(bookDTOList);
        //Then:
        ResponseEntity<List<BookDTO>> result = bookController.filterBook(parameter);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    @DisplayName("Filter Book No Content")
    public void filterBookNoContentTest(){
        //Given:
        String parameter = "parameter";
        List<BookDTO> bookDTOList = List.of();
        //When:
        when(bookService.filterBook(Mockito.any())).thenReturn(bookDTOList);
        //Then:
        ResponseEntity<List<BookDTO>> result = bookController.filterBook(parameter);
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    }

    @Test
    @DisplayName("Filter Book Wrong Path Variable")
    public void filterBookWrongPathVariableTest(){
        //Given:
        String parameter = "ñ";
        List<BookDTO> bookDTOList = List.of();
        //When:
        when(bookService.filterBook(Mockito.any())).thenReturn(bookDTOList);
        //Then:
        ResponseEntity<List<BookDTO>> result = bookController.filterBook(parameter);
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    }

    @Test
    @DisplayName("Open New Loan If Available")
    public void openNewLoanIfAvailableTest(){
        //Given:
        String bookId = "1";
        String userId = "2";
        LoanDTO loanDTO = LoanDTO.builder()
                .loanId(3)
                .userId(2)
                .bookId(1)
                .build();

        //When:
        when(bookService.isBookAvailable(Mockito.anyInt())).thenReturn(true);
        //Then:
        ResponseEntity<String> result = bookController.openNewLoanIfAvailable(bookId, userId, loanDTO);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    @DisplayName("Open New Loan If Available Not Found")
    public void openNewLoanIfAvailableNotFoundTest(){
        //Given:
        String bookId = "1";
        String userId = "2";
        LoanDTO loanDTO = LoanDTO.builder()
                .loanId(3)
                .userId(2)
                .bookId(1)
                .build();

        //When:
        when(bookService.isBookAvailable(Mockito.anyInt())).thenReturn(false);
        //Then:
        ResponseEntity<String> result = bookController.openNewLoanIfAvailable(bookId, userId, loanDTO);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    @DisplayName("Open New Loan If Available Wrong Path Variable")
    public void openNewLoanIfAvailableWrongPathVariableTest(){
        //When:
        String bookId = "ñ";
        String userId = "ñ";
        LoanDTO loanDTO = new LoanDTO();
        //Then:
        ResponseEntity<String> result = bookController.openNewLoanIfAvailable
                (bookId, userId, loanDTO);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

    @Test
    @DisplayName("Delete Loan If Available")
    public void deleteLoanIfAvailableTest(){
        //Given:
        String bookId = "1";
        String userId = "2";
        //When:
        when(bookService.isBookAvailable(Mockito.anyInt())).thenReturn(true);
        //Then:
        ResponseEntity<String> result = bookController.deleteLoanIfAvailable(bookId, userId);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    @DisplayName("Delete Loan If Available Not Found")
    public void deleteLoanIfAvailableNotFoundTest(){
        //Given:
        String bookId = "1";
        String userId = "2";
        //When:
        when(bookService.isBookAvailable(Mockito.anyInt())).thenReturn(false);
        //Then:
        ResponseEntity<String> result = bookController.deleteLoanIfAvailable(bookId, userId);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }


    @Test
    @DisplayName("Delete Loan If Available Wrong Path Variable")
    public void deleteLoanIfAvailableWrongPathVariableTest(){
        //Given:
        String bookId = "ñ";
        String userId = "ñ";
        //Then:
        ResponseEntity<String> result = bookController.deleteLoanIfAvailable(bookId,userId);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

    @Test
    @DisplayName("Reduce Available Stock")
    public void reduceAvailableStockTest(){
        //Given:
        String bookId = "1";
        //Then:
        ResponseEntity<String> result = bookController.reduceAvailableStock(bookId);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    @DisplayName("Reduce Available Stock Internal Server Error")
    public void reduceAvailableInternalServerErrorTest(){
        //Given:
        String bookId = "ñ";
        //Then:
        ResponseEntity<String> result = bookController.reduceAvailableStock(bookId);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

    @Test
    @DisplayName("Increase Available Stock")
    public void increaseAvailableStockTest(){
        //Given:
        String bookId = "1";
        //Then:
        ResponseEntity<String> result = bookController.increaseAvailableStock(bookId);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    @DisplayName("Increase Available Stock Internal Server Error")
    public void increaseAvailableInternalServerErrorTest(){
        //Given:
        String bookId = "ñ";
        //Then:
        ResponseEntity<String> result = bookController.increaseAvailableStock(bookId);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }
}