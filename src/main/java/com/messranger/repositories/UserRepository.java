package com.messranger.repositories;

import com.messranger.entity.User;
import com.messranger.model.FilterColumn;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository extends BaseRepository<User> {
    public UserRepository(HikariDataSource dataSource) {
        super(dataSource);
    }


    @Override
    protected String getTableName() {
        return "users";
    }

    @Override
    protected String getIdColumn() {
        return "id";
    }

    @Override
    protected String[] getColumnNames() {
        return new String[]{"nickname", "phone_number"};
    }

    @Override
    protected String[] getColumnPlaceholders() {
        return new String[]{"?", "?"};
    }

    @Override
    protected List<FilterColumn<?>> toFilterColumns(User filter) {
        List<FilterColumn<?>> result = new ArrayList<>();
        Optional.ofNullable(filter.getId())
                .ifPresent(it -> result.add(new FilterColumn<>("id", "=", it, (stmt, idx) -> stmt.setString(idx, it))));
        Optional.ofNullable(filter.getNickname())
                .ifPresent(it -> result.add(new FilterColumn<>("nickname", "=", it, (stmt, idx) -> stmt.setString(idx, it))));
        Optional.ofNullable(filter.getPhoneNumber())
                .ifPresent(it -> result.add(new FilterColumn<>("phone_number", "=", it, (stmt, idx) -> stmt.setString(idx, it))));
        return result;
    }

    @Override
    protected void prepareInsertStatement(PreparedStatement statement, User user) throws SQLException {
        statement.setString(1, user.getId());
        statement.setString(2, user.getNickname());
        statement.setString(3, user.getPhoneNumber());
    }

    @Override
    protected void prepareUpdateStatement(PreparedStatement statement, User user) throws SQLException {
        statement.setString(1, user.getNickname());
        statement.setString(2, user.getPhoneNumber());
        statement.setString(3, user.getId());
    }

    @Override
    protected User mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        String nickname = resultSet.getString("nickname");
        String phoneNumber = resultSet.getString("phone_number");
        return new User(nickname, phoneNumber);
    }
}



















/*

public class UserRepository extends BaseRepository<User> {

    public UserRepository(HikariDataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected String getTableName() {
        return "users";
    }

    @Override
    protected List<String> getColumnNames() {
        return List.of(
                "id", "nickname", "phonenumber"
        );
    }

    @Override
    protected String getSaveSql() {
        return "INSERT INTO users (id, nickname, phonenumber) VALUES (?, ?, ?)";
    }

    @Override
    protected String getUpdateSql() {
        return "UPDATE users SET nickname = ? WHERE id = ?";
    }

    @Override
    protected String getFindSql() {
        return "SELECT id, nickname, phonenumber FROM users WHERE id = ?";
    }

    @Override
    protected String getDeleteSql() {
        return "DELETE FROM users WHERE id = ?";
    }

    @Override
    protected String getFindAllSql(PageRequest pageRequest) {
        return "SELECT id, nickname, phonenumber FROM users ORDER BY " + String.join(", ", pageRequest.getSortBy()) +
                " LIMIT ? OFFSET ?";
    }

    @Override
    protected void prepareSaveStatement(PreparedStatement statement, User user) throws SQLException {
        statement.setString(1, user.getId());
        statement.setString(2, user.getNickname());
        statement.setString(3, user.getPhoneNumber());
    }

    @Override
    protected void prepareUpdateStatement(PreparedStatement statement, User user) throws SQLException {
        statement.setString(1, user.getNickname());
        statement.setString(2, user.getId());
    }

    @Override
    protected User mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        String id = resultSet.getString("id");
        String phoneNumber = resultSet.getString("phonenumber");
        return new User(id, phoneNumber);
    }

    @Override
    protected List<User> mapResultSetToEntities(ResultSet resultSet) throws SQLException {
        List<User> users = new ArrayList<>();
        while (resultSet.next()) {
            users.add(mapResultSetToEntity(resultSet));
        }
        return users;
    }
}
*/

/*
public class UserRepository implements Repository<User> {

    private HikariDataSource dataSource;

    public UserRepository(HikariDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public User save(User instance) {
        String sql = "INSERT INTO users (id, nickname, phonenumber) VALUES (?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, instance.getId());
            statement.setString(2, instance.getNickname());
            statement.setString(3, instance.getPhoneNumber());
            statement.executeUpdate();

            return instance;
        } catch (SQLException e) {
            GlobalConfig.LOGGER.error(e.toString());
            throw new RuntimeException("Error while saving instance", e);
        }
    }

    @Override
    public User update(User instance) {
        String sql = "UPDATE users SET nickname = ? WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, instance.getNickname());
            statement.setString(2, instance.getId());
            statement.executeUpdate();

            return instance;
        } catch (SQLException e) {
            GlobalConfig.LOGGER.error(e.toString());
            throw new RuntimeException("Error while updating instance", e);
        }
    }

    @Override
    public Optional<User> find(String id) {
        String sql = "SELECT id, nickname, phonenumber FROM users WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                User user = new User(resultSet.getString("nickname"), resultSet.getString("phonenumber"));
                return Optional.of(user);
            }
        } catch (SQLException e) {
            GlobalConfig.LOGGER.error(e.toString());
            throw new RuntimeException("Error while finding instance", e);
        }
        return Optional.empty();
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            GlobalConfig.LOGGER.error(e.toString());
            throw new RuntimeException("Error while deleting instance", e);
        }
    }

    @Override
    public List<User> findAll(PageRequest pageRequest) {
        String sql = "SELECT id, nickname, phonenumber FROM users ORDER BY " + String.join(", ", pageRequest.getSortBy()) +
                " LIMIT ? OFFSET ?";
        List<User> users = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, pageRequest.getLimit());
            statement.setLong(2, pageRequest.getOffset());
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                User user = new User(resultSet.getString("nickname"), resultSet.getString("phonenumber"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error while finding all instances", e);
        }
        return users;
    }
}
*/

/*
public class UserManager{
    Connection connection;
    Statement statement;
    ResultSet output;

    public UserManager() {
        connection = new HikariDataSource().getConnection();
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/dbmessranger", "postgres", "admin");
            statement = connection.createStatement();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    public void insertUser (User user) {
        try {
            statement.executeUpdate("INSERT INTO USERS(id,nickname,phonenumber) " +
                    "VALUES ('" + user.getId() + "', '" + user.getNickname() + "', '" + user.getPhoneNumber() + "');");
        }   catch (SQLException sql){
            System.out.println("User:" + user.getId() + " with those phone number is registered.");
        }

    }
    public void deleteUser (User user) {
        try {
            statement.executeUpdate("DELETE FROM USERS WHERE id='" + user.getId() + "';");
        } catch (SQLException sql){
            System.out.println(user.getId() + " not found");
        }
    }
    public void getUser (User user) {
        try{
            ResultSet output = statement.executeQuery("SELECT nickname, phonenumber FROM USERS WHERE id = '" + user.getId() + "';");
            if(output.next()){
                System.out.println("users nickname: " + output.getString("nickname"));
                System.out.println("users phonenumber: " + output.getString("phonenumber"));
            }
        } catch (SQLException sql){
            System.out.println(user.getId() + " not found");
        }
    }

    public void deleteUserByPhoneNumber (String phonenumber) {
        try {
            statement.executeUpdate("DELETE FROM USERS WHERE phonenumber='" + phonenumber + "';");
        } catch (SQLException sql){
            System.out.println("User with Phone:" + phonenumber + " not found");
        }
    }

    public void close(){
        try{
            connection.close();
            statement.close();
            if(output != null) {
                output.close();
            }

        } catch (SQLException e) {
            System.out.println("Something wrong!");
        }
    }
}
*/