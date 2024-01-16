package com.example.librarymanagementsystem.services;

import com.example.librarymanagementsystem.Dto.UserProfileUpdateDto;
import com.example.librarymanagementsystem.config.JwtService;
import com.example.librarymanagementsystem.Dto.AuthenticationRequest;
import com.example.librarymanagementsystem.Dto.AuthenticationResponse;
import com.example.librarymanagementsystem.Dto.RegisterRequest;
import com.example.librarymanagementsystem.models.Patron;
import com.example.librarymanagementsystem.models.Role;
import com.example.librarymanagementsystem.repositories.PatronRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PatronService {

    @Autowired
    private final PatronRepository patronRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public List<Patron> findAll() {
        return patronRepository.findAll();
    }

    public Patron findById(Long id) {
        return patronRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with ID " + id + " not found"));
    }

    public void deleteById(Long id) {
        patronRepository.deleteById(id);
    }

    public Patron save(Patron patron) {
        return patronRepository.save(patron);
    }

    public AuthenticationResponse register(RegisterRequest request){
        Patron patron = Patron.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        patronRepository.save(patron);
        String jwtToken = jwtService.generateToken(patron);
        return AuthenticationResponse.builder()
                .token(jwtToken).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
                )
        );
        Patron patron = patronRepository.findByEmail(request.getEmail())
                .orElseThrow();
        String jwtToken = jwtService.generateToken(patron);
        return AuthenticationResponse.builder()
                .token(jwtToken).build();
    }

    public Patron updatePatron(Long id, UserProfileUpdateDto userProfileUpdateDto) {
        try {
            Patron patron = patronRepository.findById(id).orElseThrow();

            // Update only if the new first name is provided
            if (userProfileUpdateDto.getFirstname() != null) {
                patron.setFirstname(userProfileUpdateDto.getFirstname());
            }

            // Update only if the new last name is provided
            if (userProfileUpdateDto.getLastname() != null) {
                patron.setLastname(userProfileUpdateDto.getLastname());
            }

            // Update and encode the password only if the new password is provided
            if (userProfileUpdateDto.getPassword() != null) {
                String encodedPassword = passwordEncoder.encode(userProfileUpdateDto.getPassword());
                patron.setPassword(encodedPassword);
            }

            return patronRepository.save(patron);

        } catch (NoSuchElementException e) {
            // Handle the case where the patron with the given id is not found
            throw new NoSuchElementException("Patron not found with id: " + id);
        }
    }
}