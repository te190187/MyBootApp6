package te4a.spring.boot.mybootapp9;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BookForm {
  private Integer id;
  private String title;
  private String writter;
  private String publisher;
  private Integer price;
}
