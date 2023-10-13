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
import java.util.stream.Collectors;

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
    /*
    Error when deleting book: could not execute statement
    [Cannot delete or update a parent row: a foreign key constraint fails
    (`library`.`loans`, CONSTRAINT `loans_ibfk_2` FOREIGN KEY (`book_id`)
    REFERENCES `books` (`book_id`))] [delete from books where book_id=?];
    SQL [delete from books where book_id=?]; constraint [null]
     */

}
