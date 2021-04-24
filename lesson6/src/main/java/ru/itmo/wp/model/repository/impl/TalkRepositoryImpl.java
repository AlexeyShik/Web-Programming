package ru.itmo.wp.model.repository.impl;

import ru.itmo.wp.model.domain.Talk;
import ru.itmo.wp.model.exception.RepositoryException;
import ru.itmo.wp.model.repository.TalkRepository;

import java.sql.*;
import java.util.List;

public class TalkRepositoryImpl extends BasicRepositoryImpl implements TalkRepository {
    
    @Override
    public Talk find(long id) {
        return super.find(id, "Talk", this::toTalk);
    }

    @Override
    public List<Talk> findAll() {
        return super.findAll("Talk", this::toTalk);
    }

    @Override
    public void save(Talk talk) {
        super.save("Talk", talk, this::toTalk, "(`sourceUserId`, `targetUserId`, `text`, `creationTime`) VALUES (?, ?, ?, NOW())",
                new String[] {String.valueOf(talk.getSourceUserId()), String.valueOf(talk.getTargetUserId()), talk.getText()});
    }

    private Talk toTalk(ResultSetMetaData metaData, ResultSet resultSet) {
        try {
            if (!resultSet.next()) {
                return null;
            }

            Talk talk = new Talk();
            talk.setId(resultSet.getLong("id"));
            talk.setCreationTime(resultSet.getTimestamp("creationTime"));
            talk.setText(resultSet.getString("text"));
            talk.setSourceUserId(resultSet.getLong("sourceUserId"));
            talk.setTargetUserId(resultSet.getLong("targetUserId"));
            return talk;
        } catch (SQLException e) {
            throw new RepositoryException("SQL Exception", e);
        }
    }
}
