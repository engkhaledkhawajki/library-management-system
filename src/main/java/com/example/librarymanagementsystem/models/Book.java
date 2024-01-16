package com.example.librarymanagementsystem.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String author;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @Column(name = "publication_year")
    private Date publication_year;

    @Pattern(regexp = "(?=(?:\\D*\\d){10}(?:(?:\\D*\\d){3})?$)", message = "Invalid ISBN format")
    private String isbn;
}