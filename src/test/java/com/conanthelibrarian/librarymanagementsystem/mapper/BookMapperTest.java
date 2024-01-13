package com.conanthelibrarian.librarymanagementsystem.mapper;

import com.conanthelibrarian.librarymanagementsystem.dao.Book;
import com.conanthelibrarian.librarymanagementsystem.dto.BookDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BookMapperTest {

    @InjectMocks
    BookMapper bookMapper;

    @Test
    @DisplayName("BookDTO to Book")
    public void bookDTOToBookTest(){
        BookDTO bookDTO = new BookDTO(1, "title", "author", 1L, "genre", 3);
        Book book = bookMapper.bookDTOToBook(bookDTO);
        assertEquals("title", bookDTO.getTitle());
    }

    @Test
    @DisplayName("BookDTO to Book Null")
    public void bookDTOToBookNullTest(){
        BookDTO bookDTO = null;
        Book book =bookMapper.bookDTOToBook(bookDTO);
        assertNull(book);
    }

    @Test
    @DisplayName("Book to BookDTO")
    public void bookToBookDTOTest(){
        Book book = new Book(2, "title", "author", 1L, "genre", 3);
        BookDTO bookDTO = bookMapper.bookToBookDTO(book);
        assertEquals("author", book.getAuthor());
    }

    @Test
    @DisplayName("Book to BookDTO Null")
    public void bookToBookDTONullTest(){
        Book book = null;
        BookDTO bookDTO = bookMapper.bookToBookDTO(book);
        assertNull(bookDTO);
    }

}