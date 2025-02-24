package prueba.example.usuario_admin.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString (exclude = {"phones"})
@Table(name="USER")
public class UserEntity {

	@Id
	@GeneratedValue(generator = "uuid-hibernate-generator")
	@GenericGenerator(name = "uuid-hibernate-generator", strategy = "org.hibernate.id.UUIDGenerator")
	private UUID id ;
	
	private String name;
	
	@Column(unique=true)
	private String email;

	private String password;

	private String created;

	private String lastLogin;

	private String token;

	private boolean isActive;

	@JsonBackReference
	@OneToMany(mappedBy = "userEntity", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<PhoneEntity> phones;

}
