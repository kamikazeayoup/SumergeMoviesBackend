package com.sumerge.movieservice.service;

import com.sumerge.movieservice.client.ValidationTokenServiceClient;
import com.sumerge.movieservice.model.Genre;
import com.sumerge.movieservice.model.Movie;
import com.sumerge.movieservice.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)

public class MovieServiceTest {
    @Mock
    private MovieRepository movieRepository;

    @Mock
    private ValidationTokenServiceClient validationTokenService;
    @InjectMocks
    private MovieService movieService;

    Movie movie1 , movie2;
    Set<Genre> genres = new HashSet<>();


    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        genres.add(new Genre(1 , "drama"));
        movie1 = new Movie();
        movie1.setId(14);
        movie1.setMovie_name("shawsahank");
        movie1.setDescription("Shawshank Movie");
        movie1.setRating("12");
        movie1.setDuration("120");
        movie1.setPoster_path("/d/aq");
        movie1.setRelease_date(LocalDate.of(2004, Month.JANUARY, 10));
        movie1.setGenres(genres);
        movie2 = new Movie();
        movie2.setId(15);
        movie2.setMovie_name("Bad Guy");
        movie2.setDescription("Bad Guy Movie");
        movie2.setRating("14");
        movie2.setDuration("160");
        movie2.setPoster_path("/ahmed/ali");
        movie2.setRelease_date(LocalDate.of(2005, Month.JANUARY, 10));
        movie2.setGenres(genres);
    }

    @Test
    void getAllMovies_Mocked() {
        List<Movie> list = new ArrayList<>();
        list.add(movie1);
        list.add(movie2);

        PageRequest pageRequest = PageRequest.of(0, 20);

       when(movieRepository.findAll(pageRequest)).thenReturn(new PageImpl<>(list));
        when(validationTokenService.CheckToken("token")).thenReturn(true);
        ResponseEntity response = movieService.getAllMovies("token" , 0 , 20);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(new PageImpl<>(list), response.getBody());
    }

    @Test
    void getMoviesById_Mocked(){
        when(movieRepository.findById(14)).thenReturn(Optional.ofNullable(movie1));
        when(validationTokenService.CheckToken("token")).thenReturn(true);
        ResponseEntity response = movieService.getMovieById(14 , "token");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Optional.ofNullable(movie1), response.getBody());
    }
    @Test
    void getMoviesById_TokenIsNotValid_Mocked(){
        when(validationTokenService.CheckToken("not-valid-token")).thenReturn(false);
        ResponseEntity response = movieService.getMovieById(14 , "not-valid-token");
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("the token is not right", response.getBody());
    }
    @Test
    void getAllMovies_TokenNotValid_Mocked() {
        when(validationTokenService.CheckToken("not-valid-token")).thenReturn(false);
        ResponseEntity response = movieService.getAllMovies("not-valid-token" , 1 , 10);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("the token is not right", response.getBody());
    }
}
