package prueba.example.usuario_admin.utils;

import java.text.SimpleDateFormat;
import java.util.Date;


public class UserUtils {
	
	private SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

	
	public String getCurrentDate() {
		return formatter.format(new Date());
	}

}
