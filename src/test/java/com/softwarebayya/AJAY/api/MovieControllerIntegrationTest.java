package com.softwarebayya.AJAY.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softwarebayya.AJAY.model.Movie;
import com.softwarebayya.AJAY.repo.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;


import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@AutoConfigureMockMvc
@SpringBootTest
class MovieControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    MovieRepository movieRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void cleanUp(){
        movieRepository.deleteAllInBatch();
    }

    @Test
    void givenMovie_whenCreateMovie_thenReturnSavedMovie() throws Exception {

        // Given
        Movie movie = new Movie();
        movie.setName("RRR");
        movie.setDirector("SS Rajamouli");
        movie.setActors(List.of("NTR", "Ram Charan", "Alia Bhatt"));

        // When & Then
        mockMvc.perform(post("/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movie)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("RRR"))
                .andExpect(jsonPath("$.director").value("SS Rajamouli"))
                .andExpect(jsonPath("$.actors[0]").value("NTR"));
    }


    @Test
    void givenMovieId_whenGetMovie_thenReturnMovie() throws Exception {

        // First, create a movie
        Movie movie = new Movie();
        movie.setName("KGF");
        movie.setDirector("Prashanth Neel");
        movie.setActors(List.of("Yash", "Srinidhi Shetty"));

        var createResponse = mockMvc.perform(post("/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movie)))
                .andExpect(status().isOk())
                .andReturn();

        // Extract returned movie JSON → convert to Movie object
        String responseBody = createResponse.getResponse().getContentAsString();
        Movie savedMovie = objectMapper.readValue(responseBody, Movie.class);

        // Now fetch the movie using GET /movies/{id}
        mockMvc.perform(get("/movies/" + savedMovie.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedMovie.getId()))
                .andExpect(jsonPath("$.name").value("KGF"))
                .andExpect(jsonPath("$.director").value("Prashanth Neel"))
                .andExpect(jsonPath("$.actors[0]").value("Yash"));
    }
    @Test
    void givenUpdatedMovie_whenUpdateMovie_thenReturnUpdatedMovie() throws Exception {

        // 1. Create a movie first
        Movie movie = new Movie();
        movie.setName("Pushpa");
        movie.setDirector("Sukumar");
        movie.setActors(List.of("Allu Arjun"));

        var createResponse = mockMvc.perform(post("/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movie)))
                .andExpect(status().isOk())
                .andReturn();

        Movie savedMovie = objectMapper.readValue(createResponse.getResponse().getContentAsString(), Movie.class);

        // 2. Update the movie
        savedMovie.setName("Pushpa 2");
        savedMovie.setActors(List.of("Allu Arjun", "Rashmika Mandanna"));

        mockMvc.perform(put("/movies/" + savedMovie.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(savedMovie)))
                .andExpect(status().isOk());

        // 3. Verify update by GET
        mockMvc.perform(get("/movies/" + savedMovie.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Pushpa 2"))
                .andExpect(jsonPath("$.actors[1]").value("Rashmika Mandanna"));
    }
    @Test
    void givenMovieId_whenDeleteMovie_thenMovieShouldBeRemoved() throws Exception {

        // 1. Create a movie
        Movie movie = new Movie();
        movie.setName("Bahubali");
        movie.setDirector("Rajamouli");
        movie.setActors(List.of("Prabhas"));

        var createResponse = mockMvc.perform(post("/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movie)))
                .andExpect(status().isOk())
                .andReturn();

        Movie savedMovie = objectMapper.readValue(createResponse.getResponse().getContentAsString(), Movie.class);

        // 2. Delete movie
        mockMvc.perform(delete("/movies/" + savedMovie.getId()))
                .andExpect(status().isOk());

        // 3. Verify deletion (GET should now fail)
        mockMvc.perform(get("/movies/" + savedMovie.getId()))
                .andExpect(status().is4xxClientError());
    }


}
