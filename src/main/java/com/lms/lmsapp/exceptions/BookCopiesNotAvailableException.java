package com.lms.lmsapp.exceptions;

public class BookCopiesNotAvailableException extends RuntimeException {
    public BookCopiesNotAvailableException(String isbn) {
        super("No copies of the book are available with ISBN: " + isbn);
    }
}
