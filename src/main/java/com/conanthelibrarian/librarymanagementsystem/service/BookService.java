package com.conanthelibrarian.librarymanagementsystem.service;

import com.conanthelibrarian.librarymanagementsystem.dao.Book;
import com.conanthelibrarian.librarymanagementsystem.dto.BookDTO;
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

    /*
    - Una operación que utilice Streams para filtrar libros en base a algún criterio (como género o autor).
    - Una operación para prestar un libro: recibe la ID del libro y la ID de la persona que recibe el libro.
    Deberás hacer los pasos necesarios para que la BBDD refleje correctamente el préstamo
    y se reduzca en 1 el stock del libro.
    También es necesario hacer una comprobación previa para prestar el libro de que efectivamente
    hay al menos 1 en stock.
    (Dejo a tu elección cómo hacer todo esto, te recomiendo que separes las funcionalidades en
                                    métodos distintos y luego uses esos métodos donde los necesites)
		      - Una operación para devolver un libro: Básicamente hace lo contrario de lo anterior.

    */

    //1. Comprobar si hay copias de ESE libro


    /*public Optional<BookDTO> findAvailableBook(Integer bookId){
        return bookRepository.findById(bookId).stream()
                .filter(book -> book.getAvailableCopies() >= 1)
                .map(book -> book.setAvailableCopies(book.getAvailableCopies()))
                .map(bookMapper::bookToBookDTO)
                .findAny();
    }*/


    //2. Reflejar el nuevo préstamo en la BBDD.
    //3. Reducir el stock en 1



}
