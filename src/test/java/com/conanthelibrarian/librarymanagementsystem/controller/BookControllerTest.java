package com.conanthelibrarian.librarymanagementsystem.controller;

import com.conanthelibrarian.librarymanagementsystem.dto.BookDTO;
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
    @DisplayName("Find Book By Id Wrong Path Variable Result")
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

    @Test
    @DisplayName("Modify Book")
    public void modifyBookTest(){
        String id = "1";
        Optional<BookDTO> optionalBookDTO = Optional.of(BookDTO.builder().author("author").build());
        BookDTO bookDTO = BookDTO.builder().author("author").build();
        when(bookService.findBookById(Mockito.anyInt())).thenReturn(optionalBookDTO);
        ResponseEntity<String> result = bookController.modifyBook(id, bookDTO);
        assertEquals("Book modified", result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    @DisplayName("Modify Book Not Found")
    public void modifyBookNotFoundTest(){
        String id= "1";
        Optional<BookDTO> optionalBookDTO = Optional.empty();
        BookDTO bookDTO = new BookDTO();
        when(bookService.findBookById(Mockito.anyInt())).thenReturn(optionalBookDTO);
        ResponseEntity<String> result = bookController.modifyBook(id,bookDTO);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    @DisplayName("Modify Book Internal Server Error")
    public void modifyBookInternalServerErrorTest(){
        String id= "ñ";
        BookDTO bookDTO = BookDTO.builder().author("author").build();
        ResponseEntity<String> result = bookController.modifyBook(id,bookDTO);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

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
}