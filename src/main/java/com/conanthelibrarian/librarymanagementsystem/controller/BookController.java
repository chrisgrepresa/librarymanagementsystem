package com.conanthelibrarian.librarymanagementsystem.controller;

import com.conanthelibrarian.librarymanagementsystem.dto.BookDTO;
import com.conanthelibrarian.librarymanagementsystem.dto.UserDTO;
import com.conanthelibrarian.librarymanagementsystem.service.BookService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/book")
@Log4j2
public class BookController {

    private final BookService bookService;

    //todo LOGs y ResponeEntity

    @GetMapping("/all")
    public List<BookDTO> findAllBook(){
        return bookService.findBook();
    }

    @PostMapping("/new")
    public void newBook(@RequestBody BookDTO bookDTO){
        bookService.newBook(bookDTO);
    }

    @GetMapping("/find/{id}")
    public List<BookDTO> findBookById(@PathVariable String id){
        return bookService.findBookById(Integer.parseInt(id));
    }

    @PutMapping("/modify/{id}")
    public BookDTO modifyBook (@PathVariable String id, @RequestBody BookDTO bookDTO)
    {
        bookService.modifyBook(Integer.parseInt(id), bookDTO);
        return bookDTO;
    }


}
