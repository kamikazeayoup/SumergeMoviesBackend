package com.sumerge.authservice.service;

import com.sumerge.authservice.model.Role;
import com.sumerge.authservice.model.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserDetailsServiceTest {
    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void TestLoadByUserName(){
        assertThrows(Exception.class , ()->{
            customUserDetailsService.loadUserByUsername("m@a.com");
        });
    }


}
