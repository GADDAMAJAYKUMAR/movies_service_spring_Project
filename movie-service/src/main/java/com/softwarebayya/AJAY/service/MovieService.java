package com.softwarebayya.AJAY.service;

import com.softwarebayya.AJAY.model.Movie;
import com.softwarebayya.AJAY.repo.MovieRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    //CRUD operations performing
    //create
    public Movie create(Movie movie){
        if(movie == null){
            throw  new RuntimeException("Invalid Movie");
        }
        return  movieRepository.save(movie);
    }

    public Movie read(Long id){
       return movieRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Movie Not Found"));
    }

    public List<Movie> readAll() {
        return movieRepository.findAll();
    }

    public void update(Movie movie){
        if(movie == null || movie.getId()==null){
            throw  new RuntimeException("Invalid Movie");
        }

        //check
        if(movieRepository.existsById(movie.getId())){
            movieRepository.save(movie);
        }else {
            throw new RuntimeException("movie not found");
        }
    }

    public void delete(Long id){
        if (movieRepository.existsById(id)){
            movieRepository.deleteById(id);
        }else {
             throw new RuntimeException("movie not found");
        }
    }
}
