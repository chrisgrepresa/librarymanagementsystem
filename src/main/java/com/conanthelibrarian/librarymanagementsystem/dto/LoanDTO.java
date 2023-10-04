package com.conanthelibrarian.librarymanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LoanDTO {
    private Integer loanId;
    private Integer userId;
    private Integer bookId;
    private LocalDate loanDate;
    private LocalDate dueDate;
}
