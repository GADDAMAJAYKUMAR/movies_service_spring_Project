package com.softwarebayya.AJAY.api;

import com.softwarebayya.AJAY.model.Movie;
import com.softwarebayya.AJAY.service.MovieService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies")
@Slf4j
public class MovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovie(@PathVariable Long id){
        Movie movie = movieService.read(id);
        log.info("Fetched movie with id: {}", id);
        return ResponseEntity.ok(movie);
    }
    @GetMapping("/readAll")
    public ResponseEntity<List<Movie>> getAllMovies() {
        List<Movie> movies = movieService.readAll();
        return ResponseEntity.ok(movies);
    }
    @PostMapping
    public ResponseEntity<Movie> createMovie(@RequestBody Movie movie){
        Movie movieCreated = movieService.create(movie);
        log.info("Created movie with id: {}", movieCreated.getId());
        return ResponseEntity.ok(movieCreated);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Movie> update(@PathVariable Long id, @RequestBody Movie updateMovie){
        updateMovie.setId(id);    // ensure ID from path is used
        movieService.update(updateMovie);
        log.info("Updated movie with id: {}", id);
        return ResponseEntity.ok(updateMovie);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        movieService.delete(id);
        log.info("Deleted movie with id: {}", id);
        return ResponseEntity.noContent().build();
    }
}
