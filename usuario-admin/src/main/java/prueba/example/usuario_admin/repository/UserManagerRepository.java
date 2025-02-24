package prueba.example.usuario_admin.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import prueba.example.usuario_admin.entity.UserEntity;

public interface UserManagerRepository extends JpaRepository<UserEntity, Long>{
	UserEntity findByToken(String token);
	UserEntity findByName(String name);
	UserEntity findByEmail(String email);

}
