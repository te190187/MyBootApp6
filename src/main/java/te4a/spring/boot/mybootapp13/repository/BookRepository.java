package te4a.spring.boot.mybootapp13.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import te4a.spring.boot.mybootapp13.bean.BookBean;
public interface BookRepository extends JpaRepository<BookBean, Integer>{

}