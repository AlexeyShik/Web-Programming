package ru.itmo.wp.model.repository.impl;

import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.exception.RepositoryException;
import ru.itmo.wp.model.repository.UserRepository;

import java.sql.*;
import java.util.List;

public class UserRepositoryImpl extends BasicRepositoryImpl implements UserRepository {

    @Override
    public User find(long id) {
        return super.find(id, "User", this::toUser);
    }

    private User findByParams(String name, String... params) {
        assert(params.length > 0 && params.length <= 2);
        try (Connection connection = DATA_SOURCE.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(String.format("SELECT * FROM User WHERE %s=? "
                    + (params.length == 1 ? "" : "AND %s=?"), name, "passwordSha"))) {
                statement.setString(1, params[0]);
                if (params.length > 1) {
                    statement.setString(2, params[1]);
                }
                try (ResultSet resultSet = statement.executeQuery()) {
                    return toUser(statement.getMetaData(), resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't find User.", e);
        }
    }


    @Override
    public User findByLogin(String login) {
        return findByParams("login", login);
    }

    @Override
    public User findByEmail(String email) {
        return findByParams("email", email);
    }

    @Override
    public User findByLoginAndPasswordSha(String login, String passwordSha) {
        return findByParams("login", login, passwordSha);
    }

    @Override
    public User findByEmailAndPasswordSha(String email, String passwordSha) {
        return findByParams("email", email, passwordSha);
    }

    @Override
    public List<User> findAll() {
        return super.findAll("User", this::toUser);
    }

    private User toUser(ResultSetMetaData metaData, ResultSet resultSet) {
        try {
            if (!resultSet.next()) {
                return null;
            }

            User user = new User();
            user.setId(resultSet.getLong("id"));
            user.setCreationTime(resultSet.getTimestamp("creationTime"));
            user.setEmail(resultSet.getString("email"));
            user.setLogin(resultSet.getString("login"));
            return user;
        } catch (SQLException e) {
            throw new RepositoryException("SQL Exception", e);
        }
    }

    @Override
    public void save(User user, String passwordSha) {
        super.save("User", user, this::toUser, "(`login`, `passwordSha`, `creationTime`, `email`) VALUES (?, ?, NOW(), ?)",
                new String[] {user.getLogin(), passwordSha, user.getEmail()});
    }

    @Override
    public int findCount() {
        try (Connection connection = DATA_SOURCE.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM User")) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (!resultSet.next()) {
                        throw new RepositoryException("Error while count query");
                    }
                    return resultSet.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't find count.", e);
        }
    }
}
