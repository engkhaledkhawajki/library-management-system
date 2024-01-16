package com.example.librarymanagementsystem.controllers;

import com.example.librarymanagementsystem.logging.LogExecution;
import com.example.librarymanagementsystem.models.BorrowingRecord;
import com.example.librarymanagementsystem.services.BookBorrowingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/return")
public class ReturningController {
    Logger logger = LoggerFactory.getLogger(ReturningController.class);

    private BookBorrowingService borrowingService;

    @Autowired
    public ReturningController(BookBorrowingService borrowingService) {
        this.borrowingService = borrowingService;
    }

    @PatchMapping("/{bookId}/patron/{patronId}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @LogExecution
    public ResponseEntity<Object> returnBook(@PathVariable Long bookId, @PathVariable Long patronId) {
        try {
            BorrowingRecord borrowingRecord = borrowingService.returnBook(bookId, patronId);
            ResponseEntity<Object> responseEntity = ResponseEntity.status(HttpStatus.OK).body(borrowingRecord);
            logger.info("Patron with ID: {} is sending a request to return a book with ID: {}", patronId, bookId);
            return responseEntity;
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }
}