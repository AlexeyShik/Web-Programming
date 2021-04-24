package ru.itmo.wp.model.repository.impl;

import ru.itmo.wp.model.database.DatabaseUtils;
import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.exception.RepositoryException;
import ru.itmo.wp.model.repository.UserRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

public class UserRepositoryImpl extends BasicRepositoryImpl implements UserRepository {
    private final DataSource DATA_SOURCE = DatabaseUtils.getDataSource();

    @Override
    public User find(long id) {
        return super.find(id, "User_HW_7", this::toUser);
    }

    private User findByParams(String... params) {
        assert(params.length > 0 && params.length <= 2);
        try (Connection connection = DATA_SOURCE.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(String.format("SELECT * FROM User_HW_7 WHERE %s=? "
                    + (params.length == 1 ? "" : "AND %s=?"), "login", "passwordSha"))) {
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
        return findByParams(login);
    }

    @Override
    public User findByLoginAndPasswordSha(String login, String passwordSha) {
        return findByParams(login, passwordSha);
    }

    @Override
    public List<User> findAll() {
        return super.findAll("User_HW_7", this::toUser, false);
    }

    private User toUser(ResultSetMetaData metaData, ResultSet resultSet) {
        try {
            if (!resultSet.next()) {
                return null;
            }

            User user = new User();
            user.setId(resultSet.getLong("id"));
            user.setCreationTime(resultSet.getTimestamp("creationTime"));
            user.setLogin(resultSet.getString("login"));
            user.setAdmin(resultSet.getBoolean("admin"));
            return user;
        } catch (SQLException e) {
            throw new RepositoryException("SQL Exception", e);
        }
    }

    @Override
    public void save(User user, String passwordSha) {
        super.save("User_HW_7", user, this::toUser,
                "(`login`, `passwordSha`, `creationTime`, `admin`) VALUES (?, ?, NOW(), ?)",
                new String[]{user.getLogin(), passwordSha, "false"});
    }

    @Override
    public void setAdmin(User user) {
        updateBoolField("User_HW_7", user, true, "admin");
    }

    @Override
    public void unsetAdmin(User user) {
        updateBoolField("User_HW_7", user, false, "admin");
    }
}
