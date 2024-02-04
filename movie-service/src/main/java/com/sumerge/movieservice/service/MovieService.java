package com.sumerge.movieservice.service;

import com.sumerge.movieservice.client.ValidationTokenServiceClient;
import com.sumerge.movieservice.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class MovieService {
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private ValidationTokenServiceClient validationTokenService;

    public ResponseEntity<?> getAllMovies(String token , int page , int size){
        if(!validationTokenService.CheckToken(token))
            return new ResponseEntity<>("the token is not right" , HttpStatus.UNAUTHORIZED);
        PageRequest pageRequest = PageRequest.of(page, size);
        return new ResponseEntity<>(movieRepository.findAll(pageRequest) , HttpStatus.OK);
    }
    public ResponseEntity<?> getMovieById(int id , String token){
        if(!validationTokenService.CheckToken(token))
            return new ResponseEntity<>("the token is not right" , HttpStatus.UNAUTHORIZED);

        return new ResponseEntity<>(movieRepository.findById(id) , HttpStatus.OK) ;
    }

}
