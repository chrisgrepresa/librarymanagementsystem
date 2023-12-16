package com.conanthelibrarian.librarymanagementsystem.service;

import com.conanthelibrarian.librarymanagementsystem.dao.Book;
import com.conanthelibrarian.librarymanagementsystem.dto.BookDTO;
import com.conanthelibrarian.librarymanagementsystem.dto.LoanDTO;
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
    private final LoanService loanService;


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
            bookRepository.deleteById(id);
            log.info("Book deleted with id: {}", id);
        }
    }

    public List<BookDTO> findBookByGenre(String genre){
        return bookRepository.findBookByGenre(genre);
    }

    public List<BookDTO> findBookInLoan(){
        return bookRepository.findBooksInLoan();
    }


    public void testAvailability(Integer bookId, Integer userId, LoanDTO loanDTO, BookDTO bookDTO){
        try{
            findAvailableBook(bookId);
            if(findAvailableBook(bookId).isEmpty()){
                System.out.println("Not available");
            } else{
                System.out.println("Available");
                //loanDTO.setUserId(userId);
                //loanDTO.setBookId(bookId);
                //loanService.newLoan(loanDTO);
                //bookDTO.setAvailableCopies(500);
                //modifyBook(bookId, bookDTO);
                //1.modificar bookDTO
                //2. guardar en book.Service.modifyBook(bookDTO)
                //reduceBookQuantity(bookId, bookDTO);
            }
        }catch(Exception e){
            System.out.println("Error");
        }
    }

    public Optional<BookDTO> findAvailableBook(Integer bookId){
        return bookRepository.findById(bookId).stream()
                .filter(book -> book.getAvailableCopies() >= 1)
                .map(bookMapper::bookToBookDTO)
                .findAny();
    }

    public BookDTO reduceBookQuantity(Integer id, BookDTO bookDTO){
        Optional<Book> bookOptional = bookRepository.findById(id);
        if(bookOptional.isPresent()){
            bookDTO.setAvailableCopies(500);
            Book book = bookMapper.bookDTOToBook(bookDTO);
            bookRepository.save(book);
            log.info("Book quantity reduced with Id: {}", bookDTO.getBookId());
        }
        return bookDTO;
    }
}
