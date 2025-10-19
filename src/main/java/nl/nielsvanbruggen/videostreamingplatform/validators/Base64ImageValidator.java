package nl.nielsvanbruggen.videostreamingplatform.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import nl.nielsvanbruggen.videostreamingplatform.annotations.IsBase64Image;

import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Base64ImageValidator implements ConstraintValidator<IsBase64Image, String> {
    private static final Pattern base64ImagePattern = Pattern.compile("data:image/(.+);base64");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext cxt) {
        if(value == null) return true;

        try {
            String[] parts = value.split(",");
            Matcher matcher = base64ImagePattern.matcher(parts[0]);

            if(parts.length < 2 || !matcher.find()) return false;

            Base64.getDecoder().decode(parts[1]);

            return true;
        } catch(IllegalArgumentException ignored) {
            return false;
        }
    }
}