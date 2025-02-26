package prueba.example.usuario_admin.dto;

import lombok.Builder;
import lombok.Data;
import prueba.example.usuario_admin.validator.PasswordValidator;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
public class SignUpRequestDTO {

	private String name;

	@Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}", message = "Email Inválido")
	private String email;

	@Size(min = 8, max = 12, message = "el password debe contener entre 8 y 12")
	@PasswordValidator(message = "Password debe contener 1 Mayúscula y 2 números")
    private String password;

	private List<PhoneDTO> phones;
}
