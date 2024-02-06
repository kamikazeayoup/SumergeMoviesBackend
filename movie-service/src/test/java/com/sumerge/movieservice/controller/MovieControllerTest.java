package com.sumerge.movieservice.controller;

import com.sumerge.movieservice.model.Genre;
import com.sumerge.movieservice.model.Movie;
import com.sumerge.movieservice.service.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import  org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

@WebMvcTest(MovieController.class)
public class MovieControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    MovieService movieService;

    Movie movie1 = new Movie();
    Movie movie2 = new Movie();

    Set<Genre> genres = new HashSet<>();

    @BeforeEach
    void setData(){
        genres.add(new Genre(1 , "drama"));
        movie1.setId(14);
        movie1.setMovie_name("shawsahank");
        movie1.setDescription("Shawshank Movie");
        movie1.setRating("12");
        movie1.setDuration("120");
        movie1.setPoster_path("/d/aq");
        movie1.setRelease_date(LocalDate.of(2004, Month.JANUARY, 10));
        movie1.setGenres(genres);
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
    void getAllMoviesSuccessTest() throws Exception{
        List<Movie> movieList = new ArrayList<>(Arrays.asList(movie1 , movie2));
        ResponseEntity r=new ResponseEntity(movieList , HttpStatus.OK);

        Mockito.when(movieService.getAllMovies("ssaw" , 0 , 10)).thenReturn(r);
        ResponseEntity result = movieService.getAllMovies("ssaw" , 0 , 10);
        List<Movie> actualMovies = (List<Movie>) result.getBody();

        System.out.println(movieService.getAllMovies("ssaw" , 0 , 10));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/movie?token=token&page=0&size=20")
                        .contentType(MediaType.APPLICATION_JSON))
                 .andExpect(MockMvcResultMatchers.status().isOk());


        assertThat(actualMovies).hasSize(2);
    }
    @Test
    void getMovieByIdSuccessTest() throws Exception{
        ResponseEntity response = new ResponseEntity(movie1 , HttpStatus.OK);
        Mockito.when(movieService.getMovieById(14 , "token")).thenReturn(response);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/movie/14?token=token")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(result -> {
                    String responseBody = result.getResponse().getContentAsString();
                    System.out.println("Response Body: " + responseBody);
                    assertThat(responseBody.subSequence(6 , 8)).isEqualTo(movie1.getId().toString());
                });    }



}
