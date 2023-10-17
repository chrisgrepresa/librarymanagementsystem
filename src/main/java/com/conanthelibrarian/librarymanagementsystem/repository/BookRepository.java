package com.conanthelibrarian.librarymanagementsystem.repository;

import com.conanthelibrarian.librarymanagementsystem.dao.Book;
import com.conanthelibrarian.librarymanagementsystem.dto.BookDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {

    //todo Query personalizada:
    //- Recupera los libros de un género específico


    /*
    @Query("""
            SELECT new com.blockbuster.videoclub.dto.MovieCustomerDTO
            (m.movieId, m.director, m.genre, m.releaseYear, m.title, c.firstName, c.lastName, mr.dueReturnDate, mr.isReturned)
            FROM Movie m
            JOIN MovieRental mr ON m.movieId = mr.movie.movieId
            JOIN Customer c ON mr.customer.customerId = c.customerId
            WHERE m.movieId = :movieId
            """)
    List<MovieCustomerDTO> findCustomerByMovieId(Integer movieId);
     */
}
