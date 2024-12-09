package com.lms.lmsapp.exceptions;

public class BookByISBNNotFoundException extends RuntimeException {
    public BookByISBNNotFoundException(String isbn) {
        super("Book not found for ISBN: " + isbn);
    }
}
