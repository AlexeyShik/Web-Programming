package ru.itmo.wp.web.page;

import ru.itmo.wp.model.domain.Article;
import ru.itmo.wp.model.exception.ValidationException;
import ru.itmo.wp.web.annotation.Json;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class MyArticlesPage extends Page {
    @Json
    private void findMyArticles(HttpServletRequest request, Map<String, Object> view) {
        long userId = getUser(request).getId();
        view.put("articles", articleService.findAllByUserId(userId));
    }

    private Article validateHidden(HttpServletRequest request) throws ValidationException {
        long id = Long.parseLong(request.getParameter("id"));
        Article article = articleService.find(id);
        if (getUser(request).getId() != article.getUserId()) {
            throw new ValidationException("You could update only your own articles");
        }
        return article;
    }

    @Json
    private void setHidden(HttpServletRequest request, Map<String, Object> view) throws ValidationException {
        Article article = validateHidden(request);
        articleService.setHidden(article);
        view.put("id", article.getId());
        view.put("hidden", true);
    }

    @Json
    private void setShown(HttpServletRequest request, Map<String, Object> view) throws ValidationException {
        Article article = validateHidden(request);
        articleService.setShown(article);
        view.put("id", article.getId());
        view.put("hidden", false);
    }
}
