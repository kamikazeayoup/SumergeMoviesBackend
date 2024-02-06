package com.sumerge.authservice.service;

import com.sumerge.authservice.model.Role;
import com.sumerge.authservice.model.UserEntity;
import com.sumerge.authservice.repository.UserEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.reactivestreams.Publisher;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static reactor.core.publisher.Mono.when;

public class UserDetailsServiceTest {
    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private UserEntityRepository userRepository;

    List<Role> roles;


    @BeforeEach
    public void init() {
        Role newRole = new Role();
        newRole.setId(1);
        newRole.setName("USER");
        roles = new ArrayList<>(Arrays.asList(newRole));

        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loadByUserNameSuccessTest(){
        UserEntity user = new UserEntity(1 , "usermame" , "email@emil.com" , "123" , roles);


        Mockito.when(userRepository.save(user)).thenReturn(user);
        Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        UserDetails res = customUserDetailsService.loadUserByUsername(user.getEmail());

    }


}
