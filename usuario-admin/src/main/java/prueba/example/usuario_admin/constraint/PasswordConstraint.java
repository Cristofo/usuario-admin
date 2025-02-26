package prueba.example.usuario_admin.constraint;



import prueba.example.usuario_admin.validator.PasswordValidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class PasswordConstraint implements ConstraintValidator<PasswordValidator, String> {

    @Override
    public void initialize(PasswordValidator constraintAnnotation) {

    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintContext) {
        return password.matches("^\\D*(?:\\d\\D*){2}$") &&
                password.matches("^[^A-Z]*[A-Z][^A-Z]*$");
    }
}
