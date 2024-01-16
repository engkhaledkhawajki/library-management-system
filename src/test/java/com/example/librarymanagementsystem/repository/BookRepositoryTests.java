package com.example.librarymanagementsystem.repository;

import com.example.librarymanagementsystem.models.Book;
import com.example.librarymanagementsystem.repositories.BookRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@TestPropertySource(locations="../../../../test.properties")
@ContextConfiguration()
public class BookRepositoryTests {
    private final BookRepository bookRepository;

    @Autowired
    public BookRepositoryTests(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Test
    public void bookRepository_SaveAll_ReturnsSavedBook() {
        Book book = Book.builder().title("title").author("author").build();

        Book savedBook = bookRepository.save(book);

        Assertions.assertThat(savedBook).isNotNull();
        Assertions.assertThat(savedBook.getId()).isGreaterThan(0);
    }

    @Test
    public void BookRepository_GetAll_ReturnsMoreThenOneBook() {
        Book book = Book.builder().title("title").author("author").build();
        Book book2 = Book.builder().title("title").author("author").build();

        bookRepository.save(book);
        bookRepository.save(book2);

        List<Book> bookList = bookRepository.findAll();

        Assertions.assertThat(bookList).isNotNull();
        Assertions.assertThat(bookList.size()).isEqualTo(2);
    }

    @Test
    public void bookRepository_FindById_ReturnsSavedBook() {
        Book book = Book.builder().title("title").author("author").build();

        bookRepository.save(book);

        Book bookReturn = bookRepository.findById(book.getId()).get();

        Assertions.assertThat(bookReturn).isNotNull();
    }

    @Test
    public void bookRepository_UpdateBook_ReturnBook() {
        Book book = Book.builder().title("title").author("author").build();

        bookRepository.save(book);

        Book bookSave = bookRepository.findById(book.getId()).get();
        bookSave.setTitle("title");
        bookSave.setAuthor("author");
        Book udpatedPokemon = bookRepository.save(bookSave);

        Assertions.assertThat(udpatedPokemon.getTitle()).isNotNull();
        Assertions.assertThat(udpatedPokemon.getAuthor()).isNotNull();
    }

    @Test
    public void bookRepository_BookDelete_ReturnBookIsEmpty() {
        Book book = Book.builder().title("title").author("author").build();

        bookRepository.save(book);

        bookRepository.deleteById(book.getId());
        Optional<Book> bookReturn = bookRepository.findById(book.getId());

        Assertions.assertThat(bookReturn).isEmpty();
    }

}