package prueba.example.usuario_admin.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import prueba.example.usuario_admin.entity.UserEntity;
import prueba.example.usuario_admin.repository.UserManagerRepository;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MyUserDetailsServiceTest {

    @Mock
    private UserManagerRepository userManagerRepository;

    @InjectMocks
    private MyUserDetailsService myUserDetailsService;

    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        userEntity = new UserEntity();
        userEntity.setEmail("test@gmail.com");
        userEntity.setPassword("encodedPassword");
    }

    @Test
    void testLoadUserByUsernameOK() {
        when(userManagerRepository.findByEmail("test@gmail.com")).thenReturn(userEntity);

        UserDetails userDetails = myUserDetailsService.loadUserByUsername("test@gmail.com");

        assertNotNull(userDetails);
        assertEquals("test@gmail.com", userDetails.getUsername());
        assertEquals("encodedPassword", userDetails.getPassword());
    }

    @Test
    void testLoadUserByUsername_UsuarioNoEncontrado() {

        when(userManagerRepository.findByEmail("noexiste@gmail.com")).thenReturn(null);

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            myUserDetailsService.loadUserByUsername("noexiste@gmail.com");
        });

        assertEquals("Usuario no encontrado.", exception.getMessage());
    }
}