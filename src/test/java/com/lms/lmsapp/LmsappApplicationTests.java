package com.lms.lmsapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lms.lmsapp.model.Book;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class LmsappApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void contextLoads() {
    }

    @Test
    public void addBook() throws Exception {
        Book book = Book.builder().isbn("1234").title("Book1").author("Author1")
                .pubYear(2014).avlCopies(5).build();

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isCreated());
    }

    @Test
    public void findBookByISBN() throws Exception {
        String isbn = "1234";
        mockMvc.perform(get("/api/books/{isbn}", isbn))
                .andExpect(status().isOk());
    }

    @Test
    public void findBooksByAuthor() throws Exception {
        String author = "Author1";
        mockMvc.perform(get("/api/books?author={author}", author))
                .andExpect(status().isOk());
    }

    @Test
    public void borrowBook() throws Exception {
        String isbn = "1234";
        mockMvc.perform(patch("/api/books/borrow/{isbn}", isbn))
                .andExpect(status().isOk());
    }

    @Test
    public void returnBook() throws Exception {
        String isbn = "1234";
        mockMvc.perform(patch("/api/books/return/{isbn}", isbn))
                .andExpect(status().isOk());
    }

    @Test
    public void removeBook() throws Exception {
        String isbn = "12345";
        Book book = Book.builder().isbn(isbn).title("Book2").author("Author2")
                .pubYear(2015).avlCopies(2).build();

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isCreated());

        mockMvc.perform(delete("/api/books/{isbn}", isbn))
                .andExpect(status().isNoContent());
    }

}

