package com.example.librarymanagementsystem.repositories;

import com.example.librarymanagementsystem.models.BorrowingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookBorrowingRepository extends JpaRepository<BorrowingRecord, Long> {

    @Query("SELECT br FROM BorrowingRecord br WHERE br.borrowedBy.id = :borrowedBy AND br.borrowedBook.id = :borrowedBook ORDER BY br.id DESC LIMIT 1")
    BorrowingRecord findLastBorrowingRecord(@Param("borrowedBy") Long borrowedBy, @Param("borrowedBook") Long borrowedBook);
}
