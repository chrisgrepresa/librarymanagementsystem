package com.conanthelibrarian.librarymanagementsystem.service;

import com.conanthelibrarian.librarymanagementsystem.dao.Book;
import com.conanthelibrarian.librarymanagementsystem.dao.User;
import com.conanthelibrarian.librarymanagementsystem.dto.BookDTO;
import com.conanthelibrarian.librarymanagementsystem.dto.UserDTO;
import com.conanthelibrarian.librarymanagementsystem.mapper.BookMapper;
import com.conanthelibrarian.librarymanagementsystem.repository.BookRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Log4j2
public class BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;


    public List<BookDTO> findBook(){
        return bookRepository.findAll().stream()
                .map(bookMapper::bookToBookDTO)
                .collect(Collectors.toList());
    }

    public Optional<BookDTO> findBookById(Integer id){
        return bookRepository.findById(id).stream()
                .map(bookMapper::bookToBookDTO)
                .findAny();
    }

    public void newBook (BookDTO bookDTO){
        Book book = bookMapper.bookDTOToBook(bookDTO);
        bookRepository.save(book);
        log.info("Book saved with title: {}", bookDTO.getTitle());
    }

    public BookDTO modifyBook(Integer id, BookDTO bookDTO){
        Optional<Book> bookOptional = bookRepository.findById(id);
        if(bookOptional.isPresent()){
            Book book = bookMapper.bookDTOToBook(bookDTO);
            bookRepository.save(book);
            log.info("Book modified with title: {}", bookDTO.getTitle());
        }
        return bookDTO;
    }

    public void deleteBookById(Integer id) {
        if(id != null){
            BookDTO bookDTO = BookDTO.builder().build();
            bookMapper.bookDTOToBook(bookDTO);
            bookRepository.deleteById(id);
            log.info("Book deleted with id: {}", bookDTO.getBookId());
        }
    }
}
