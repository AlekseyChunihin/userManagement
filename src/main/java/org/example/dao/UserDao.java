package org.example.dao;

import org.example.entity.User;
import org.h2.jdbcx.JdbcConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao extends GenericJdbcDao {

    private static final Logger log = LoggerFactory.getLogger(UserDao.class);

    private static final String INSERT_USERS_SQL = "INSERT INTO user_table" + "  (name, email, country) VALUES "
            + " (?, ?, ?);";
    private static final String SELECT_USER_BY_ID = "select id,name,email,country from user_table where id =?";
    private static final String SELECT_ALL_USERS = "select * from user_table";
    private static final String DELETE_USERS_SQL = "delete from user_table where id = ?;";
    private static final String UPDATE_USERS_SQL = "update user_table set name = ?,email= ?, country =? where id = ?;";

    public UserDao() {
    }

    public void create(User user) {
        try (Connection connection = getConnectionPool().getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USERS_SQL)) {
                connection.setAutoCommit(false);
                preparedStatement.setString(1, user.getName());
                preparedStatement.setString(2, user.getEmail());
                preparedStatement.setString(3, user.getCountry());
                preparedStatement.executeUpdate();
                connection.commit();
            } catch (SQLException e) {
                log.error("error while inserting user: {}", e.getMessage());
                connection.rollback();
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            log.error("error while connecting to database: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public User findById(Long id) {
        User user = new User();
        try (Connection connection = getConnectionPool().getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID)) {
                preparedStatement.setLong(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    user.setId(resultSet.getLong("id"));
                    user.setName(resultSet.getString("name"));
                    user.setCountry(resultSet.getString("country"));
                    user.setEmail(resultSet.getString("email"));
                }
            } catch (SQLException e) {
                log.error("error while finding user: {}", e.getMessage());
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            log.error("error while connecting to database: {}", e.getMessage());
            throw new RuntimeException(e);
        }
        return user;
    }

    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try (Connection connection = getConnectionPool().getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERS)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                log.info("\t\t\tUsers:");
                while (resultSet.next()) {
                    User user = new User();
                    user.setId(resultSet.getLong("id"));
                    user.setName(resultSet.getString("name"));
                    user.setCountry(resultSet.getString("country"));
                    user.setEmail(resultSet.getString("email"));
                    log.info("{}", user);
                    users.add(user);
                }
            } catch (SQLException e) {
                log.error("error while find all users: {}", e.getMessage());
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            log.error("error while connecting to database: {}", e.getMessage());
            throw new RuntimeException(e);
        }
        return users;
    }

    public boolean update(User user) {
        boolean rowsUpdated;
        try (Connection connection = getConnectionPool().getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USERS_SQL)) {
                connection.setAutoCommit(false);
                preparedStatement.setString(1, user.getName());
                preparedStatement.setString(2, user.getEmail());
                preparedStatement.setString(3, user.getCountry());
                preparedStatement.setLong(4, user.getId());
                rowsUpdated = preparedStatement.executeUpdate() > 0;
                log.info("updated user with name: {}" + user.getName());
                connection.commit();
            } catch (SQLException e) {
                log.error("error while updating user: {}", e.getMessage());
                connection.rollback();
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            log.error("error while connecting to database: {}", e.getMessage());
            throw new RuntimeException(e);
        }
        return rowsUpdated;
    }

    public boolean delete(Long id) {
        boolean rowsDeleted;
        try (Connection connection = getConnectionPool().getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USERS_SQL)) {
                connection.setAutoCommit(false);
                preparedStatement.setLong(1, id);
                rowsDeleted = preparedStatement.executeUpdate() > 0;
                connection.commit();
            } catch (SQLException e) {
                log.error("error while deleting user: {}", e.getMessage());
                connection.rollback();
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            log.error("error while connecting to database: {}", e.getMessage());
            throw new RuntimeException(e);
        }
        return rowsDeleted;
    }
}
