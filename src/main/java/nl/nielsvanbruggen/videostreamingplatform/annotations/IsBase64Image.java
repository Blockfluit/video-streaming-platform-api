package nl.nielsvanbruggen.videostreamingplatform.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import nl.nielsvanbruggen.videostreamingplatform.validators.Base64ImageValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = Base64ImageValidator.class)
@Target( { ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface IsBase64Image {
    String message() default "This is not a base64 image";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
