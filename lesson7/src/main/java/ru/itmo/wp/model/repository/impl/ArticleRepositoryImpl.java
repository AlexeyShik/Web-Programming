package ru.itmo.wp.model.repository.impl;

import ru.itmo.wp.model.domain.Article;
import ru.itmo.wp.model.exception.RepositoryException;
import ru.itmo.wp.model.repository.ArticleRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArticleRepositoryImpl extends BasicRepositoryImpl implements ArticleRepository {
    @Override
    public Article find(long id) {
        return super.find(id, "Article", this::toArticle);
    }

    @Override
    public void save(Article article) {
        super.save("Article", article, this::toArticle,
                "(`userId`, `title`, `text`, `creationTime`, `hidden`) VALUES (?, ?, ?, NOW(), FALSE)",
                new String[]{String.valueOf(article.getUserId()), article.getTitle(), article.getText()});
    }

    @Override
    public List<Article> findAll() {
        return super.findAll("Article", this::toArticle, true);
    }

    @Override
    public List<Article> findAllByUserId(long userId) {
        List<Article> articles = new ArrayList<>();
        try (Connection connection = DATA_SOURCE.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM Article WHERE userId=? ORDER BY creationTime DESC")) {
                statement.setLong(1, userId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    Article article;
                    while ((article = toArticle(statement.getMetaData(), resultSet)) != null) {
                        articles.add(article);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't find Article", e);
        }
        return articles;
    }

    @Override
    public void setHidden(Article article) {
        updateBoolField("Article", article, true, "hidden");
    }

    @Override
    public void setShown(Article article) {
        updateBoolField("Article", article, false, "hidden");
    }


    private Article toArticle(ResultSetMetaData metaData, ResultSet resultSet) {
        try {
            if (!resultSet.next()) {
                return null;
            }
            Article article = new Article();
            article.setId(resultSet.getLong("id"));
            article.setCreationTime(resultSet.getTimestamp("creationTime"));
            article.setUserId(resultSet.getLong("userId"));
            article.setTitle(resultSet.getString("title"));
            article.setText(resultSet.getString("text"));
            article.setHidden(resultSet.getBoolean("hidden"));
            return article;
        } catch (SQLException e) {
            throw new RepositoryException("SQL Exception", e);
        }
    }
}
