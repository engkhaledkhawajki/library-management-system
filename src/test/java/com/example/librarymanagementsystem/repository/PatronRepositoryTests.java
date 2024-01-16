package com.example.librarymanagementsystem.repository;

import com.example.librarymanagementsystem.models.Patron;
import com.example.librarymanagementsystem.models.Role;
import com.example.librarymanagementsystem.repositories.PatronRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

//-> Sometimes other tests in the code modifies the database and leave additional records.
//-> The test environment might not be isolated, and changes made by other tests could affect the outcome some test.

//DataJpaTest:
//Purpose: This annotation is used to test JPA components in isolation.
//Functionality: It configures a slice of the application context that
// contains only the beans needed for JPA testing,
// such as the EntityManager, repositories, and the DataSource.
@DataJpaTest
@TestPropertySource(locations="../../../../test.properties")
@ContextConfiguration()
public class PatronRepositoryTests {

    @Autowired
    private PatronRepository patronRepository;

    @Test
    public void PatronRepository_GetAll_ReturnMoreThenOnePatron() {
        Patron patron = Patron.builder()
                .firstname("Anas")
                .lastname("Khawajki")
                .email("anas.khawajki@gmail.com")
                .password("12345")
                .role(Role.USER)
                .build();
        Patron patron2 = Patron.builder()
                .firstname("Ahmad")
                .lastname("Berkdar")
                .email("ahmad.berkdar@gmail.com")
                .password("54321")
                .role(Role.USER)
                .build();

        patronRepository.save(patron);
        patronRepository.save(patron2);

        List<Patron> patronList = patronRepository.findAll();

        Assertions.assertThat(patronList).isNotNull();
        Assertions.assertThat(patronList.size()).isEqualTo(2);
    }

    @Test
    public void PatronRepository_SaveAll_ReturnSavedPatron() {

        //Arrange
        Patron patron = Patron.builder()
                .firstname("Khaled")
                .lastname("Khawajki")
                .email("khaled.khawajki@gmail.com")
                .password("12345")
                .role(Role.USER)
                .build();

        //Act
        Patron savedPatron = patronRepository.save(patron);

        //Assert
        Assertions.assertThat(savedPatron).isNotNull();
        Assertions.assertThat(savedPatron.getId()).isGreaterThan(0);
    }

    @Test
    public void PatronRepository_FindById_ReturnPatron() {
        Patron patron = Patron.builder()
                .firstname("Khaled")
                .lastname("Khawajki")
                .email("khaled.khawajki@gmail.com")
                .password("12345")
                .role(Role.USER)
                .build();

        patronRepository.save(patron);

        Patron patronList = patronRepository.findById(patron.getId()).get();

        Assertions.assertThat(patronList).isNotNull();
    }

    @Test
    public void PatronRepository_FindByEmail_ReturnPatronNotNull() {
        Patron patron = Patron.builder()
                .firstname("Khaled")
                .lastname("Khawajki")
                .email("khaled.khawajki@gmail.com")
                .password("12345")
                .role(Role.USER)
                .build();

        patronRepository.save(patron);

        Patron patronList = patronRepository.findByEmail(patron.getEmail()).get();

        Assertions.assertThat(patronList).isNotNull();
    }

    @Test
    public void PatronRepository_UpdatePatron_ReturnPatronNotNull() {
        Patron patron = Patron.builder()
                .firstname("Khaled")
                .lastname("Khawajki")
                .email("khaled.khawajki@gmail.com")
                .password("12345")
                .role(Role.USER)
                .build();

        patronRepository.save(patron);

        Patron patronSave = patronRepository.findById(patron.getId()).get();
        patronSave.setEmail("khaled.khawajki@gmail.com");
        patronSave.setFirstname("Khaled");

        Patron updatedPatron = patronRepository.save(patronSave);

        Assertions.assertThat(updatedPatron.getEmail()).isNotNull();
        Assertions.assertThat(updatedPatron.getFirstname()).isNotNull();
    }

    @Test
    public void PatronRepository_PatronDelete_ReturnPatronIsEmpty() {
        Patron patron = Patron.builder()
                .firstname("Khaled")
                .lastname("Khawajki")
                .email("khaled.khawajki@gmail.com")
                .password("12345")
                .role(Role.USER)
                .build();

        patronRepository.save(patron);

        patronRepository.deleteById(patron.getId());
        Optional<Patron> patronReturn = patronRepository.findById(patron.getId());

        Assertions.assertThat(patronReturn).isEmpty();
    }


}