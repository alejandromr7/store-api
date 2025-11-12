package com.codewithmosh.store.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterUserRequest(
        @NotBlank(message = "The name is required!")
        @Size(max = 255, message = "Name must be less than 255 characters")
        String name,

        @NotBlank(message = "The email is required!")
        @Email(message = "Email must be valid")
        String email,

        @NotBlank(message = "The password is required!")
        @Size(min = 6, max = 25, message = "Password must between 6 to 25 characters")
        String password
) {
}
