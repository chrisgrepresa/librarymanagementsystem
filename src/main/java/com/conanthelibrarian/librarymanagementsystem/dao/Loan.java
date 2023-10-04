package com.conanthelibrarian.librarymanagementsystem.dao;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "loans")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer loanId;
    private Integer userId;
    private Integer bookId;
    private LocalDate loanDate;
    private LocalDate dueDate;
}
