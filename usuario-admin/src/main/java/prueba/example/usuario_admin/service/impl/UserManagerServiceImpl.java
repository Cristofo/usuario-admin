package prueba.example.usuario_admin.service.impl;


import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import prueba.example.usuario_admin.config.JwtTokenUtil;
import prueba.example.usuario_admin.dto.*;
import prueba.example.usuario_admin.entity.PhoneEntity;
import prueba.example.usuario_admin.entity.UserEntity;
import prueba.example.usuario_admin.exception.GeneralException;
import prueba.example.usuario_admin.repository.UserManagerRepository;
import prueba.example.usuario_admin.service.UserManagerService;
import prueba.example.usuario_admin.utils.UserUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserManagerServiceImpl implements UserManagerService {

	private final ModelMapper modelMapper;

	private final UserManagerRepository userRepository;

	private final JwtTokenUtil jwtTokenUtil;

	private final PasswordEncoder passwordEncoder;

	private final UserUtils userUtils = new UserUtils();

	@Override
	public ResponseEntity<Object> login(Map<String,String> headers) {

		// Obtiene token desde BD para validar que sea igual al del header
		String tokenOld = getHeader("authorization", headers);
		tokenOld = tokenOld.substring(7);
		UserEntity userEntity = userRepository.findByToken(tokenOld);

		if(Objects.isNull(userEntity)){
			throw new GeneralException("Token inválido");
		}

		//Actualizar user con nuevos datos
		final String tokenNew =  jwtTokenUtil.generateTokenByUserName(userEntity.getEmail());
		userEntity.setLastLogin(userUtils.getCurrentDate());
		userEntity.setToken(tokenNew);
		userEntity.setActive(true);
		userRepository.save(userEntity);


		List<PhoneDTO> phoneEntities = mapList(userEntity.getPhones(), PhoneDTO.class);

		// Creamos respuesta con datos de userEntity obtenido desde BD
		LoginResponseDTO loginResponseDTO = LoginResponseDTO.builder()
				.id(userEntity.getId())
				.lastLogin(userEntity.getLastLogin())
				.created(userEntity.getCreated())
				.name(userEntity.getName())
				.token(tokenNew)
				.isActive(userEntity.isActive())
				.email(userEntity.getEmail())
				.password(userEntity.getPassword())
				.phones(phoneEntities)
				.build();

		return new ResponseEntity<>(loginResponseDTO, HttpStatus.OK);

	}

	public ResponseEntity<Object> signUp(SignUpRequestDTO signUpRequestDTO){

		validateUnique(signUpRequestDTO.getEmail());

		// Se genera token a partir del email
		final String token =  jwtTokenUtil.generateTokenByUserName(signUpRequestDTO.getEmail());

		// Se codifica el password
		String encodedPassword = passwordEncoder.encode(signUpRequestDTO.getPassword());

		List<PhoneEntity> phoneEntities = mapList(signUpRequestDTO.getPhones(), PhoneEntity.class);

		//Se Obtienen datos del userEntity
		UserEntity userEntity = UserEntity.builder()
				.email(signUpRequestDTO.getEmail())
				.created(userUtils.getCurrentDate())
				.name(signUpRequestDTO.getName())
				.password(encodedPassword)
				.token(token)
				.phones(phoneEntities)
				.build();

		// Se genera la relación entre user y telefonos en caso de que no sean null
		List<PhoneEntity> phones = userEntity.getPhones();
		if(Objects.nonNull(phones)) {
			phones.forEach(phone -> phone.setUserEntity(userEntity));
		}

		// Se actualiza token
		userRepository.save(userEntity);

		// Se crea response de salida
		SignUpResponseDTO signUpResponseDTO = SignUpResponseDTO.builder()
				.id(userEntity.getId())
				.created(userEntity.getCreated())
				.token(userEntity.getToken())
				.isActive(userEntity.isActive())
				.lastLogin(userEntity.getLastLogin())
				.build();

		return new ResponseEntity<>(signUpResponseDTO, HttpStatus.OK);

	}

	<S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
		if(Objects.isNull(source)) return null;
		return source
				.stream()
				.map(element -> modelMapper.map(element, targetClass))
				.collect(Collectors.toList());
	}

	private String getHeader(String key, Map<String,String> headers){
		return headers.get(key);
	}

	/**
	 * Validamos que el usuario de email sea distinto.
	 * @param email usuario
	 */
	private void validateUnique(String email){
		if(Objects.nonNull(userRepository.findByEmail(email))){
			throw new GeneralException("El usuario ya existe");
		}
	}

}
