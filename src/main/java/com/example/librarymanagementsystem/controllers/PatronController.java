package com.example.librarymanagementsystem.controllers;

import com.example.librarymanagementsystem.Dto.AuthenticationRequest;
import com.example.librarymanagementsystem.Dto.AuthenticationResponse;
import com.example.librarymanagementsystem.Dto.RegisterRequest;
import com.example.librarymanagementsystem.Dto.UserProfileUpdateDto;
import com.example.librarymanagementsystem.logging.LogExecution;
import com.example.librarymanagementsystem.models.Patron;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.example.librarymanagementsystem.services.PatronService;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/patrons")
@RequiredArgsConstructor
public class PatronController {

    Logger logger = LoggerFactory.getLogger(PatronController.class);

    @Autowired
    private PatronService patronService;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public List<Patron> getAllPatrons()
    {
        logger.info("User with username: {} is fetching all users.",
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName());
        return patronService.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @LogExecution
    public ResponseEntity<Object> getPatron(@PathVariable Long id) {
        try {
            Patron patron = patronService.findById(id);
            return new ResponseEntity<Object>(patron, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<Object>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @LogExecution
    public void deletePatron(@PathVariable Long id) {
        patronService.deleteById(id);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> registerPatron(
            @RequestBody RegisterRequest request
    )
    {
        ResponseEntity<AuthenticationResponse> responseEntity =
                ResponseEntity.ok(patronService.register(request));

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            logger.info("New user registered with username: {}", request.getEmail());
        }

        return responseEntity;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticatePatron(
            @RequestBody AuthenticationRequest request
    )
    {
        ResponseEntity<AuthenticationResponse> responseEntity =
                ResponseEntity.ok(patronService.authenticate(request));

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            logger.info("User authenticated with username: {}", request.getEmail());
        }

        return responseEntity;
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<Patron> updatePatron(
            @PathVariable Long id,
            @RequestBody UserProfileUpdateDto request) {
        try {
            Patron updatedPatron = patronService.updatePatron(id, request);
            logger.info("User authenticated with username: {} is updating his profile.",
                    SecurityContextHolder
                            .getContext()
                            .getAuthentication()
                            .getName());
            return ResponseEntity.ok(updatedPatron);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            // Handle other exceptions (e.g., database errors)
            return ResponseEntity.status(500).build();
        }
    }
}