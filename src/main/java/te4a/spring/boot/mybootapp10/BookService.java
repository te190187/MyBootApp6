package te4a.spring.boot.mybootapp10;

import java.lang.StackWalker.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService {
    @Autowired
    BookRepository bookRepository;

    public BookForm create(BookForm bookForm) {
        BookBean bookBean = new BookBean();
        BeanUtils.copyProperties(bookForm, bookBean);
        bookRepository.save(bookBean);

        return bookForm;
    }

    public BookForm update(BookForm bookForm) {
        BookBean bookBean = new BookBean();
        BeanUtils.copyProperties(bookForm, bookBean);
        bookRepository.save(bookBean);

        return bookForm;
    }

    public void delete(Integer id) {
        bookRepository.deleteById(id);
    }

    public List<BookForm> findAll() {
        List<BookBean> beanList = bookRepository.findAll();
        List<BookForm> formList = new ArrayList<>();
        
        for(BookBean bookBena: beanList) {
            BookForm bookForm = new BookForm();
            BeanUtils.copyProperties(bookBena, bookForm);
            formList.add(bookForm);
        }

        return formList;
    }

    public BookForm findOne(Integer id) {
        // OptionalからorElseで値を取り出す。
        // データが見つからなかった場合の処理は省略して、
        // 適当に新しいBookBeanをデフォルトで渡す。
        BookBean bookBean = bookRepository.findById(id).orElse(new BookBean());
        BookForm bookForm = new BookForm();
        BeanUtils.copyProperties(bookBean, bookForm);

        return bookForm;
    }
}
