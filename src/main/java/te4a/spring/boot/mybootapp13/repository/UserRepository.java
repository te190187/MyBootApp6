package te4a.spring.boot.mybootapp13.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import te4a.spring.boot.mybootapp13.bean.UserBean;

public interface UserRepository extends JpaRepository<UserBean, String>{
  
}
