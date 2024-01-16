package com.example.librarymanagementsystem.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BorrowingRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book borrowedBook;

    @ManyToOne
    @JoinColumn(name = "patron_id")
    private Patron borrowedBy;

    @NotNull(message = "Borrowing date cannot be null")
    private LocalDateTime borrowingDate;

    private LocalDateTime returnDate;

    public BorrowingRecord(long bookId, long patronId, LocalDate now) {
    }
}