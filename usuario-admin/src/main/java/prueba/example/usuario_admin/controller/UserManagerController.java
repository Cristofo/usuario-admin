package prueba.example.usuario_admin.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import prueba.example.usuario_admin.dto.SignUpRequestDTO;
import prueba.example.usuario_admin.dto.ErrorDTO;
import prueba.example.usuario_admin.dto.ErrorListDTO;
import prueba.example.usuario_admin.exception.GeneralException;
import prueba.example.usuario_admin.service.UserManagerService;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(value="/api")
@RequiredArgsConstructor
public class UserManagerController {

	private final UserManagerService userService;

	
	@PostMapping("/login")
	public ResponseEntity<Object> login(@RequestHeader Map<String,String> headers) {
		return userService.login(headers);
	}

	@PostMapping("/sign-up")
	public ResponseEntity<Object> signUp(@Valid @RequestBody SignUpRequestDTO authRequest){
		return userService.signUp(authRequest);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
		public ErrorListDTO handleValidationExceptions(MethodArgumentNotValidException ex) {

		ErrorListDTO errors = ErrorListDTO.builder().build();
		List<ErrorDTO> listErrorDto = new ArrayList<>();
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());

		ex.getBindingResult().getAllErrors().forEach((error) -> {
			ErrorDTO errorDTO = ErrorDTO.builder().build();
			errorDTO.setCodigo(HttpStatus.BAD_REQUEST.value());
			errorDTO.setTimestamp(timestamp);
			errorDTO.setDetail(error.getDefaultMessage());
			listErrorDto.add(errorDTO);
		});
		errors.setErrors(listErrorDto);
		return errors;
	}

	@ExceptionHandler(GeneralException.class)
	public ErrorListDTO handleCustomException(GeneralException ex) {

		ErrorListDTO errors = ErrorListDTO.builder().build();
		List<ErrorDTO> listErrorDto = new ArrayList<>();

		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		ErrorDTO errorDTO = ErrorDTO.builder().build();
		errorDTO.setCodigo(HttpStatus.BAD_REQUEST.value());
		errorDTO.setTimestamp(timestamp);
		errorDTO.setDetail(ex.getMessage());

		listErrorDto.add(errorDTO);
		errors.setErrors(listErrorDto);

		return errors;
	}
}
