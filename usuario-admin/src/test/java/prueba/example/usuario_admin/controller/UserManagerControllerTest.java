package prueba.example.usuario_admin.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import prueba.example.usuario_admin.dto.ErrorListDTO;
import prueba.example.usuario_admin.dto.SignUpRequestDTO;
import prueba.example.usuario_admin.exception.GeneralException;
import prueba.example.usuario_admin.service.UserManagerService;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserManagerControllerTest {

	@Mock
	private UserManagerService userService;

	@InjectMocks
	private UserManagerController userManagerController;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testLogin_OK() {
		
		Map<String, String> headers = Collections.singletonMap("Authorization", "Bearer token");
		ResponseEntity<Object> expectedResponse = ResponseEntity.ok().build();
		when(userService.login(headers)).thenReturn(expectedResponse);

		
		ResponseEntity<Object> response = userManagerController.login(headers);

		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		verify(userService).login(headers);
	}

	@Test
	void testSignUp_Ok() {
		
		SignUpRequestDTO signUpRequestDTO = SignUpRequestDTO.builder().build();
		ResponseEntity<Object> expectedResponse = ResponseEntity.status(HttpStatus.CREATED).build();
		when(userService.signUp(signUpRequestDTO)).thenReturn(expectedResponse);

		
		ResponseEntity<Object> response = userManagerController.signUp(signUpRequestDTO);

		
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		verify(userService).signUp(signUpRequestDTO);
	}

	@Test
	void testHandleValidationExceptions() {
		BindingResult bindingResult = Mockito.mock(BindingResult.class);

		when(bindingResult.getAllErrors()).thenReturn(Collections.singletonList(
				new FieldError("objectName", "fieldName", "defaultMessage")
		));

		MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

		ErrorListDTO errorListDTO = userManagerController.handleValidationExceptions(ex);

		assertEquals(1, errorListDTO.getErrors().size());
	}

	@Test
	void testHandleCustomException() {
		
		GeneralException ex = new GeneralException("Custom error message");

		
		ErrorListDTO errorListDTO = userManagerController.handleCustomException(ex);

		
		assertEquals(1, errorListDTO.getErrors().size());
		assertEquals("Custom error message", errorListDTO.getErrors().get(0).getDetail());
		assertEquals(HttpStatus.BAD_REQUEST.value(), errorListDTO.getErrors().get(0).getCodigo());
	}
}