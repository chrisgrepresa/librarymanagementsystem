package com.conanthelibrarian.librarymanagementsystem.repository;

import com.conanthelibrarian.librarymanagementsystem.dao.Book;
import com.conanthelibrarian.librarymanagementsystem.dao.Loan;
import com.conanthelibrarian.librarymanagementsystem.dto.BookDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {

    @Query("""
            SELECT new com.conanthelibrarian.librarymanagementsystem.dto.BookDTO
            (b.bookId,
            b.title,
            b.author,
            b.isbn,
            b.genre,
            b.availableCopies)
            FROM Book b
            WHERE b.genre = :genre
            """)
    List<BookDTO> findBookByGenre(@Param("genre") String genre);

    @Query("""
            SELECT new com.conanthelibrarian.librarymanagementsystem.dto.BookDTO
            (b.bookId,
            b.title,
            b.author,
            b.isbn,
            b.genre,
            b.availableCopies)
            FROM Book b
            JOIN Loan l ON b.bookId = l.bookId
            WHERE b.bookId = l.bookId
            """)
    List<BookDTO> findBooksInLoan();


}
