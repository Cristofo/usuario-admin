package prueba.example.usuario_admin.dto;

import lombok.Data;

@Data
public class PhoneDTO {

	private Long number;
	private int citycode;
	private String countrycode;
}
