package com.lms.lmsapp.exceptions;

public class BookAlreadyExistsException extends RuntimeException {
    public BookAlreadyExistsException(String isbn) {
        super("Book already exists with ISBN: " + isbn);
    }
}
