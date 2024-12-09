package com.lms.lmsapp.exceptions;

public class BookByAuthorNotFoundException extends RuntimeException {
    public BookByAuthorNotFoundException(String author) {
        super("Book not found with Author: " + author);
    }
}
