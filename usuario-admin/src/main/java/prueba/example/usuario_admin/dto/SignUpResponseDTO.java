package prueba.example.usuario_admin.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class SignUpResponseDTO {

	private UUID id;

	private String created;

	private String lastLogin;

	private String token;
	
	private boolean isActive;
}
