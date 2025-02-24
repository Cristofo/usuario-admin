package prueba.example.usuario_admin.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserManagerDTO {

	private String name;
	private String email;
	private String password;
	private String creationDate;
	private String modificationDate;
	private String lastLoginDate;
	private String token;
	private boolean isActive;
	private List<PhoneDTO> phones;
}
