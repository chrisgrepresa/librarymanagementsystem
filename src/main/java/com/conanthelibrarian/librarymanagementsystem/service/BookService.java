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

    public Optional<BookDTO> findBookById(Integer id) {
        return bookRepository.findById(id).stream()
                .map(bookMapper::bookToBookDTO)
                .findAny();
    }

    public void createNewBook(BookDTO bookDTO) {
        Book book = bookMapper.bookDTOToBook(bookDTO);
        bookRepository.save(book);
        log.info("Book saved with title: {}", bookDTO.getTitle());
    }

    public BookDTO modifyBook(Integer id, BookDTO bookDTO) {
        Optional<Book> bookOptional = bookRepository.findById(id);
        if (bookOptional.isPresent()) {
            Book book = bookMapper.bookDTOToBook(bookDTO);
            bookRepository.save(book);
            log.info("Book modified with title: {}", bookDTO.getTitle());
        }
        return bookDTO;
    }

    public void deleteBookById(Integer id) {
        if (id != null) {
            bookRepository.deleteById(id);
            log.info("Book deleted with id: {}", id);
        }
    }

    public List<BookDTO> findBookByGenre(String genre) {
        return bookRepository.findBookByGenre(genre);
    }

    public List<BookDTO> findBookInLoan() {
        return bookRepository.findBooksInLoan();
    }

    public void openNewLoanAndReduceStockIfAvailable(Integer bookId, Integer userId, LoanDTO loanDTO) {
        try {
            if (isBookAvailable(bookId)) {
                System.out.println("Available");
                createNewLoanIfAvailable(bookId, userId, loanDTO);
                reduceBookQuantity(bookId);
            } else {
                System.out.println("Not available");
            }
        } catch (Exception e) {
            System.out.println("Error" + e.getMessage());
        }
    }

    public void deleteLoanAndIncreaseStockIfAvailable(Integer bookId, Integer userId) {
        try {
            if (isBookAvailable(bookId)) {
                System.out.println("Available");
                findLoanId(bookId, userId);
                loanService.deleteLoanById(findLoanId(bookId, userId));
                increaseBookQuantity(bookId);
            } else {
                System.out.println("Not available");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    public List<BookDTO> filterBook(String parameter) {
        return bookRepository.findAll().stream()
                .filter(line -> parameter.equalsIgnoreCase(line.getAuthor()) ||
                        parameter.equalsIgnoreCase(line.getTitle()) ||
                        parameter.equalsIgnoreCase(line.getGenre()))
                .map(bookMapper::bookToBookDTO)
                .collect(Collectors.toList());

    }

    public boolean isBookAvailable(Integer bookId) {
        bookRepository.findById(bookId).stream()
                .filter(book -> book.getAvailableCopies() >= 1)
                .map(bookMapper::bookToBookDTO)
                .findAny();
        return true;
    }

    private void createNewLoanIfAvailable(Integer bookId, Integer userId, LoanDTO loanDTO) {
        loanDTO.setUserId(userId);
        loanDTO.setBookId(bookId);
        loanService.createNewLoan(loanDTO);
    }

    public void reduceBookQuantity(Integer bookId) {
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        if (bookOptional.isPresent()) {
            bookOptional.get().setAvailableCopies(bookOptional.get().getAvailableCopies() - 1);
            bookRepository.save(bookOptional.get());
            log.info("Book modified with title: {}", bookOptional.get().getTitle());
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

    public void increaseBookQuantity(Integer bookId) {
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        if (bookOptional.isPresent()) {
            bookOptional.get().setAvailableCopies(bookOptional.get().getAvailableCopies() + 1);
            bookRepository.save(bookOptional.get());
            log.info("Book modified with title: {}", bookOptional.get().getTitle());
        }
    }

}