package com.lms.lmsapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lms.lmsapp.model.Book;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class LmsappRateLimitTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RateLimiterRegistry rateLimiterRegistry;

    @Test
    public void testRateLimit() throws Exception {
        RateLimiter rateLimiter = rateLimiterRegistry.rateLimiter("default");
        String isbn = "01234";

        Book book = Book.builder().isbn(isbn).title("Book1").author("Author1")
                .pubYear(2014).avlCopies(5).build();

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isCreated());

        // handle requests that are rate-limited
        for (int i = 0; i < 5; i++) {
            try {
                rateLimiter.acquirePermission();
                mockMvc.perform(MockMvcRequestBuilders.get("/api/books/{isbn}", isbn));

            } catch (Exception e) {
                mockMvc.perform(MockMvcRequestBuilders.get("/api/books/{isbn}", isbn))
                        .andExpect(status().isTooManyRequests());
            }
        }
    }

}

