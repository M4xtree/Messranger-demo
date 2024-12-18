package com.messranger.repositories;

import com.messranger.entity.Members;
import com.messranger.model.FilterColumn;
import com.zaxxer.hikari.HikariDataSource;
import com.messranger.constants.SqlConstants;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MembersRepository extends BaseRepository<Members> {

    public MembersRepository(HikariDataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected String getTableName() {
        return "members";
    }

    @Override
    protected String getIdColumn() {
        return "chat_id, user_id";
    }

    @Override
    protected String[] getColumnNames() {
        return new String[]{"role", "can_delete_messages", "can_add_participants", "can_edit_messages", "caret", "joined_at"};
    }

    @Override
    protected String[] getColumnPlaceholders() {
        return new String[]{"?", "?", "?", "?", "?", "?", "?"};
    }

    @Override
    protected List<FilterColumn<?>> toFilterColumns(Members filter) {
        List<FilterColumn<?>> result = new ArrayList<>();
        Optional.ofNullable(filter.getChatId())
                .ifPresent(it -> result.add(new FilterColumn<>("chat_id", "=", it, (stmt, idx) -> stmt.setString(idx, it))));
        Optional.ofNullable(filter.getUserId())
                .ifPresent(it -> result.add(new FilterColumn<>("user_id", "=", it, (stmt, idx) -> stmt.setString(idx, it))));
        return result;
    }

    @Override
    protected void prepareInsertStatement(PreparedStatement statement, Members member) throws SQLException {
        statement.setString(1, member.getChatId());
        statement.setString(2, member.getUserId());
        statement.setString(3, member.getRole());
        statement.setBoolean(4, member.isCanDeleteMessages());
        statement.setBoolean(5, member.isCanAddParticipants());
        statement.setBoolean(6, member.isCanEditMessages());
        statement.setString(7, member.getCaret());
        statement.setTimestamp(8, Timestamp.valueOf(member.getJoinedAt()));
    }

    @Override
    protected void prepareUpdateStatement(PreparedStatement statement, Members member) throws SQLException {
        statement.setString(1, member.getRole());
        statement.setBoolean(2, member.isCanDeleteMessages());
        statement.setBoolean(3, member.isCanAddParticipants());
        statement.setBoolean(4, member.isCanEditMessages());
        statement.setString(5, member.getCaret());
        statement.setTimestamp(6, Timestamp.valueOf(member.getJoinedAt()));
        statement.setString(7, member.getChatId());
        statement.setString(8, member.getUserId());
    }

    @Override
    protected Members mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        return new Members(
                resultSet.getString("chat_id"),
                resultSet.getString("user_id"),
                resultSet.getString("role"),
                resultSet.getBoolean("can_delete_messages"),
                resultSet.getBoolean("can_add_participants"),
                resultSet.getBoolean("can_edit_messages"),
                resultSet.getString("caret"),
                resultSet.getTimestamp("joined_at").toLocalDateTime()
        );
    }

    public Optional<Members> find(String chatId, String userId) {
        return super.find(chatId, userId);
    }

    public void delete(String chatId, String userId) {
        super.delete(chatId, userId);
    }
    public Members update(Members instance) {
        String sql = SqlConstants.UPDATE + " members" + SqlConstants.SET
                + "role = ?, can_delete_messages = ?, can_add_participants = ?, can_edit_messages = ?, caret = ?, joined_at = ?"
                + SqlConstants.WHERE + "chat_id = ?" + SqlConstants.AND + "user_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, instance.getRole());
            statement.setBoolean(2, instance.isCanDeleteMessages());
            statement.setBoolean(3, instance.isCanAddParticipants());
            statement.setBoolean(4, instance.isCanEditMessages());
            statement.setString(5, instance.getCaret());
            statement.setTimestamp(6, Timestamp.valueOf(instance.getJoinedAt()));
            statement.setString(7, instance.getChatId());
            statement.setString(8, instance.getUserId());

            statement.executeUpdate();
            return instance;
        } catch (SQLException e) {
            LOGGER.error(e.toString());
            throw new RuntimeException("Error while updating member", e);
        }
    }


    public Members save(Members instance) {
        String insertSql = SqlConstants.INSERT + SqlConstants.INTO +"members (chat_id, user_id, role, can_delete_messages, can_add_participants, can_edit_messages, caret, joined_at)" + SqlConstants.VALUES +"(?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(insertSql)) {

            statement.setString(1, instance.getChatId());
            statement.setString(2, instance.getUserId());
            statement.setString(3, instance.getRole());
            statement.setBoolean(4, instance.isCanDeleteMessages());
            statement.setBoolean(5, instance.isCanAddParticipants());
            statement.setBoolean(6, instance.isCanEditMessages());
            statement.setString(7, instance.getCaret());
            statement.setTimestamp(8, Timestamp.valueOf(instance.getJoinedAt()));
            statement.executeUpdate();

            return instance;
        } catch (SQLException e) {
            LOGGER.error(e.toString());
            throw new RuntimeException("Error while saving instance", e);
        }
    }
}