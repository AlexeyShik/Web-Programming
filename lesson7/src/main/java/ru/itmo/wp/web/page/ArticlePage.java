package ru.itmo.wp.web.page;

import ru.itmo.wp.model.domain.Article;
import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.exception.ValidationException;
import ru.itmo.wp.model.service.ArticleService;
import ru.itmo.wp.web.exception.RedirectException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class ArticlePage extends Page {
    private final ArticleService articleService = new ArticleService();

    private void create(HttpServletRequest request, Map<String, Object> view) throws ValidationException {
        Article article = new Article();
        article.setUserId(((User) request.getSession().getAttribute("user")).getId());
        String title = request.getParameter("title");
        String text = request.getParameter("text");

        articleService.validateArticle(title, text);
        article.setTitle(title);
        article.setText(text);

        articleService.save(article);
        request.getSession().setAttribute("message", "Your article is successfully created!");
        throw new RedirectException("/index");
    }
}
