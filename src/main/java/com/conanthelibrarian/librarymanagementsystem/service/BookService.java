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


    public List<BookDTO> findAllBook() {
        return bookRepository.findAll().stream()
                .map(bookMapper::bookToBookDTO)
                .collect(Collectors.toList());
    }

    public Optional<BookDTO> findBookById(Integer bookId) {
        return bookRepository.findById(bookId).stream()
                .map(bookMapper::bookToBookDTO)
                .findAny();
    }

    public void createNewBook(BookDTO bookDTO) {
        Book book = bookMapper.bookDTOToBook(bookDTO);
        bookRepository.save(book);
        log.info("Book saved with title: {}", bookDTO.getTitle());
    }

    public void modifyBook(BookDTO bookDTO) {
        Optional<Book> bookOptional = bookRepository.findById(bookDTO.getBookId());
        if (bookOptional.isPresent()) {
            Book book = bookMapper.bookDTOToBook(bookDTO);
            bookRepository.save(book);
            log.info("Book modified with title: {}", bookDTO.getTitle());
        }
    }

    public void deleteBookById(Integer bookId) {
        if (bookId != null) {
            bookRepository.deleteById(bookId);
            log.info("Book deleted with bookId: {}", bookId);
        }
    }

    public List<BookDTO> findBookByGenre(String genre) {
        return bookRepository.findBookByGenre(genre);
    }

    public List<BookDTO> findBookInLoan() {
        return bookRepository.findBooksInLoan();
    }

    public void openNewLoanAndReduceStockIfAvailable(Integer bookId, Integer userId, LoanDTO loanDTO) {
        if (isBookAvailable(bookId)) {
            log.info("Available");
            createNewLoanIfAvailable(bookId, userId, loanDTO);
            reduceAvailableStock(bookId);
        } else {
            log.info("Not available");
        }
    }

    public void deleteLoanAndIncreaseStockIfAvailable(Integer bookId, Integer userId) {
        if (isBookAvailable(bookId)) {
            log.info("Available");
            findLoanId(bookId, userId);
            loanService.deleteLoanById(findLoanId(bookId, userId));
            increaseAvailableStock(bookId);
        } else {
            log.info("Not available");
        }
    }


    public List<BookDTO> filterBook(String parameter) {
        return bookRepository.findAll().stream()
                .filter(line -> parameter.equalsIgnoreCase(line.getAuthor()) ||
                        parameter.equalsIgnoreCase(line.getTitle()) ||
                        parameter.equalsIgnoreCase(line.getGenre()))
                .filter(line -> line.getAuthor().contains(parameter))
                .map(bookMapper::bookToBookDTO)
                .collect(Collectors.toList());
    }

    public boolean isBookAvailable(Integer bookId) {
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        if (bookOptional.isPresent()) {
            if (bookOptional.get().getAvailableCopies() >= 1) {
                log.info("Book available");
                return true;
            }
            else{
                log.info("Book not available");
                return false;
            }
        }
        else{
            log.info("Book not found");
            return false;
        }
    }

    public void createNewLoanIfAvailable(Integer bookId, Integer userId, LoanDTO loanDTO) {
        loanDTO.setUserId(userId);
        loanDTO.setBookId(bookId);
        loanService.createNewLoan(loanDTO);
    }

    public void reduceAvailableStock(Integer bookId) {
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        if (bookOptional.isPresent()) {
            if (bookOptional.get().getAvailableCopies() >= 1) {
                bookOptional.get().setAvailableCopies(bookOptional.get().getAvailableCopies() - 1);
                bookRepository.save(bookOptional.get());
                log.info("Book modified with title: {}", bookOptional.get().getTitle());
            }
            else{
                log.info("Not available copies");
            }
        }
    }

    public Integer findLoanId(Integer bookId, Integer userId) {
        for (LoanDTO loanDTOs : loanService.findAllLoan()) {
            if (loanDTOs.getBookId().equals(bookId) && loanDTOs.getUserId().equals(userId)) {
                Integer loanId = loanDTOs.getLoanId();
                return loanId;
            }
        }
        return null;
    }

    public void increaseAvailableStock(Integer bookId) {
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        if (bookOptional.isPresent()) {
            if (bookOptional.get().getAvailableCopies() >= 1) {
                bookOptional.get().setAvailableCopies(bookOptional.get().getAvailableCopies() + 1);
                bookRepository.save(bookOptional.get());
                log.info("Book modified with title: {}", bookOptional.get().getTitle());
            }
            else{
                log.info("Not available copies");
            }
        }
    }

}