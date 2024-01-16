package com.example.librarymanagementsystem.services;

import com.example.librarymanagementsystem.Dto.BookUpdateDto;
import com.example.librarymanagementsystem.models.Book;
import com.example.librarymanagementsystem.models.Patron;
import com.example.librarymanagementsystem.repositories.BookRepository;
import com.example.librarymanagementsystem.repositories.PatronRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private PatronRepository patronRepository;

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Book findById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with ID " + id + " not found"));
    }

    public Book save(Book book) {
        return bookRepository.save(book);
    }

    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    public Book updateBook(Long id, BookUpdateDto bookUpdateDto) {
        try {
            Book book = bookRepository.findById(id).orElseThrow();

            // Update only if the new title is provided
            if (bookUpdateDto.getTitle() != null) {
                book.setTitle(bookUpdateDto.getTitle());
            }

            // Update only if the new author is provided
            if (bookUpdateDto.getAuthor() != null) {
                book.setAuthor(bookUpdateDto.getAuthor());
            }

            return bookRepository.save(book);
        } catch (NoSuchElementException e) {
            // Handle the case where the book with the given id is not found
            throw new NoSuchElementException("Book not found with id: " + id);
        }
    }
}