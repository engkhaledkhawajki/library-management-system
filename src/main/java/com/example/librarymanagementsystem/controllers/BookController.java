package com.example.librarymanagementsystem.controllers;

import com.example.librarymanagementsystem.Dto.BookUpdateDto;
import com.example.librarymanagementsystem.logging.LogExecution;
import com.example.librarymanagementsystem.models.Book;
import com.example.librarymanagementsystem.models.Patron;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.example.librarymanagementsystem.services.BookService;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/books")
public class BookController {

    Logger logger = LoggerFactory.getLogger(BookController.class);

    @Autowired
    private BookService bookService;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @LogExecution
    public List<Book> getAllBooks() {
        return bookService.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @LogExecution
    public ResponseEntity<Object> getBook(@PathVariable Long id) {
        try {
            Book book = bookService.findById(id);
            return new ResponseEntity<Object>(book, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<Object>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @LogExecution
    public Book addBook(@RequestBody Book book) {
        return bookService.save(book);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @LogExecution
    public void deleteBook(@PathVariable Long id) {
        bookService.deleteById(id);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @LogExecution
    public ResponseEntity<Book> updateBook(
            @PathVariable Long id,
            @RequestBody BookUpdateDto request) {

        try {
            Book updatedBook = bookService.updateBook(id, request);
            String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
            StringBuilder logMessage = new StringBuilder("User with username: ").append(currentUsername);
            logMessage.append(" is updating the book with ID: ").append(id);

            if (request.getTitle() != null) {
                logMessage.append(". New title: ").append(request.getTitle());
            }

            if (request.getAuthor() != null) {
                logMessage.append(". New author: ").append(request.getAuthor());
            }

            logger.info(logMessage.toString());
            return ResponseEntity.ok(updatedBook);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            // Handle other exceptions (e.g., database errors)
            return ResponseEntity.status(500).build();
        }
    }

}