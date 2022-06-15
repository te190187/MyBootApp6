package te4a.spring.boot.mybootapp13.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
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
@AutoConfigureMockMvc
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WithUserDetails(value = "testuser", userDetailsServiceBeanName = "loginUserDetailsService")
public class BookControllerTest {

  public static class BookData {
    public BookForm form;

    public BookData(BookForm form){
      this.form = form;
    }

    public Operation insertOperation(){
      return Operations
        .insertInto("books")
        .columns("id", "title", "writter", "publisher", "price")
        .values(form.getId(), form.getTitle(), form.getWritter(), form.getPublisher(), form.getPrice())
        .build();
    }
  }

  public static final BookData bookData1 = new BookData(new BookForm(1,"タイトル1", "著者1", "出版社1", 100));
  public static final BookData bookData2 = new BookData(new BookForm(2,"タイトル2", "著者2", "出版社2", 200));

  @Autowired
  MockMvc mockMvc;

  @Autowired
  WebApplicationContext wac;

  @Autowired
  private DataSource dataSource;

  @BeforeAll
  public void テスト前処理() {
    InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
    viewResolver.setPrefix("/templates");
    viewResolver.setSuffix(".htlm");
    mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
  }

  // マイグレーションファイルの内でINSERTされた行を消す
  @BeforeEach
  public void cleanUp() {
    Destination dest = new DataSourceDestination(dataSource);
    DbSetup dbSetup = new DbSetup(dest, Operations.deleteAllFrom("books"));
    dbSetup.launch();
  }


  @SuppressWarnings("unchecked")
  @Test
  public void 存在する書籍を全て取得できる() throws Exception {
    //DB状態
    //書籍:3冊
    Destination dest = new DataSourceDestination(dataSource);
    Operation ops = Operations.sequenceOf(bookData1.insertOperation(), bookData2.insertOperation());
    DbSetup dbSetup = new DbSetup(dest, ops);
    dbSetup.launch();

    MvcResult result = mockMvc.perform(get("/books"))
      .andExpect(status().is2xxSuccessful())
      .andExpect(view().name("books/list"))
      .andReturn();

    try {
      List<BookForm> list = (List<BookForm>) result
        .getModelAndView().getModel().get("books");

      assertThat(list).contains(bookData1.form, bookData2.form);
    } catch (NullPointerException e) {
      throw new Exception(e);
    }
  }

  @SuppressWarnings("unchecked")
  @Test
  public void 書籍が存在しない場合は書籍を取得できない() throws Exception {
    var result = mockMvc.perform(get("/books"))
      .andExpect(status().isOk())
      .andExpect(view().name("books/list"))
      .andReturn();
    
      var books = (List<BookForm>)result
        .getModelAndView().getModel().get("books");

      assertThat(books.size()).isEqualTo(0);
  }

  @SuppressWarnings("unchecked")
  @Test
  public void 書籍を削除できる() throws Exception {
    Destination dest = new DataSourceDestination(dataSource);
    Operation ops = Operations.sequenceOf(bookData1.insertOperation(), bookData2.insertOperation());
    DbSetup dbSetup = new DbSetup(dest, ops);
    dbSetup.launch();

    // 書籍の削除リクエスト
    mockMvc.perform(
      post("/books/delete")
      .param("id", bookData1.form.getId().toString())
      .with(SecurityMockMvcRequestPostProcessors.csrf())
    )
    .andExpect(status().is3xxRedirection())
    .andReturn();

    // 削除後のページを確認する
    var result = mockMvc.perform(get("/books"))
      .andExpect(status().isOk())
      .andReturn();

    try{
      var books = (List<BookForm>)result
        .getModelAndView().getModel().get("books");

      assertThat(books)
        .doesNotContain(bookData1.form)
        .contains(bookData2.form);
    } catch (Exception e){
      throw new Exception(e);
    }
  }

  @SuppressWarnings("unchecked")
  @Test
  public void 書籍を登録できる() throws Exception {
    var bookForm = new BookForm(1, "たいとる", "東北タロウ", "しゅっぱんしゃ", 100);


    // 書籍の登録リクエスト
    mockMvc.perform(
      post("/books/create")
      .param("title", bookForm.getTitle())
      .param("writter", bookForm.getWritter())
      .param("publisher", bookForm.getPublisher())
      .param("price",bookForm.getPrice().toString())
      .with(SecurityMockMvcRequestPostProcessors.csrf())
    )
    .andExpect(status().is3xxRedirection())
    .andReturn();

    // 登録後のページを確認する
    var result = mockMvc.perform(get("/books"))
      .andExpect(status().isOk())
      .andReturn();

    try {
      var books = (List<BookForm>)result
        .getModelAndView().getModel().get("books");
      assertThat(books.size()).isEqualTo(1);

      var book = books.get(0);
      assertThat(book.getTitle()).isEqualTo(bookForm.getTitle());
      assertThat(book.getWritter()).isEqualTo(bookForm.getWritter());
      assertThat(book.getPublisher()).isEqualTo(bookForm.getPublisher());
      assertThat(book.getPrice()).isEqualTo(bookForm.getPrice());

    } catch (Exception e) {
      throw new Exception(e);
    }
  }
}
