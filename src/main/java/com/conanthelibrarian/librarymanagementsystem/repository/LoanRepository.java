package com.conanthelibrarian.librarymanagementsystem.repository;

import com.conanthelibrarian.librarymanagementsystem.dao.Loan;
import com.conanthelibrarian.librarymanagementsystem.dto.LoanDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Integer> {

    /*
    @Query("""
            SELECT userId
            FROM Loan l
            GROUP BY userId
            HAVING COUNT(*)>1;
            """)
    List<LoanDTO> findUserInLoan;*/

}
