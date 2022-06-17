package te4a.spring.boot.mybootapp13.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import te4a.spring.boot.mybootapp13.form.BookForm;
import te4a.spring.boot.mybootapp13.service.BookService;

@Controller
@RequestMapping("books")
public class BookController {
    @Autowired
    BookService bookService;

    // bookFormっていうのが自動的にmodelに設定される？
    // 多分、model.addAttributeをやってくれるっぽい
    // 名前は指定しなければクラス名をキャメルケースにするらしい
    @ModelAttribute
    BookForm setupForm() {
        return new BookForm();
    }

    @GetMapping
    String list(Model model){
        model.addAttribute("books", bookService.findAll());
        return "books/list";
    }

    // books/createにpostを送ってしまうと、送信後のURLがbooks/createになってしまうので、
    // postリクエストがあればcreateとみなす
    @PostMapping
    String create(@Validated BookForm form, BindingResult result, Model model, RedirectAttributes redirectAttributes){
        if(result.hasErrors()) {
            return list(model);
        }
        bookService.create(form);
        return "forward:/books";
    }

    @PostMapping(path = "edit", params = "form")
    String editForm(@RequestParam Integer id, BookForm form) {
        BookForm bookForm = bookService.findOne(id);
        BeanUtils.copyProperties(bookForm, form);
        return "books/edit";
    }

    //エラーがある状態で更新ボタンを押すと、他のフィールドが反映されない？
    //editFormでは、DBからidで指定されたBookBeanをformに詰め替えなおしているので、
    //変更が反映されていない?
    //入力されたformをそのまま返す別のメソッドを作ったらよさそう
    @PostMapping(path = "edit")
    String edit(@RequestParam Integer id, @Validated BookForm form, BindingResult result) {
        if(result.hasErrors()) {
            return editForm(id, form);
        }

        bookService.update(form);
        return "redirect:/books";
    }

    @PostMapping(path = "delete") 
    String delete(@RequestParam Integer id) {
        bookService.delete(id);
        return "redirect:/books";
    }

    @PostMapping(path = "edit", params = "goToTop")
    String goToTop() {
        return "redirect:/books";
    }
}
