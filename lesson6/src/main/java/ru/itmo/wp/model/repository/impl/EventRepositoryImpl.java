package ru.itmo.wp.model.repository.impl;

import ru.itmo.wp.model.domain.Event;
import ru.itmo.wp.model.exception.RepositoryException;
import ru.itmo.wp.model.repository.EventRepository;

import java.sql.*;

public class EventRepositoryImpl extends BasicRepositoryImpl implements EventRepository {

    @Override
    public Event find(long id) {
        return super.find(id, "Event", this::toEvent);
    }

    @Override
    public void save(Event event) {
        super.save("Event", event, this::toEvent, "(`userId`, `type`, `creationTime`) VALUES (?, ?, NOW())",
                new String[] {String.valueOf(event.getUserId()), event.getType().name()});
    }

    private Event toEvent(ResultSetMetaData metaData, ResultSet resultSet) {
        try {
            if (!resultSet.next()) {
                return null;
            }

            Event event = new Event();
            event.setId(resultSet.getLong("id"));
            event.setCreationTime(resultSet.getTimestamp("creationTime"));
            event.setType((Event.TYPE.valueOf((String) resultSet.getObject("type"))));
            event.setUserId(resultSet.getLong("userId"));
            return event;
        } catch (SQLException e) {
            throw new RepositoryException("SQL Exception", e);
        }
    }
}
