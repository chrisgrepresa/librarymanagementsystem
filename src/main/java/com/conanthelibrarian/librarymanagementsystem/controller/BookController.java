package com.conanthelibrarian.librarymanagementsystem.controller;

import com.conanthelibrarian.librarymanagementsystem.dto.BookDTO;
import com.conanthelibrarian.librarymanagementsystem.repository.LoanRepository;
import com.conanthelibrarian.librarymanagementsystem.service.BookService;
import com.conanthelibrarian.librarymanagementsystem.service.LoanService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/book")
@Log4j2
public class BookController {

    private final BookService bookService;


    @GetMapping("/all")
    public ResponseEntity<List<BookDTO>> findAllBook() {
        if (bookService.findBook().isEmpty()) {
            return new ResponseEntity<>(bookService.findBook(), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(bookService.findBook(), HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<Optional<BookDTO>> findBookById(@PathVariable String id) {
        if (bookService.findBookById(Integer.parseInt(id)).isEmpty()) {
            log.info("Book not found with ID:{}", id);
            return new ResponseEntity<>(bookService.findBookById(Integer.parseInt(id)), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(bookService.findBookById(Integer.parseInt(id)), HttpStatus.OK);
    }

    @PostMapping("/new")
    public ResponseEntity<String> newBook(@RequestBody BookDTO bookDTO) {
        try {
            bookService.newBook(bookDTO);
            log.info("New book saved");
            return ResponseEntity.status(200).body("New book saved");
        } catch (Exception e) {
            log.info("Error when saving book: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error when saving book:" + e.getMessage());
        }
    }

    @PutMapping("/modify/{id}")
    public ResponseEntity<String> modifyBook(@PathVariable String id, @RequestBody BookDTO bookDTO) {
        try {
            bookService.modifyBook(Integer.parseInt(id), bookDTO);
            log.info("New book modified");
            return ResponseEntity.status(200).body("New book modified");
        } catch (Exception e) {
            log.info("Error when saving book: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error when saving book:" + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable String id) {
        try {
            bookService.deleteBookById(Integer.parseInt(id));
            log.info("Book deleted with Id: {}", id);
            return ResponseEntity.status(200).body("Book deleted");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error when deleting book: " +
                    e.getMessage());
        }
    }

    @GetMapping("/genre/{genre}")
    public ResponseEntity<List<BookDTO>> findBookByGenre (@PathVariable String genre){
        if(bookService.findBookByGenre(genre).isEmpty()){
            log.info("Book not found with genre:{}", genre);
            return new ResponseEntity<>(bookService.findBookByGenre(genre), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(bookService.findBookByGenre(genre), HttpStatus.OK);
    }

    @GetMapping("/loan")
    public ResponseEntity<List<BookDTO>> findBookInLoan(){
        if(bookService.findBookInLoan().isEmpty()){
            log.info("No books found in Loan Database");
            return new ResponseEntity<>(bookService.findBookInLoan(), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(bookService.findBookInLoan(), HttpStatus.OK);
    }


    @GetMapping("/available/{id}")
    public ResponseEntity<Optional<BookDTO>> findAvailableBook(@PathVariable String id) {
        if (bookService.findAvailableBook(Integer.parseInt(id)).isEmpty()) {
            log.info("Book not found with ID:{}", id);
            return new ResponseEntity<>(bookService.findAvailableBook(Integer.parseInt(id)), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(bookService.findAvailableBook(Integer.parseInt(id)), HttpStatus.OK);
    }


    @GetMapping("/check/{bookId}")
    public ResponseEntity<String> checkBookAvailability(@PathVariable Integer bookId, Integer userId) {
        try {
            if(bookService.checkBookAvailability(bookId, userId).equalsIgnoreCase("available")){
                log.info("Book available with id: {}", bookId);
                return ResponseEntity.status(200).body("The book is available");
            }
            else{
                log.info("Book not available with id: {}", bookId);
                return ResponseEntity.status(200).body("The book is not available");
            }
        } catch (Exception e) {
            log.info("Error: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error:" + e.getMessage());
        }
    }

    /*@PutMapping("/available/reduce/{id}")
    public ResponseEntity<String> reduceAvailableStock(@PathVariable String id, @RequestBody BookDTO bookDTO) {
        try {
            bookService.modifyStock(Integer.parseInt(id), bookDTO);
            log.info("New book modified");
            return ResponseEntity.status(200).body("New book modified");
        } catch (Exception e) {
            log.info("Error when saving book: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error when saving book:" + e.getMessage());
        }
    }*/
}
