package com.codewithmosh.store.dtos;

public record ChangePasswordRequest(String oldPassword, String newPassword) {
}
