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

    //todo comprobar la cobertura


    @Test
    @DisplayName("BookDTO to Book")
    public void bookDTOToBook(){
        BookDTO bookDTO = new BookDTO(1, "title", "author", 1L, "genre", 3);
        Book book = bookMapper.bookDTOToBook(bookDTO);
        assertEquals("title", bookDTO.getTitle());
    }

    @Test
    @DisplayName("Book to BookDTO")
    public void bookToBookDTO(){
        Book book = new Book(2, "title", "author", 1L, "genre", 3);
        BookDTO bookDTO = bookMapper.bookToBookDTO(book);
        assertEquals("author", book.getAuthor());
    }

}