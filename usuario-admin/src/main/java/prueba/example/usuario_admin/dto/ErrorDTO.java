package prueba.example.usuario_admin.dto;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class ErrorDTO {

    private Timestamp timestamp;
    private int codigo;
    private String detail;
}
