package com.conanthelibrarian.librarymanagementsystem.service;

import com.conanthelibrarian.librarymanagementsystem.dao.Book;
import com.conanthelibrarian.librarymanagementsystem.dto.BookDTO;
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
}