package prueba.example.usuario_admin.service;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import prueba.example.usuario_admin.entity.UserEntity;
import prueba.example.usuario_admin.repository.UserManagerRepository;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

	private final UserManagerRepository userManagerRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity userEntity = userManagerRepository.findByEmail(username);
		if(null == userEntity) {
			throw new UsernameNotFoundException("Usuario no encontrado.");
		}
		return new User(userEntity.getEmail(), userEntity.getPassword(), new ArrayList<>());
	}
}