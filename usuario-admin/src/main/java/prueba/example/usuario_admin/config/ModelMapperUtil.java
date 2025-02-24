package prueba.example.usuario_admin.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperUtil {
	
	@Bean
	public ModelMapper modelMapper() {
		
	    return new ModelMapper();
	}

}
