package com.example.librarymanagementsystem.services;

import com.example.librarymanagementsystem.models.Book;
import com.example.librarymanagementsystem.models.BorrowingRecord;
import com.example.librarymanagementsystem.models.Patron;
import com.example.librarymanagementsystem.repositories.BookBorrowingRepository;
import com.example.librarymanagementsystem.repositories.BookRepository;
import com.example.librarymanagementsystem.repositories.PatronRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class BookBorrowingService {

    @Autowired
    private BookBorrowingRepository bookBorrowingRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private PatronRepository patronRepository;

    public List<BorrowingRecord> findAll() {
        return bookBorrowingRepository.findAll();
    }

    public BorrowingRecord findById(Long id) {
        return bookBorrowingRepository.findById(id).orElse(null);
    }

    public BorrowingRecord save(BorrowingRecord borrowingRecord) {
        return bookBorrowingRepository.save(borrowingRecord);
    }

    public void deleteById(Long id) {
        bookBorrowingRepository.deleteById(id);
    }

    @Transactional
    public BorrowingRecord borrowBook(Long bookId, Long patronId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new NoSuchElementException("Book not found with ID: " + bookId));
        Patron patron = patronRepository.findById(patronId)
                .orElseThrow(() -> new NoSuchElementException("Patron not found with ID: " + patronId));

        BorrowingRecord lastBorrowingRecord = bookBorrowingRepository.findLastBorrowingRecord(patronId, bookId);

        if (lastBorrowingRecord != null && lastBorrowingRecord.getReturnDate() == null) {
            throw new IllegalStateException("Book has not been returned yet.");
        }

        BorrowingRecord borrowingRecord = new BorrowingRecord();
        borrowingRecord.setBorrowedBy(patron);
        borrowingRecord.setBorrowedBook(book);
        borrowingRecord.setBorrowingDate(LocalDateTime.now());
        return bookBorrowingRepository.save(borrowingRecord);
    }
    @Transactional
    public BorrowingRecord returnBook(Long bookId, Long patronId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new NoSuchElementException("Book not found with ID: " + bookId));
        Patron patron = patronRepository.findById(patronId)
                .orElseThrow(() -> new NoSuchElementException("Patron not found with ID: " + patronId));

        BorrowingRecord lastBorrowingRecord = bookBorrowingRepository.findLastBorrowingRecord(patronId, bookId);

        if (lastBorrowingRecord != null && lastBorrowingRecord.getReturnDate() != null) {
            throw new IllegalStateException("Book has already been returned.");
        }

        assert lastBorrowingRecord != null;

        lastBorrowingRecord.setReturnDate(LocalDateTime.now());

        return bookBorrowingRepository.save(lastBorrowingRecord);
    }

}
