package com.lms.lmsapp.service;

import com.lms.lmsapp.exceptions.BookAlreadyExistsException;
import com.lms.lmsapp.exceptions.BookByAuthorNotFoundException;
import com.lms.lmsapp.exceptions.BookByISBNNotFoundException;
import com.lms.lmsapp.exceptions.BookCopiesNotAvailableException;
import com.lms.lmsapp.model.Book;
import com.lms.lmsapp.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Thread safe Library class which plays the role of a BookService
 */
@Service
public class Library {

    @Autowired
    private BookRepository bookRepository;
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    // Adds a new book to the library
//    @CachePut(value = "bookCache", key = "#isbn")
    public Book addBook(@org.jetbrains.annotations.NotNull Book book) {
        if (bookRepository.existsById(book.getIsbn())) {
            throw new BookAlreadyExistsException(book.getIsbn());
        }

        readWriteLock.writeLock().lock();
        try {
            return bookRepository.save(book);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    // Removes a book from the library by ISBN
//    @CacheEvict(value = "bookCache", key = "#isbn")
    public void removeBook(String isbn) {
        if (!bookRepository.existsById(isbn)) {
            throw new BookByISBNNotFoundException(isbn);
        }

        readWriteLock.writeLock().lock();
        try {
            bookRepository.deleteById(isbn);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    //Returns a book by its ISBN
//    @Cacheable(value = "bookCache", key = "#isbn")
    public Book findBookByISBN(String isbn) {
        readWriteLock.readLock().lock();
        try {
            return bookRepository.findById(isbn).orElseThrow(() -> new BookByISBNNotFoundException(isbn));
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    //Returns a list of books by a given author
    public List<Book> findBooksByAuthor(String author) {
        readWriteLock.readLock().lock();
        try {
            List<Book> booksByAuthor = bookRepository.findByAuthor(author);
            if (booksByAuthor.isEmpty()) {
                throw new BookByAuthorNotFoundException(author);
            }
            return booksByAuthor;
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    //Decreases the available copies of a book by 1
//    @CachePut(value = "bookCache", key = "#isbn")
    public boolean borrowBook(String isbn) {
        readWriteLock.writeLock().lock();
        try {
            readWriteLock.readLock().lock();
            Book book = bookRepository.findById(isbn).orElseThrow(() -> new BookByISBNNotFoundException(isbn));
            if (book.getAvlCopies() <= 0) {
                throw new BookCopiesNotAvailableException(isbn);
            }
            book.setAvlCopies(book.getAvlCopies() - 1);
            bookRepository.save(book);
            return true;
        } finally {
            readWriteLock.readLock().unlock();
            readWriteLock.writeLock().unlock();
        }
    }

    //Increases the available copies of a book by 1
//    @CachePut(value = "bookCache", key = "#isbn")
    public boolean returnBook(String isbn) {
        readWriteLock.writeLock().lock();
        try {
            readWriteLock.readLock().lock();
            Book book = bookRepository.findById(isbn).orElseThrow(() -> new BookByISBNNotFoundException(isbn));
            book.setAvlCopies(book.getAvlCopies() + 1);
            bookRepository.save(book);
            return true;
        } finally {
            readWriteLock.readLock().unlock();
            readWriteLock.writeLock().unlock();
        }
    }


}
