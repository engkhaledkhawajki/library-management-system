package com.example.librarymanagementsystem.repositories;

import com.example.librarymanagementsystem.models.Patron;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatronRepository extends JpaRepository<Patron, Long> {

    Optional<Patron> findByEmail(String email);
}