package com.sumerge.movieservice.controller;

import com.sumerge.movieservice.model.Movie;
import com.sumerge.movieservice.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/movie")
@CrossOrigin(origins = "*", allowedHeaders = "*")

public class MovieController {
    @Autowired
    private MovieService movieService;

    @GetMapping("")
    @CrossOrigin(origins = "*", allowedHeaders = "*")

    public ResponseEntity<?> getAllMovies(String token , @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "20") int size){
    return movieService.getAllMovies(token , page , size);
}
@GetMapping("/{id}")
@CrossOrigin(origins = "*", allowedHeaders = "*")

public ResponseEntity<?> getMovieById(@PathVariable int id , String token){
        return movieService.getMovieById(id , token);
}

}
