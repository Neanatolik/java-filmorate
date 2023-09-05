package ru.yandex.practicum.filmorate.storage.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Primary
public class UserDbStorage implements UserStorage {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    final String ADD_USER = "MERGE INTO USERS (id, name, login, email, birthday) values (?,?,?,?,?)";
    final String ADD_USER_FRIENDS = "MERGE INTO FRIENDS (request_user_id, response_user_id) " +
            "KEY(request_user_id, response_user_id) values (?,?)";
    final String UPDATE_USER = "UPDATE USERS SET name = ?, login = ?, email = ?, birthday = ? WHERE id = ?";
    final String GET_USER = "SELECT * FROM USERS WHERE id = ?";
    final String GET_USERS = "SELECT * FROM USERS";
    final String GET_FRIENDS = "SELECT response_user_id FROM FRIENDS WHERE request_user_id = ?";
    final String DELETE_FRIEND = "DELETE FROM FRIENDS WHERE request_user_id = ? AND response_user_id = ?";
    private JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User addUser(User user) {
        jdbcTemplate.update(ADD_USER, user.getId(), user.getName(), user.getLogin(), user.getEmail(), user.getBirthday());
        for (Long id : user.getFriends()) {
            jdbcTemplate.update(ADD_USER_FRIENDS, user.getId(), id);
        }
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = jdbcTemplate.query(GET_USERS, ((rs, rowNum) -> {
            return makeUser(rs);
        }));
        return users;
    }

    @Override
    public User getUser(Long id) {
        return jdbcTemplate.queryForObject(GET_USER, (rs, rowNum) -> {
            return makeUser(rs);
        }, id);
    }

    @Override
    public Map<Long, User> getUsers() {
        Map<Long, User> users = new HashMap<>();
        for (User user : getAllUsers()) {
            users.put(user.getId(), user);
        }
        return users;
    }

    @Override
    public User updateUser(User user) {
        jdbcTemplate.update(UPDATE_USER, user.getName(), user.getLogin(), user.getEmail(), user.getBirthday(), user.getId());
        for (Long id : user.getFriends())
            jdbcTemplate.update(ADD_USER_FRIENDS, user.getId(), id);
        return user;
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        jdbcTemplate.update(DELETE_FRIEND, userId, friendId);
    }

    private List<Long> getFriends(Long id) {
        return jdbcTemplate.query(GET_FRIENDS, ((rs, rowNum) -> {
            return rs.getLong("response_user_id");
        }), id);
    }

    private User makeUser(ResultSet rs) throws SQLException {
        Long idUser = rs.getLong("id");
        String name = rs.getString("name");
        String login = rs.getString("login");
        String email = rs.getString("email");
        LocalDate birthday = rs.getDate("birthday").toLocalDate();
        User user = new User(name, login, email, birthday);
        for (Long friendId : getFriends(idUser)) {
            user.addFriend(friendId);
        }
        user.setId(idUser);
        return user;
    }

}
