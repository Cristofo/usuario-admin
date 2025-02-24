package prueba.example.usuario_admin.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import prueba.example.usuario_admin.entity.PhoneEntity;

public interface PhoneRepository extends JpaRepository<PhoneEntity, Long>{

}
