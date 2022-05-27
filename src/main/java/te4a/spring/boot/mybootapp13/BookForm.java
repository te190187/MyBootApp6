package te4a.spring.boot.mybootapp13;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BookForm {
  private Integer id;

  @NotNull
  @Size(min = 3)
  private String title;

  @Size(min = 3, max = 20)
  @Writter(ok = "東北タロウ")
  private String writter;

  private String publisher;

  @Min(0)
  private Integer price;
}
