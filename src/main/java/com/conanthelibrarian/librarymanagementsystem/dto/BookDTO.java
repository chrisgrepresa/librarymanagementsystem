package com.conanthelibrarian.librarymanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BookDTO {
    private Integer bookId;
    private String title;
    private String author;
    private Long isbn;
    private String genre;
    private Integer availableCopies;

}
