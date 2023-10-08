package com.conanthelibrarian.librarymanagementsystem.mapper;

import com.conanthelibrarian.librarymanagementsystem.dao.Book;
import com.conanthelibrarian.librarymanagementsystem.dao.User;
import com.conanthelibrarian.librarymanagementsystem.dto.BookDTO;
import com.conanthelibrarian.librarymanagementsystem.dto.UserDTO;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

    public Book bookDTOToBook(BookDTO bookDTO){
        if(bookDTO == null){
            return null;
        }
        Book book = new Book();
        book.setBookId(bookDTO.getBookId());
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        book.setIsbn(bookDTO.getIsbn());
        book.setGenre(bookDTO.getGenre());
        book.setAvailableCopies(bookDTO.getAvailableCopies());
        return book;
    }

    public BookDTO bookToBookDTO(Book book){
        if(book == null){
            return null;
        }
        BookDTO bookDTO = new BookDTO();
        bookDTO.setBookId(book.getBookId());
        bookDTO.setTitle(book.getTitle());
        bookDTO.setAuthor(book.getAuthor());
        bookDTO.setIsbn(book.getIsbn());
        bookDTO.setGenre(book.getGenre());
        bookDTO.setAvailableCopies(book.getAvailableCopies());
        return bookDTO;
    }
}
