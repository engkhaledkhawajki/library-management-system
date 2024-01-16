package com.example.librarymanagementsystem.controller;

import com.example.librarymanagementsystem.models.BorrowingRecord;
import com.example.librarymanagementsystem.services.BookBorrowingService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.NoSuchElementException;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
public class ReturningControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BookBorrowingService borrowingService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testReturnBook_SuccessfulReturn() throws Exception {
        // Mock data
        long bookId = 1L;
        long patronId = 2L;
        BorrowingRecord borrowingRecord = new BorrowingRecord(bookId, patronId, LocalDate.now());

        // Mock behavior
        when(borrowingService.returnBook(bookId, patronId)).thenReturn(borrowingRecord);

        // Perform the request and assert the response
        mockMvc.perform(MockMvcRequestBuilders.put("/api/return/{bookId}/patron/{patronId}", bookId, patronId))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Verify that the service method was called
        verify(borrowingService, times(1)).returnBook(bookId, patronId);
    }

    @Test
    public void testReturnBook_BookNotFound() throws Exception {
        // Mock data
        long bookId = 1L;
        long patronId = 2L;

        // Mock behavior
        when(borrowingService.returnBook(bookId, patronId)).thenThrow(new NoSuchElementException("Book not found"));

        // Perform the request and assert the response
        mockMvc.perform(MockMvcRequestBuilders.put("/api/return/{bookId}/patron/{patronId}", bookId, patronId))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("Book not found"));

        // Verify that the service method was called
        verify(borrowingService, times(1)).returnBook(bookId, patronId);
    }

    @Test
    public void testReturnBook_InvalidRequest() throws Exception {
        // Mock data
        long bookId = 1L;
        long patronId = 2L;

        // Mock behavior
        when(borrowingService.returnBook(bookId, patronId)).thenThrow(new IllegalStateException("Invalid request"));

        // Perform the request and assert the response
        mockMvc.perform(MockMvcRequestBuilders.put("/api/return/{bookId}/patron/{patronId}", bookId, patronId))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Invalid request"));

        // Verify that the service method was called
        verify(borrowingService, times(1)).returnBook(bookId, patronId);
    }

    @Test
    public void testReturnBook_InternalServerError() throws Exception {
        // Mock data
        long bookId = 1L;
        long patronId = 2L;

        // Mock behavior
        when(borrowingService.returnBook(bookId, patronId)).thenThrow(new RuntimeException("Unexpected error"));

        // Perform the request and assert the response
        mockMvc.perform(MockMvcRequestBuilders.put("/api/return/{bookId}/patron/{patronId}", bookId, patronId))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.content().string("An unexpected error occurred"));

        // Verify that the service method was called
        verify(borrowingService, times(1)).returnBook(bookId, patronId);
    }
}
