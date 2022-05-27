package te4a.spring.boot.mybootapp13;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class WritterValidator implements ConstraintValidator<Writter, String> {
  String ok;

  @Override
  public void initialize(Writter w) {
    ok = w.ok();
  }

  @Override
  public boolean isValid(String in, ConstraintValidatorContext ctx) {
    if(in == null){
      return false;
    }
    return in.equals(ok);
  }
  
  
}
