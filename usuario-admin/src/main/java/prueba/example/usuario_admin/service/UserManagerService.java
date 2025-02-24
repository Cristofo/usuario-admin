package prueba.example.usuario_admin.service;


import org.springframework.http.ResponseEntity;
import prueba.example.usuario_admin.dto.SignUpRequestDTO;

import java.util.Map;


public interface UserManagerService {

	ResponseEntity<Object> login(Map<String,String> headers);


	ResponseEntity<Object> signUp(SignUpRequestDTO signUpRequestDTO);
}
