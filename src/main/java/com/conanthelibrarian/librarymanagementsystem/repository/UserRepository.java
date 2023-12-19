package com.conanthelibrarian.librarymanagementsystem.repository;

import com.conanthelibrarian.librarymanagementsystem.dao.User;
import com.conanthelibrarian.librarymanagementsystem.dto.UserDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {


    @Query("""
            SELECT new com.conanthelibrarian.librarymanagementsystem.dto.UserDTO
            (u.userId,
            u.name,
            u.email,
            u.password,
            u.role)
            FROM User u
            JOIN Loan l ON u.userId = l.userId
            WHERE u.userId IN (
            SELECT l.userId FROM Loan l
            GROUP BY userId
            HAVING COUNT(*)>= :quantity)
            """)
    List<UserDTO> findUserInLoanForQuantity(@Param("quantity") Integer quantity);


}
