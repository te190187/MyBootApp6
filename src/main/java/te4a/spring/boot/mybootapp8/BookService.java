package te4a.spring.boot.mybootapp8;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.qos.logback.core.joran.util.beans.BeanUtil;

@Service
public class BookService {
    @Autowired
    BookRepository bookRepository;

    public BookForm create(BookForm bookForm) {
        bookForm.setId(bookRepository.getBookId());
        BookBean bookBean = new BookBean();
        BeanUtils.copyProperties(bookForm, bookBean);
        bookRepository.create(bookBean);

        return bookForm;
    }

    public BookForm update(BookForm bookForm) {
        BookBean bookBean = new BookBean();
        BeanUtils.copyProperties(bookForm, bookBean);
        bookRepository.update(bookBean);

        return bookForm;
    }

    public void delete(Integer id) {
        bookRepository.delete(id);
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
        BookBean bookBean = bookRepository.findOne(id);
        BookForm bookForm = new BookForm();
        BeanUtils.copyProperties(bookBean, bookForm);

        return bookForm;
    }
}
