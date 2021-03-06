package ru.itmo.wp.model.repository.impl;

import ru.itmo.wp.model.database.DatabaseUtils;
import ru.itmo.wp.model.domain.Entity;
import ru.itmo.wp.model.exception.RepositoryException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class BasicRepositoryImpl {
    protected final DataSource DATA_SOURCE = DatabaseUtils.getDataSource();

    protected <T extends Entity> T find(long id, String tableName, BiFunction<ResultSetMetaData, ResultSet, T> builder) {
        try (Connection connection = DATA_SOURCE.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(String.format("SELECT * FROM %s WHERE id=?", tableName))) {
                statement.setLong(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    return builder.apply(statement.getMetaData(), resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't find " + tableName, e);
        }
    }

    protected <T extends Entity> List<T> findAll(String tableName, BiFunction<ResultSetMetaData, ResultSet, T> builder, boolean isDesc) {
        List<T> talks = new ArrayList<>();
        try (Connection connection = DATA_SOURCE.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(String.format("SELECT * FROM %s ORDER BY creationTime"
                    + (isDesc ? " DESC ": ""), tableName))) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    T item;
                    while ((item = builder.apply(statement.getMetaData(), resultSet)) != null) {
                        talks.add(item);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't find " + tableName, e);
        }
        return talks;
    }

    public <T extends Entity> void save(String tableName, T entity, BiFunction<ResultSetMetaData, ResultSet, T> builder,
                                        String sqlQueryParams, String[] statementParams) {
        try (Connection connection = DATA_SOURCE.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(String.format("INSERT INTO %s %s", tableName, sqlQueryParams), Statement.RETURN_GENERATED_KEYS)) {
                for (int i = 1; i <= statementParams.length; ++i) {
                    if (tableName.equals("Article") && i == 1) {
                        statement.setLong(i, Long.parseLong(statementParams[i - 1]));
                    } else if (tableName.equals("User_HW_7") && i == 3) {
                        statement.setBoolean(i, Boolean.parseBoolean(statementParams[i - 1]));
                    } else {
                        statement.setString(i, statementParams[i - 1]);
                    }
                }
                if (statement.executeUpdate() != 1) {
                    throw new RepositoryException("Can't save " + tableName);
                } else {
                    ResultSet generatedKeys = statement.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        entity.setId(generatedKeys.getLong(1));
                        entity.setCreationTime(find(entity.getId(), tableName, builder).getCreationTime());
                    } else {
                        throw new RepositoryException("Can't save " + tableName + " [no autogenerated fields].");
                    }
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't save " + tableName, e);
        }
    }

    protected <T extends Entity> void updateBoolField(String tableName, T entity, boolean value, String sqlQueryParam) {
        try (Connection connection = DATA_SOURCE.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(String.format("UPDATE %s SET %s = ? WHERE id=?", tableName, sqlQueryParam))) {
                statement.setBoolean(1, value);
                statement.setLong(2, entity.getId());
                if (statement.executeUpdate() != 1) {
                    throw new RepositoryException(String.format("Can't update %s, id = ", tableName) + entity.getId());
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException(String.format("Can't update %s in %s, id = ", sqlQueryParam, tableName) + entity.getId());
        }
    }
}
