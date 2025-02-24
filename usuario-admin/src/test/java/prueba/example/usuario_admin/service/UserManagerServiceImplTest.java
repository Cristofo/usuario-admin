package prueba.example.usuario_admin.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import prueba.example.usuario_admin.config.JwtTokenUtil;
import prueba.example.usuario_admin.dto.LoginResponseDTO;
import prueba.example.usuario_admin.dto.PhoneDTO;
import prueba.example.usuario_admin.dto.SignUpRequestDTO;
import prueba.example.usuario_admin.dto.SignUpResponseDTO;
import prueba.example.usuario_admin.entity.PhoneEntity;
import prueba.example.usuario_admin.entity.UserEntity;
import prueba.example.usuario_admin.exception.GeneralException;
import prueba.example.usuario_admin.repository.UserManagerRepository;
import prueba.example.usuario_admin.service.impl.UserManagerServiceImpl;
import prueba.example.usuario_admin.utils.UserUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserManagerServiceImplTest {

    @Mock
    private UserManagerRepository userRepository;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private UserUtils userUtils;

    @InjectMocks
    private UserManagerServiceImpl userManagerService;

    private UserEntity userEntity;
    private SignUpRequestDTO signUpRequestDTO;

    @BeforeEach
    void setUp() {

        // Initialize test data
        UserEntity userEntityPhone = UserEntity.builder().build();
        userEntity = UserEntity.builder()
                .id(UUID.fromString("f162de5d-da1c-4f38-b5ab-161ceff50583"))
                .email("myemail@gmail.com")
                .name("UsuarioTest")
                .password("encodedPassword")
                .token("oldToken")
                .phones(Collections.singletonList(
                        new PhoneEntity(UUID.fromString("f162de5d-da1c-4f38-b5ab-161ceff50583"),userEntityPhone, 123123L,10, "57")))
                .build();

        signUpRequestDTO = SignUpRequestDTO.builder()
                .email("myemail@gmail.com")
                .name("UsuarioTest")
                .password("password123")
                .phones(Collections.singletonList(new PhoneDTO(123456789L, 10, "57")))
                .build();
    }

    @Test
    void testLoginOK() {
        Map<String, String> headers = new HashMap<>();
        headers.put("authorization", "Bearer oldToken");

        when(userRepository.findByToken("oldToken")).thenReturn(userEntity);
        when(jwtTokenUtil.generateTokenByUserName(anyString())).thenReturn("newToken");


        ResponseEntity<Object> response = userManagerService.login(headers);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof LoginResponseDTO);

        LoginResponseDTO loginResponseDTO = (LoginResponseDTO) response.getBody();
        assertEquals("newToken", loginResponseDTO.getToken());
        assertEquals(UUID.fromString("f162de5d-da1c-4f38-b5ab-161ceff50583"), loginResponseDTO.getId());
        assertEquals("UsuarioTest", loginResponseDTO.getName());
        assertEquals("myemail@gmail.com", loginResponseDTO.getEmail());
    }

    @Test
    void testLogin_InvalidToken() {
        
        Map<String, String> headers = new HashMap<>();
        headers.put("authorization", "Bearer invalidToken");

        when(userRepository.findByToken("invalidToken")).thenReturn(null);
        
        GeneralException exception = assertThrows(GeneralException.class, () -> userManagerService.login(headers));
        assertEquals("Token inválido", exception.getMessage());
    }

    @Test
    void testSignUpOk() {
        UserEntity userEntityPhone = UserEntity.builder().build();
        
        when(userRepository.findByEmail(anyString())).thenReturn(null);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(jwtTokenUtil.generateTokenByUserName(anyString())).thenReturn("newToken");
        when(modelMapper.map(any(), eq(PhoneEntity.class)))
                .thenReturn(new PhoneEntity(UUID.fromString("f162de5d-da1c-4f38-b5ab-161ceff50583"),userEntityPhone, 123123L,10, "57"));
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        
        ResponseEntity<Object> response = userManagerService.signUp(signUpRequestDTO);

        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof SignUpResponseDTO);

        SignUpResponseDTO signUpResponseDTO = (SignUpResponseDTO) response.getBody();
        assertEquals("newToken", signUpResponseDTO.getToken());
    }


    @Test
    void testSignUpOkNoPhones() {

        when(userRepository.findByEmail(anyString())).thenReturn(null);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(jwtTokenUtil.generateTokenByUserName(anyString())).thenReturn("newToken");
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        
        signUpRequestDTO.setPhones(null);
        ResponseEntity<Object> response = userManagerService.signUp(signUpRequestDTO);

        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof SignUpResponseDTO);

        SignUpResponseDTO signUpResponseDTO = (SignUpResponseDTO) response.getBody();
        assertEquals("newToken", signUpResponseDTO.getToken());
    }

    @Test
    void testSignUp_EmailYaExiste() {
        
        when(userRepository.findByEmail(anyString())).thenReturn(userEntity);


        GeneralException exception = assertThrows(GeneralException.class, () -> userManagerService.signUp(signUpRequestDTO));
        assertEquals("El usuario ya existe", exception.getMessage());
    }
}
