package te4a.spring.boot.mybootapp13.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.List;

import javax.sql.DataSource;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.Operations;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.destination.Destination;
import com.ninja_squad.dbsetup.operation.Operation;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import te4a.spring.boot.mybootapp13.BookApplication;
import te4a.spring.boot.mybootapp13.form.BookForm;

import static org.assertj.core.api.Assertions.*;

@ContextConfiguration(classes = BookApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WithUserDetails(value = "testuser", userDetailsServiceBeanName = "loginUserDetailsService")
public class BookControllerTest {

  public static final Operation INSERT_BOOK_DATA1 = Operations
    .insertInto("books")
	  .columns("id", "title", "writter", "publisher", "price")
    .values(1, "タイトル１", "著者１", "出版社１", 100)
    .build();

  public static final Operation INSERT_BOOK_DATA2 = Operations
    .insertInto("books")
	  .columns("id", "title", "writter", "publisher", "price")
    .values(2, "タイトル２", "著者２", "出版社２", 200)
    .build();

  @Autowired
  MockMvc mockMvc;

  @Autowired
  WebApplicationContext wac;

  @Autowired
  private DataSource dataSrouce;

  @BeforeAll
  public void テスト前処理() {
    InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
    viewResolver.setPrefix("/templates");
    viewResolver.setSuffix(".htlm");
    mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
  }

  @SuppressWarnings("unchecked")
  @Test
  public void 書籍追加一覧ページ表示_書籍あり() throws Exception {
    //DB状態
    //書籍:3冊
    Destination dest = new DataSourceDestination(dataSrouce);
    Operation ops = Operations.sequenceOf(INSERT_BOOK_DATA1, INSERT_BOOK_DATA2);
    DbSetup dbSetup = new DbSetup(dest, ops);
    dbSetup.launch();

    BookForm form1 = new BookForm();
    form1.setId(1);
    form1.setTitle("タイトル１");
    form1.setWritter("著者１");
    form1.setPublisher("出版社１");
    form1.setPrice(100);

    BookForm form2 = new BookForm();
    form2.setId(2);
    form2.setTitle("タイトル２");
    form2.setWritter("著者２");
    form2.setPublisher("出版社２");
    form2.setPrice(200);

    MvcResult result = mockMvc.perform(get("/books"))
      .andExpect(status().is2xxSuccessful())
      .andExpect(view().name("books/list"))
      .andReturn();

    try {
      List<BookForm> list = (List<BookForm>) result
        .getModelAndView().getModel().get("books");

      assertThat(list).contains(form1, form2);
    } catch (NullPointerException e) {
      throw new Exception(e);
    }
  }
}
