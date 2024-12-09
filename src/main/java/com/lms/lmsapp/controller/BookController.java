package com.lms.lmsapp.controller;

import com.lms.lmsapp.model.Book;
import com.lms.lmsapp.service.Library;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/books")
public class BookController {

    @Autowired
    private Library library;

    @PostMapping
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        Book addedBook = library.addBook(book);
        return new ResponseEntity<>(addedBook, HttpStatus.CREATED);
    }

    @DeleteMapping("/{isbn}")
    public ResponseEntity<Void> removeBook(@PathVariable String isbn) {
        library.removeBook(isbn);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{isbn}")
    public ResponseEntity<Book> findBookByIsbn(@PathVariable String isbn) {
        Book book = library.findBookByISBN(isbn);
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Book>> findBooksByAuthor(@RequestParam String author) {
        List<Book> books = library.findBooksByAuthor(author);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @PatchMapping("/borrow/{isbn}")
    public ResponseEntity<Void> borrowBook(@PathVariable String isbn) {
        library.borrowBook(isbn);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/return/{isbn}")
    public ResponseEntity<Void> returnBook(@PathVariable String isbn) {
        library.returnBook(isbn);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}