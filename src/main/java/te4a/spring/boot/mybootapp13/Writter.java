package te4a.spring.boot.mybootapp13;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;


@Documented
@Constraint(validatedBy = WritterValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Writter {
  String ok();
  String message() default "Input {ok}";

  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default{};
}
