package prueba.example.usuario_admin.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ErrorListDTO{
	
	List<ErrorDTO> errors;
}
