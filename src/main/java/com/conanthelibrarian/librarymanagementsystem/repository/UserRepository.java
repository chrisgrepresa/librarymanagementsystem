package com.conanthelibrarian.librarymanagementsystem.repository;

import com.conanthelibrarian.librarymanagementsystem.dao.User;
import com.conanthelibrarian.librarymanagementsystem.dto.BookDTO;
import com.conanthelibrarian.librarymanagementsystem.dto.UserDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    /*todo Query personalizada:
    //- Recupera todos los usuarios que tienen prestados más de X libros.

    @Query("""
            SELECT new com.conanthelibrarian.librarymanagementsystem.dto.UserDTO
            (u.userId,
            u.name,
            u.email,
            u.password,
            u.role)
            FROM User u
            JOIN Loan l ON u.userId = l.userId
            WHERE u.userId = l.userId
            """)
    List<UserDTO> findUserInLoan();*/
}
