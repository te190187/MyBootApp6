package te4a.spring.boot.mybootapp11;

import org.springframework.data.jpa.repository.JpaRepository;
public interface BookRepository extends JpaRepository<BookBean, Integer>{

}