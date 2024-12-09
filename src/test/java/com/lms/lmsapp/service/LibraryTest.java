package com.lms.lmsapp.service;

import com.lms.lmsapp.model.Book;
import com.lms.lmsapp.repository.BookRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

//@SpringBootTest
class LibraryTest {

    private Book book;
    private final String ISBN1 = "1234";
    private final String AUTHOR1 = "Author1";
    private final String TITLE1 = "Title1";
    private final int PUB_YEAR = 2014;
    private final int AVL_COPIES = 3;

    @AfterEach
    void tearDown() {
    }


    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private Library service;

    @BeforeEach
    void setUp() {
        book = Book.builder().title(TITLE1).author(AUTHOR1)
                .avlCopies(AVL_COPIES).pubYear(PUB_YEAR).isbn(ISBN1).build();
        openMocks(this);
    }

    @Test
    void addBook() {
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        Book result = service.addBook(book);
        assertEquals(ISBN1, result.getIsbn());
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void removeBook() {
        when(bookRepository.existsById(ISBN1)).thenReturn(true);
        doNothing().when(bookRepository).deleteById(ISBN1);
        service.removeBook(ISBN1);
        verify(bookRepository, times(1)).deleteById(ISBN1);
    }

    @Test
    void findBookByISBN() {
        when(bookRepository.findById(ISBN1)).thenReturn(Optional.of(book));
        Book result = service.findBookByISBN(ISBN1);
        assertEquals(ISBN1, result.getIsbn());
        verify(bookRepository, times(1)).findById(ISBN1);
    }

    @Test
    void findBooksByAuthor() {
        when(bookRepository.findByAuthor(AUTHOR1)).thenReturn(List.of(book));
        List<Book> result = service.findBooksByAuthor(AUTHOR1);
        assertEquals(AUTHOR1, result.get(0).getAuthor());
        verify(bookRepository, times(1)).findByAuthor(AUTHOR1);
    }


    @Test
    void borrowBook() {
        book.setAvlCopies(10);
        when(bookRepository.findById(ISBN1)).thenReturn(Optional.of(book));

        doAnswer(invocation -> {
            book.setAvlCopies(9);
            return null;
        }).when(bookRepository).save(any(Book.class));

        service.borrowBook(ISBN1);
        assertEquals(9, book.getAvlCopies());
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void returnBook() {
        book.setAvlCopies(9);
        when(bookRepository.findById(ISBN1)).thenReturn(Optional.of(book));

        doAnswer(invocation -> {
            book.setAvlCopies(10);
            return null;
        }).when(bookRepository).save(any(Book.class));

        service.returnBook(ISBN1);
        assertEquals(10, book.getAvlCopies());
        verify(bookRepository, times(1)).save(book);
    }

}
