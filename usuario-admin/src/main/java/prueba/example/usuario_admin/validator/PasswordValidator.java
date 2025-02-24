package prueba.example.usuario_admin.validator;

import prueba.example.usuario_admin.constraint.PasswordConstraint;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordConstraint.class)
@Documented
public @interface PasswordValidator {

    String message() default "Password invalido";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}