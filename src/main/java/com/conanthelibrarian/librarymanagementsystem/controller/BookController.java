package com.conanthelibrarian.librarymanagementsystem.controller;

import com.conanthelibrarian.librarymanagementsystem.dto.BookDTO;
import com.conanthelibrarian.librarymanagementsystem.dto.LoanDTO;
import com.conanthelibrarian.librarymanagementsystem.service.BookService;
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
    public ResponseEntity<List<BookDTO>> findAllBook(){
        if(bookService.findAllBook().isEmpty()){
            return new ResponseEntity<>(bookService.findAllBook(), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(bookService.findAllBook(), HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<Optional<BookDTO>> findBookById(@PathVariable String id){
        if(bookService.findBookById(Integer.parseInt(id)).isEmpty()){
            log.info("Book not found with ID:{}", id);
            return new ResponseEntity<>(bookService.findBookById(Integer.parseInt(id)), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(bookService.findBookById(Integer.parseInt(id)), HttpStatus.OK);
    }

    @PostMapping("/new")
    public ResponseEntity<String> createNewBook(@RequestBody BookDTO bookDTO){
        try{
            bookService.createNewBook(bookDTO);
            log.info("New book saved");
            return ResponseEntity.status(200).body("New book saved");
        }catch(Exception e){
            log.info("Error when saving book: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error when saving book:" + e.getMessage());
        }
    }

    @PutMapping("/modify/{id}")
    public ResponseEntity<String> modifyBook(@PathVariable String id, @RequestBody BookDTO bookDTO){
        try{
            if(bookService.findBookById(Integer.parseInt(id)).isEmpty()){
                log.info("Book not found with ID:{}", id);
                return new ResponseEntity<>("Book not found", HttpStatus.NOT_FOUND);
            }
            else{
                bookService.modifyBook(Integer.parseInt(id), bookDTO);
                log.info("Book modified");
                return ResponseEntity.status(200).body("Book modified");
            }
        }catch(Exception e){
            log.info("Error when saving book: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error when saving book:" + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable String id){
        try{
            bookService.deleteBookById(Integer.parseInt(id));
            log.info("Book deleted with Id: {}", id);
            return ResponseEntity.status(200).body("Book deleted");
        }catch(Exception e){
            return ResponseEntity.status(500).body("Error when deleting book: " +
                    e.getMessage());
        }
    }

    @GetMapping("/genre/{genre}")
    public ResponseEntity<List<BookDTO>> findBookByGenre(@PathVariable String genre){
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

    /*@GetMapping("/available/{id}")
    public ResponseEntity<Optional<BookDTO>> findAvailableBook(@PathVariable String id){
        if(!bookService.isBookAvailable(Integer.parseInt(id))){
            log.info("Book not found with ID:{}", id);
            return new ResponseEntity<>(bookService.isBookAvailable(Integer.parseInt(id)), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(bookService.isBookAvailable(Integer.parseInt(id)), HttpStatus.OK);
    }*/

    @GetMapping("/filter/{parameter}")
    public ResponseEntity<List<BookDTO>> filterBook (@PathVariable String parameter){
        if(bookService.filterBook(parameter).isEmpty()){
            log.info("Books not found for parameter: {}", parameter);
            return new ResponseEntity<>(bookService.filterBook(parameter), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(bookService.filterBook(parameter), HttpStatus.OK);
    }

    @PostMapping("/new/book/{bookId}/user/{userId}")
    public ResponseEntity<String> openNewLoanIfAvailable(@PathVariable Integer bookId, @PathVariable Integer userId,
                                                         @RequestBody LoanDTO loanDTO, BookDTO bookDTO){
        bookService.openNewLoanAndReduceStockIfAvailable(bookId, userId, loanDTO, bookDTO);
        try{
            if(bookService.isBookAvailable(bookId)){
                log.info("Book available with id: {}", bookId);
                log.info("New loan saved");
                return ResponseEntity.status(200).body("The book is available, new loan saved");
            }
            else{
                log.info("Book not available with id: {}", bookId);
                return ResponseEntity.status(200).body("The book is not available");
            }
        }catch(Exception e){
            log.info("Error when saving loan: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error when saving loan:" + e.getMessage());
        }
    }

    @DeleteMapping("/return/book/{bookId}/user/{userId}")
    public ResponseEntity<String> deleteLoanIfAvailable(@PathVariable String bookId, @PathVariable String userId){
        try {
            if(bookService.isBookAvailable(Integer.parseInt(bookId))){
                bookService.deleteLoanAndIncreaseStockIfAvailable(Integer.parseInt(bookId), Integer.parseInt(userId));
                log.info("Book available with id: {}", bookId);
                log.info("Loan deleted with Id: {}",
                        bookService.findLoanId(Integer.parseInt(bookId), Integer.parseInt(userId)));
                return ResponseEntity.status(200).body("Loan deleted");
            }
            else{
                log.info("Book not available with id: {}", bookId);
                return ResponseEntity.status(200).body("The book is not available");
            }
        }catch(Exception e){
            log.info("Error when saving loan: {}" + e.getMessage());
            return ResponseEntity.status(500).body("Error when deleting loan: " +
                    e.getMessage());
        }
    }

    @PatchMapping("/available/reduce/{id}")
    public ResponseEntity<String> reduceAvailableStock(@PathVariable String id, @RequestBody BookDTO bookDTO){
        try{
            if(bookService.findBookById(Integer.parseInt(id)).isEmpty()){
                log.info("Book not found with ID:{}", id);
                return new ResponseEntity<>("Book not found", HttpStatus.NOT_FOUND);
            }
            else{
                bookService.reduceAvailableStock(Integer.parseInt(id), bookDTO);
                log.info("Book modified");
                return ResponseEntity.status(200).body("Book modified");
            }
        }catch(Exception e){
            log.info("Error when saving book: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error when saving book:" + e.getMessage());
        }
    }
}


