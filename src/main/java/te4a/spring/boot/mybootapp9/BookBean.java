package te4a.spring.boot.mybootapp9;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "books")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookBean {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false)
    private String title;
    private String writter;
    private String publisher;
    private Integer price;
}
