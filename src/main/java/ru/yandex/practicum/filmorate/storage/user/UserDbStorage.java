package ru.yandex.practicum.filmorate.storage.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Component
public class UserDbStorage implements UserStorage {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User addUser(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
        user.setId(simpleJdbcInsert.executeAndReturnKey(user.toMap()).longValue());
        log.debug(user.toString());
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        String getUsers = "SELECT * FROM USERS";
        List<User> users = jdbcTemplate.query(getUsers, ((rs, rowNum) -> makeUser(rs)));
        log.debug(users.toString());
        return users;
    }

    @Override
    public User getUser(Long id) {
        log.debug(id.toString());
        String getUser = "SELECT * FROM USERS WHERE id = ?";
        return jdbcTemplate.queryForObject(getUser, (rs, rowNum) -> {
            return makeUser(rs);
        }, id);
    }

    @Override
    public User updateUser(User user) {
        String updateUser = "UPDATE USERS SET name = ?, login = ?, email = ?, birthday = ? WHERE id = ?";
        jdbcTemplate.update(updateUser, user.getName(), user.getLogin(), user.getEmail(), user.getBirthday(), user.getId());
        log.debug(user.toString());
        return user;
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        String deleteFriend = "DELETE FROM FRIENDS WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(deleteFriend, userId, friendId);
        log.debug(userId.toString(), friendId.toString());
    }

    @Override
    public void addFriend(Long friendId, Long userId) {
        String addUserFriends = "MERGE INTO FRIENDS (user_id, friend_id) " +
                "KEY(user_id, friend_id) values (?,?)";
        jdbcTemplate.update(addUserFriends, friendId, userId);
    }

    @Override
    public List<User> getFriends(Long id) {
        String getFriends = "SELECT * FROM FRIENDS f INNER JOIN users u ON f.friend_id = u.ID WHERE user_id = ?";
        return jdbcTemplate.query(getFriends, ((rs, rowNum) -> makeUser(rs)), id);
    }

    @Override
    public List<Long> getUsersId() {
        String getUsersId = "SELECT id FROM USERS";
        return jdbcTemplate.query(getUsersId, ((rs, rowNum) -> rs.getLong("id")));
    }

    @Override
    public List<Long> getFriendsId(Long otherId) {
        String getFriendsId = "SELECT friend_id FROM FRIENDS WHERE user_id = ?";
        return jdbcTemplate.query(getFriendsId, ((rs, rowNum) -> rs.getLong("friend_id")), otherId);
    }

    private User makeUser(ResultSet rs) throws SQLException {
        Long idUser;
        try {
            idUser = rs.getLong("friend_id");
        } catch (SQLException e) {
            idUser = rs.getLong("id");
        }
        String name = rs.getString("name");
        String login = rs.getString("login");
        String email = rs.getString("email");
        LocalDate birthday = rs.getDate("birthday").toLocalDate();
        User user = new User(name, login, email, birthday);
        user.setId(idUser);
        return user;
    }

}
